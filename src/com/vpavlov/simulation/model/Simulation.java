package com.vpavlov.simulation.model;

import com.vpavlov.map.Path;
import com.vpavlov.map.api.IOasisPathsGetter;
import com.vpavlov.map.GraphEntity;
import com.vpavlov.map.MapGraph;
import com.vpavlov.simulation.events.DrinkPlace;
import com.vpavlov.simulation.events.EventLogger;
import com.vpavlov.simulation.exceptions.NoGoodsException;
import com.vpavlov.console.ConsoleColor;
import com.vpavlov.console.LogType;
import com.vpavlov.console.Logger;
import com.vpavlov.simulation.helpers.*;
import com.vpavlov.map.Point;

import java.util.*;
import java.util.List;

/**
 * Class represents simulation
 *
 * @author vpavlov
 */
public class Simulation {

    /**
     * List of warehouses
     */
    private final List<Warehouse> warehouses = new ArrayList<>();

    /**
     * Queue of warehouse supplies by next supply time
     */
    private final PriorityQueue<WarehouseSupply> warehouseSupplies = new PriorityQueue<>();

    /**
     * List of oases
     */
    private final List<Oasis> oases = new ArrayList<>();

    /**
     * List of camels to return to warehouses
     */
    private final PriorityQueue<Camel> camelsToReturn = new PriorityQueue<>();

    /**
     * List of camels
     */
    private final PriorityQueue<CamelType> camelTypes = new PriorityQueue<>();

    /**
     * List of requests
     */
    private final PriorityQueue<Request> requests = new PriorityQueue<>();

    /**
     * Map of warehouses and oases
     */
    private final MapGraph map;

    /**
     * Event logger
     */
    private final EventLogger eventLogger;

    /**
     * File and console logger
     */
    private final Logger logger;

    /**
     * Count of roads
     */
    private final int roadsCount;

    /**
     * Time of the simulation start
     */
    private final long startTime = System.currentTimeMillis();


    /**
     * Flag if simulation is crashed
     */
    private boolean isCrashed = false;

    /**
     * Constructor
     *
     * @param params parameters of the simulation
     * @param logger logger
     */
    public Simulation(String[] params, Logger logger) {
        logger.log("Simulation building..", LogType.DEBUG);

        this.logger = logger;
        eventLogger = new EventLogger(logger);
        int i = 0;
        int warehouseCount = Integer.parseInt(params[i++]);
        List<Point> points = new ArrayList<>();

        logger.log("Adding warehouses", LogType.DEBUG);

        for (int j = 0; j < warehouseCount; ++j) {
            Warehouse w = new Warehouse(
                    j,
                    Double.parseDouble(params[i++]),
                    Double.parseDouble(params[i++]),
                    Integer.parseInt(params[i++]),
                    Double.parseDouble(params[i++]),
                    Double.parseDouble(params[i++])
            );
            points.add(w.getLocation());
            warehouses.add(w);
            warehouseSupplies.add(new WarehouseSupply(w));
        }

        logger.log("Warehouses has been added", LogType.DEBUG);
        logger.log("Adding oases", LogType.DEBUG);

        int oasesCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < oasesCount; ++j) {
            Oasis o = new Oasis(
                    j + warehouseCount,
                    Double.parseDouble(params[i++]),
                    Double.parseDouble(params[i++])
            );
            points.add(o.getLocation());
            oases.add(o);
        }
        logger.log("Oases has been added", LogType.DEBUG);
        logger.log("Adding paths", LogType.DEBUG);

        map = new MapGraph(points, camelTypes, warehouses);
        this.roadsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < this.roadsCount; ++j) {
            int p1 = Integer.parseInt(params[i++]) - 1;
            int p2 = Integer.parseInt(params[i++]) - 1;
            map.addBidirectionalEdge(p1, p2);
        }

        logger.log("Paths has been added", LogType.DEBUG);
        logger.log("Adding camel types", LogType.DEBUG);

        int camelsCount = Integer.parseInt(params[i++]);

        for (int j = 0; j < camelsCount; ++j) {
            CamelType c = new CamelType(
                    params[i++],
                    Double.parseDouble(params[i++]),
                    Double.parseDouble(params[i++]),
                    Double.parseDouble(params[i++]),
                    Double.parseDouble(params[i++]),
                    Double.parseDouble(params[i++]),
                    Integer.parseInt(params[i++]),
                    Double.parseDouble(params[i++])
            );
            camelTypes.add(c);
        }

        logger.log("Camel types has been added", LogType.DEBUG);
        logger.log("Adding requests", LogType.DEBUG);

        int requestsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < requestsCount; j++) {
            Request r = new Request(
                    Double.parseDouble(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Double.parseDouble(params[i++])
            );
            requests.add(r);
        }
        logger.log("Request has been added", LogType.DEBUG);

        logger.log("Simulation has been built", LogType.DEBUG);
    }

    /**
     * Supply all warehouses
     *
     * @param currentTime current simulation time
     */
    private void supplyWarehouses(double currentTime) {
        WarehouseSupply warehouseSupply;
        Warehouse w;
        for (; ; ) {
            warehouseSupply = warehouseSupplies.peek();
            if (warehouseSupply == null) {
                break;
            }
            if (warehouseSupply.getNextSupplyTime() <= currentTime) {
                w = warehouseSupply.warehouse();
                warehouseSupplies.poll();
                eventLogger.addWarehouseSupplyEvent(currentTime, w.getId(), w.supply());
                warehouseSupplies.add(new WarehouseSupply(w));
            } else {
                break;
            }
        }
    }

    /**
     * Check if this camel type can pass the path
     *
     * @param w       start warehouse
     * @param path    path to pass
     * @param type    camel type to check
     * @param goods   amount of goods to load
     * @param timeout time for request processing
     * @return minimal possible speed for this path with this camel type to pass.
     * If it's negative then camel cannot pass
     */
    private double computePathForCamelType(Warehouse w, Path path, CamelType type, int goods, double timeout) {
        //System.out.println("[DEBUG] computePathForCamelType");
        List<Point> points = path.getPoints();
        double stamina = type.getEffectiveDistance();
        double transitionDistance;
        double time = w.getLoadingTime() * goods * 2;         //uploading and unloading
        double minPossibleSpeed;
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            transitionDistance = Point.getDistance(p1, p2);
            if (stamina < transitionDistance) {
                time += type.getDrinkTime();
                stamina = type.getEffectiveDistance();
            }
            stamina -= transitionDistance;
        }
        if (Double.compare(timeout, time) <= 0) {
            return Double.POSITIVE_INFINITY;
        }
        minPossibleSpeed = path.getDistance() / (timeout - time);
        return minPossibleSpeed;
    }

    /**
     * Find a fitting camel type to pass the path in time
     *
     * @param path    path to pass
     * @param goods   amount of goods in request
     * @param timeout time to process the request
     * @return path, fitting camel type and minimal possible speed to pass the path
     * @throws NoGoodsException if there are not enough goods fot the request
     */
    private PathCamelType getFitCamelType(Path path, int goods, double timeout) throws NoGoodsException {
        //System.out.println("[DEBUG] getFitCamelType");
        boolean noGoods = true;
        double minPossibleSpeed;
        Warehouse w = warehouses.get(path.getWarehouseId());
        for (CamelType type : camelTypes) {
            if (w.getGoodsAmount() < Math.min(goods, type.getMaxLoad())) {
                continue;
            }
            noGoods = false;
            if ((path.getMaxTransitionDistance() > type.getEffectiveDistance())) {
                continue;
            }
            minPossibleSpeed = computePathForCamelType(w, path, type, goods, timeout);
            if (minPossibleSpeed < 0 || minPossibleSpeed > type.getMaxSpeed()) {
                continue;
            }
            return new PathCamelType(type, path, minPossibleSpeed);
        }
        if (noGoods) {
            throw new NoGoodsException();
        }
        return null;
    }

    /**
     * Find fitting path and camel type for the request
     *
     * @param pathGetter path getter for the oasis
     * @param goods      goods to deliver
     * @param timeout    time for pass the path
     * @return fitting path, fitting camel type and minimal possible speed to pass the path
     * @throws NoGoodsException if there are not enough goods fot the request
     */
    private PathCamelType getRequestFitCamelType(IOasisPathsGetter pathGetter, int goods, double timeout) throws NoGoodsException {
        //System.out.println("[DEBUG] getRequestFitCamelType");
        PathCamelType fitCamelType = null;
        PriorityQueue<Path> paths;
        Iterator<Path> it;
        while (pathGetter.hasNext()) {
            paths = pathGetter.getNextPaths();
            if (paths != null) {
                it = paths.iterator();
                while (it.hasNext()) {
                    Path path = it.next();
                    try {
                        fitCamelType = getFitCamelType(path, goods, timeout);
                    } catch (NoGoodsException e) {
                        if (!it.hasNext() && !pathGetter.hasNext()) {
                            throw e;
                        }
                    }
                    if (fitCamelType != null) {
                        return fitCamelType;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Computes camel returning to the corresponding warehouse
     *
     * @param pathCamel path - camel pair
     * @param start     start time
     */
    private void returnCamelPath(PathCamel pathCamel, double start) {
        // System.out.println("[DEBUG] returnCamelPath");
        Camel camel = pathCamel.camel();
        Path path = pathCamel.path();
        List<Point> points = path.getPoints();
        double time = start;
        for (int i = points.size() - 1; i > 0; --i) {
            Point p1 = points.get(i);
            Point p2 = points.get(i - 1);
            time = pointsTransition(time, path.getOasisId(), p1, p2, camel);
        }
        camel.setReturnTime(time);
        camelsToReturn.add(camel);
    }

    /**
     * Computes camel path to the oasis
     *
     * @param pathCamel path - camel pair
     * @param goods     goods to deliver
     * @param start     start time
     * @param timeout   time for request processing
     * @return amount of goods that left in the request
     * @throws NoGoodsException if there are not enough goods fot the request
     */
    private int computePathForCamel(PathCamel pathCamel, int goods, double start, double timeout) throws NoGoodsException {
        //System.out.println("[DEBUG] computePathForCamel");
        Camel camel = pathCamel.camel();
        Path path = pathCamel.path();
        Warehouse warehouse = warehouses.get(path.getWarehouseId());
        List<Point> points = path.getPoints();
        double time = start;
        double loadingTime = warehouse.getLoadingTime();
        int maxLoad = camel.getType().getMaxLoad();
        int prepareGoods = Math.min(goods, maxLoad);
        double prepareTime = prepareGoods * loadingTime;

        warehouse.removeGoods(prepareGoods);

        //preparing camel
        eventLogger.addCamelLoadEvent(time, camel.getId(), warehouse.getId(), prepareGoods, prepareTime);

        time += prepareTime;

        //road
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            time = pointsTransition(time, path.getWarehouseId(), p1, p2, camel);
        }

        eventLogger.addCamelArriveEvent(time, camel.getId(), path.getOasisId(), prepareGoods, prepareTime, timeout);

        returnCamelPath(pathCamel, time);
        return prepareGoods;
    }

    /**
     * Calculate camel path between two points
     *
     * @param time    current path time
     * @param startId path start point
     * @param p1      first point
     * @param p2      second point
     * @param camel   camel on the path
     * @return transition time
     */
    private double pointsTransition(double time, int startId, Point p1, Point p2, Camel camel) {
        double transitionDistance = Point.getDistance(p1, p2);
        double drinkTime = camel.getType().getDrinkTime();
        if (Double.compare(camel.getStamina(), transitionDistance) < 0) {
            if (p1.id() < warehouses.size()) {
                //stopped in warehouse
                eventLogger.addCamelDrinkEvent(DrinkPlace.WAREHOUSE, time, camel.getId(), p1.id(), camel.getType().getName(), drinkTime);
            } else {
                //stopped in oasis
                eventLogger.addCamelDrinkEvent(DrinkPlace.OASIS, time, camel.getId(), p1.id(), camel.getType().getName(), drinkTime);
            }
            time += camel.drink();
        } else {
            if (p1.id() != startId) {
                //transit
                eventLogger.addCamelIgnoreEvent(time, camel.getId(), p1.id());
            }
        }
        camel.removeStamina(transitionDistance);
        time += (transitionDistance / camel.getSpeed());
        return time;
    }

    /**
     * Find a fitting path and camel for the request
     *
     * @param request request to process
     * @return fitting path and camel
     * @throws NoGoodsException if there are not enough goods fot the request
     */
    private PathCamel camelPrepare(Request request) throws NoGoodsException {
        //System.out.println("[DEBUG] camelPrepare");
        int oasisId = request.getOasisId() + warehouses.size() - 1;
        //System.out.println("[DEBUG] OasisId="+oasisId);
        IOasisPathsGetter pathsGetter = map.getPathsForOasis(oasisId);
        PathCamelType fitCamelType;
        Camel camel;
        Iterator<Camel> iterator;
        Warehouse w;
        fitCamelType = getRequestFitCamelType(pathsGetter, request.getGoodsCount(), request.getTimeout());
        if (fitCamelType == null) {
            return null;
        }
        w = warehouses.get(fitCamelType.path().getWarehouseId());
        iterator = w.getCamelsByType(fitCamelType.camelType()).iterator();
        do {
            if (iterator.hasNext()) {
                camel = iterator.next();
            } else {
                camel = null;
                iterator = w.generateCamels(fitCamelType.camelType(), camelTypes).iterator();
            }
        } while ((camel == null) || (camel.getSpeed() < fitCamelType.minSpeed()) || (camel.getDistance() < fitCamelType.path().getMaxTransitionDistance()));

        w.removeCamel(camel);

        return new PathCamel(fitCamelType.path(), camel);
    }

    /**
     * compute the request
     *
     * @param request     request to process
     * @param currentTime current time
     * @return true if computing was success, else - false
     * @throws NoGoodsException if there are not enough goods fot the request
     */
    private boolean computeRequest(Request request, double currentTime) throws NoGoodsException {
        //System.out.println("[DEBUG] computeRequest");
        int goods = request.getGoodsCount();
        PathCamel pathCamel;
        while (goods > 0) {
            pathCamel = camelPrepare(request);
            if (pathCamel == null) {
                return false;
            }
            try {
                goods -= computePathForCamel(pathCamel, goods, currentTime, request.getTime() + request.getTimeout());
            } catch (NoGoodsException e) {
                request.setGoodsCount(goods);
                throw e;
            }
        }
        return true;
    }

    /**
     * Check if warehouses supply must be done before the requests are processed
     *
     * @param currentTime current simulation time
     * @return true if warehouse supply must be done before requests are processed, else false
     */
    private boolean isSuppliesFirst(double currentTime) {
        WarehouseSupply supplier = warehouseSupplies.peek();
        if (supplier == null) {
            return false;
        }
        int compare = Double.compare(currentTime, supplier.getNextSupplyTime());
        return compare >= 0;
    }

    /**
     * Starts the harpagon simulation
     */
    public void simulate() {
        double nextSupply;
        while (!isCrashed && !requests.isEmpty()) {
            nextSupply = Double.POSITIVE_INFINITY;
            List<Request> actualRequests = getActualRequests();
            actualRequests.sort(Request::sortByTimeout);
            double currentTime = actualRequests.get(0).getPostponeTime();
            returnCamels(currentTime);

            if (isSuppliesFirst(currentTime)) {
                supplyWarehouses(currentTime);
            }
            if (warehouseSupplies.peek() != null) {
                nextSupply = warehouseSupplies.peek().getNextSupplyTime();

            }
            eventLogger.log(currentTime);
            for (Request request : actualRequests) {
                try {
                    if (!computeRequest(request, currentTime)) {
                        simulationCrashed(request, currentTime);
                        isCrashed = true;
                        break;
                    }
                } catch (NoGoodsException e) {
                    if (!request.postpone(nextSupply)) {
                        simulationCrashed(request, currentTime);
                        isCrashed = true;
                        break;
                    } else {
                        requests.add(request);
                    }
                }
            }
        }
        if (!isCrashed) {
            returnCamels(Double.POSITIVE_INFINITY);
            eventLogger.logAll();
        }
        countCamels();
        countCamelsByType();

        simulationEnd();
    }

    /**
     * Log info about simulation crash
     *
     * @param request     current request
     * @param currentTime current simulation time
     */
    private void simulationCrashed(Request request, double currentTime) {
        logger.log(
                String.format(
                        "Cas: %d, Oaza: %d, Vsichni vymreli, Harpagon zkrachoval, Konec simulace",
                        Math.round(currentTime),
                        request.getOasisId()
                ),
                LogType.ERROR
        );
    }

    /**
     * Compute camels returning
     *
     * @param currentTime current simulation time
     */
    private void returnCamels(double currentTime) {
        Camel camel;
        for (; ; ) {
            camel = camelsToReturn.peek();
            if (camel == null) {
                break;
            }
            if (camel.getReturnTime() <= currentTime) {
                camelsToReturn.poll();
                eventLogger.addCamelReturnEvent(camel.getReturnTime(), camel.getId(), camel.getWarehouseId());
                warehouses.get(camel.getWarehouseId()).returnCamel(camel);
            } else {
                break;
            }
        }
    }

    /**
     * Calculate simulation time
     */
    private void simulationEnd() {
        String result = isCrashed ? "FAILED" : "SUCCESS";
        double elapsed = (System.currentTimeMillis() - startTime) / 1e3;
        logger.log(
                String.format("Simulation ended in %f seconds. Result: %s", elapsed, result),
                LogType.HEADER
        );
    }

    /**
     * Count all generated camels in all warehouses
     */
    private void countCamels() {
        int count = 0;
        for (Warehouse w : warehouses) {
            count += w.getCamelsCount();
        }
        logger.log(
                String.format("Total camels count: %d", count),
                LogType.INFO
        );
    }

    /**
     * Calculate all generated camels in all warehouses by type
     */
    private void countCamelsByType() {
        long count = 0;
        for (CamelType type : camelTypes) {
            for (Warehouse w : warehouses) {
                count += w.getCamelsCount(type);
            }

            logger.log(
                    String.format("For the type [%s : %.3f] total camels count: %d", type.getName(), type.getProportion(), count),
                    LogType.INFO
            );

            count = 0;
        }
    }

    /**
     * Gets next requests that have the same arrive time
     *
     * @return list of requests with the same arrive time
     */
    public List<Request> getActualRequests() {
        List<Request> requestList = new LinkedList<>();
        Request request, prevRequest = null;

        for (; ; ) {
            request = requests.peek();
            if (request == null) {
                return requestList;
            }
            if ((prevRequest == null) || (prevRequest.getTime() == request.getTime())) {
                prevRequest = request;
                requestList.add(request);
                requests.poll();
            } else {
                return requestList;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append(ConsoleColor.ANSI_GREEN).append("-------------------------------->>WAREHOUSES [").append(warehouses.size()).append("]<<--------------------------------\n\n").append(ConsoleColor.ANSI_RESET);
        str.append(ConsoleColor.ANSI_YELLOW).append("{x} - X-coordinate\n{y} - Y-coordinate\n{ks} - supply amount\n{ts} - supply timeout\n{tn} - goods loading time\n{tc} - goods amount\n{ps} - previous supply\n\n").append(ConsoleColor.ANSI_RESET);
        for (Warehouse warehouse : warehouses) {
            str.append(warehouse.toString()).append("\n");
        }

        str.append(ConsoleColor.ANSI_GREEN).append("\n-------------------------------->>OASES [").append(oases.size()).append("]<<--------------------------------\n\n").append(ConsoleColor.ANSI_RESET);
        for (Oasis oasis : oases) {
            str.append(oasis.toString()).append("\n");
        }

        str.append(ConsoleColor.ANSI_GREEN).append("\n-------------------------------->>ROADS [").append(this.roadsCount).append("]<<--------------------------------\n\n").append(ConsoleColor.ANSI_RESET);
        str.append(ConsoleColor.ANSI_YELLOW).append("[i]<-->[j] {i,j} - indexes of oasis or path\n[0,S] - warehouses, [S+1, S+O] - oases\nS - number of warehouses, O - number of oases\n\n").append(ConsoleColor.ANSI_RESET);
        int road = 0;
        List<GraphEntity> graph = map.getGraph();
        for (int i = 0; i < graph.size(); i++) {
            Set<Integer> neighbours = graph.get(i).neighbours;
            if (neighbours != null) {
                for (int j : neighbours) {
                    str.append("[").append(++road).append("] ").append(i + 1).append("<-->").append(j + 1).append("\n");
                }
            }
        }

        str.append(ConsoleColor.ANSI_GREEN).append("\n-------------------------------->>CAMEL TYPES [").append(camelTypes.size()).append("]<<--------------------------------\n\n").append(ConsoleColor.ANSI_RESET);
        str.append(ConsoleColor.ANSI_YELLOW).append("{camelType} - camel camelType\n{vmin} - minimal speed\n{vmax} - maximal speed\n{dmin} - minimal distance\n{dmax} - maximal distance\n{td} - time to drink\n{kd} - maximal load\n{pd} - herd proportion   \n\n").append(ConsoleColor.ANSI_RESET);
        for (CamelType camelType : camelTypes) {
            str.append(camelType.toString()).append("\n");
        }

        str.append(ConsoleColor.ANSI_GREEN).append("\n-------------------------------->>REQUESTS [").append(requests.size()).append("]<<--------------------------------\n\n").append(ConsoleColor.ANSI_RESET);
        str.append(ConsoleColor.ANSI_YELLOW).append("{tz} - request arrival time\n{tp} - request timeout\n{op} - oasis index to deliver\n{kp} - goods count\n\n").append(ConsoleColor.ANSI_RESET);
        for (Request request : requests) {
            str.append(request.toString()).append("\n");
        }

        return str.toString();
    }
}
