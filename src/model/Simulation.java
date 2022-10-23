package model;

import console.LogType;
import console.Logger;
import helpers.*;
import helpers.Point;

import java.util.*;
import java.util.List;

import static console.LogType.HEADER;

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
     * Constructor
     *
     * @param params -parameters of the simulation
     */
    public Simulation(String[] params, Logger logger) {
        this.logger = logger;
        eventLogger = new EventLogger(logger);
        int i = 0;
        int warehouseCount = Integer.parseInt(params[i++]);
        List<Point> points = new ArrayList<>();
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
        }
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

        map = new MapGraph(points, camelTypes, warehouseCount);
        this.roadsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < this.roadsCount; ++j) {
            int p1 = Integer.parseInt(params[i++]) - 1;
            int p2 = Integer.parseInt(params[i++]) - 1;
            map.addBidirectionalEdge(p1, p2);
        }

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

        //map.calculateEffectivePaths(warehouseCount, camelTypes);

        //map.calculatePaths(warehouseCount);

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
    }


    public void showPath(int oasisId) {
        PriorityQueue<Path> pathList = map.getPathsForOasis(oasisId);
        for (Path path : pathList) {
            System.out.println(path);
        }
    }

    public void showPaths() {
        for (int i = warehouses.size(); i < warehouses.size() + oases.size(); ++i) {
            showPath(i);
            System.out.println();
        }
    }


    /**
     * Supply all warehouses
     *
     * @param currentTime current simulation time
     */
    private void supplyWarehouses(double currentTime) {
        for (Warehouse w : warehouses) {
            w.supply(currentTime);
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
        //TODO problem when timeout == time
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
     */
    private PathCamelType getFitCamelType(Path path, int goods, double timeout) {
        double time;
        double minPossibleSpeed;
        Warehouse w = warehouses.get(path.getWarehouseId());
        for (CamelType type : camelTypes) {
            if ((w.getGoodsAmount() < Math.min(goods, type.getMaxLoad())) || (path.getMaxTransitionDistance() > type.getEffectiveDistance())) {
                continue;
            }
            minPossibleSpeed = computePathForCamelType(w, path, type, goods, timeout);
            if (minPossibleSpeed < 0 || minPossibleSpeed > type.getMaxSpeed()) {
                continue;
            }
            return new PathCamelType(type, path, minPossibleSpeed);
        }
        return null;
    }

    /**
     * Find fitting path and camel type for the request
     *
     * @param paths   all paths from the requests oasis
     * @param goods   goods to deliver
     * @param timeout time for pass the path
     * @return fitting path, fitting camel type and minimal possible speed to pass the path
     */
    private PathCamelType getRequestFirCamelType(PriorityQueue<Path> paths, int goods, double timeout) {
        PathCamelType fitCamelType;
        for (Path path : paths) {
            fitCamelType = getFitCamelType(path, goods, timeout);
            if (fitCamelType != null) {
                return fitCamelType;
            }
        }
        return null;
    }

    /**
     * Computes camel returning to the corresponding warehouse
     *
     * @param pathCamel path - camel pair
     * @param start     start time
     * @param stamina   current stamina
     */
    private void returnCamelPath(PathCamel pathCamel, double start, double stamina) {
        Camel camel = pathCamel.camel();
        Path path = pathCamel.path();
        Warehouse warehouse = warehouses.get(path.getWarehouseId());
        List<Point> points = path.getPoints();
        double time = start;
        double transitionDistance;
        double drinkTime = camel.getType().getDrinkTime();
        for (int i = points.size() - 1; i > 0; --i) {
            Point p1 = points.get(i);
            Point p2 = points.get(i - 1);
            transitionDistance = Point.getDistance(p1, p2);
            if (stamina < transitionDistance) {
                if (p1.id() < warehouses.size()) {
                    //stopped in warehouse
                    eventLogger.addEvent(
                            time,
                            String.format(
                                    "Cas: %d, Velbloud: %d, Sklad: %d, Ziznivy: %s, Pokracovani mozne v: %d",
                                    Math.round(time),
                                    camel.getId(),
                                    p1.id(),
                                    camel.getType().getName(),
                                    Math.round(time + drinkTime)
                            )
                    );
                } else {
                    //stopped in oasis
                    eventLogger.addEvent(
                            time,
                            String.format(
                                    "Cas: %d, Velbloud: %d, oasa: %d, Ziznivy: %s, Pokracovani mozne v: %d",
                                    Math.round(time),
                                    camel.getId(),
                                    p1.id(),
                                    camel.getType().getName(),
                                    Math.round(time + drinkTime)
                            )
                    );
                }

                time += drinkTime;
            }
            time += transitionDistance / camel.getSpeed();
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
     */
    private int computePathForCamel(PathCamel pathCamel, int goods, double start, double timeout) {
        Camel camel = pathCamel.camel();
        Path path = pathCamel.path();
        Warehouse warehouse = warehouses.get(path.getWarehouseId());
        List<Point> points = path.getPoints();
        double time = start;
        double stamina = camel.getDistance();
        double transitionDistance;
        double drinkTime = camel.getType().getDrinkTime();
        double loadingTime = warehouse.getLoadingTime();
        int maxLoad = camel.getType().getMaxLoad();
        int prepareGoods = Math.min(goods, maxLoad);
        double prepareTime = prepareGoods * loadingTime;

        //preparing camel
        eventLogger.addEvent(
                time,
                String.format(
                        "Cas: %d, Velbloud: %d, Sklad: %d, Nalozeno kosu: %d, Odchod v: %d",
                        Math.round(time),
                        camel.getId(),
                        warehouse.getId(),
                        prepareGoods,
                        Math.round(time + prepareTime)
                )
        );
        time += prepareTime;

        //road
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            transitionDistance = Point.getDistance(p1, p2);
            if (stamina < transitionDistance) {
                if (p1.id() < warehouses.size()) {
                    //stopped in warehouse
                    eventLogger.addEvent(
                            time,
                            String.format(
                                    "Cas: %d, Velbloud: %d, Sklad: %d, Ziznivy: %s, Pokracovani mozne v: %d",
                                    Math.round(time),
                                    camel.getId(),
                                    p1.id(),
                                    camel.getType().getName(),
                                    Math.round(time + drinkTime)
                            )
                    );
                } else {
                    //stopped in oasis
                    eventLogger.addEvent(
                            time,
                            String.format(
                                    "Cas: %d, Velbloud: %d, oasa: %d, Ziznivy: %s, Pokracovani mozne v: %d",
                                    Math.round(time),
                                    camel.getId(),
                                    p1.id(),
                                    camel.getType().getName(),
                                    Math.round(time + drinkTime)
                            )
                    );
                }
                time += drinkTime;
            } else {
                if (i != path.getWarehouseId()) {
                    //transit
                    eventLogger.addEvent(
                            time,
                            String.format(
                                    "Cas: %d, Velbloud: %d, oasa: %d, Kuk na velblouda",
                                    Math.round(time),
                                    camel.getId(),
                                    p1.id()
                            )
                    );

                }
            }
            time += transitionDistance / camel.getSpeed();
        }

        eventLogger.addEvent(
                time,
                String.format(
                        "Cas: %d, Velbloud: %d, oasa: %d, Vylozeno kosu: %d, Vylozeno v: %d, Casova rezerva: %d",
                        Math.round(time),
                        camel.getId(),
                        path.getOasisId(),
                        prepareGoods,
                        Math.round(time + prepareTime),
                        Math.round(timeout - (time + prepareTime))
                )
        );
        returnCamelPath(pathCamel, time, stamina);
        return prepareGoods;
    }

    /**
     * Find a fitting path and camel for the request
     *
     * @param request request to process
     * @return fitting path and camel
     */
    private PathCamel camelPrepare(Request request) {
        int oasisId = request.getOasisId() + warehouses.size() - 1;
        PriorityQueue<Path> paths = map.getPathsForOasis(oasisId);
        PathCamelType fitCamelType;
        Camel camel;
        Iterator<Camel> iterator;
        Warehouse w;
        fitCamelType = getRequestFirCamelType(paths, request.getGoodsCount(), request.getTimeout());
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
     */
    private boolean computeRequest(Request request, double currentTime) {
        int goods = request.getGoodsCount();
        PathCamel pathCamel;
        while (goods > 0) {
            pathCamel = camelPrepare(request);
            if (pathCamel == null) {
                return false;
            }
            goods -= computePathForCamel(pathCamel, goods, currentTime, currentTime + request.getTimeout());
        }
        return true;
    }

    /**
     * Starts the harpagon simulation
     */
    public void simulate() {
        while (!requests.isEmpty()) {
            List<Request> actualRequests = getActualRequests();
            double currentTime = actualRequests.get(0).getTime();
            returnCamels(currentTime);
            eventLogger.log(currentTime);
            supplyWarehouses(currentTime);
            for (Request request : actualRequests) {
                if (!computeRequest(request, currentTime)) {
                    logger.log(
                            String.format(
                                    "Cas: %d, Oaza: %d, Vsichni vymreli, Harpagon zkrachoval, Konec simulace",
                                    Math.round(currentTime),
                                    request.getOasisId()
                            ),
                            LogType.ERROR
                    );
                    break;
                }
            }
        }
        returnCamels(Double.POSITIVE_INFINITY);
        eventLogger.logAll();
        countCamels();
        countCamelsByType();
        simulationEnd();
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

                eventLogger.addEvent(
                        camel.getReturnTime(),
                        String.format(
                                "Cas: %d, Velbloud: %d, Navrat do skladu: %d",
                                Math.round(camel.getReturnTime()),
                                camel.getId(),
                                camel.getWarehouseId()
                        ));
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
        double elapsed = (System.currentTimeMillis() - startTime) / 1e3;
        logger.log(
                String.format("Simulation ended in %f seconds", elapsed),
                HEADER
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
