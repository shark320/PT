package model;

import helpers.*;

import java.util.*;

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
     * List of camels
     */
    private final List<CamelType> camelTypes = new LinkedList<>();

    /**
     * List of requests
     */
    private final List<Request> requests = new LinkedList<>();

    private final MapGraph map;

    /**
     * Count of roads
     */
    private final int roadsCount;

    /**
     * Constructor
     *
     * @param params -parameters of the simulation
     */
    public Simulation(String[] params) {
        int i = 0;
        int warehouseCount = Integer.parseInt(params[i++]);
        List<Point> points = new ArrayList<>();
        for (int j = 0; j < warehouseCount; ++j) {
            Warehouse w = new Warehouse(
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++])
            );
            points.add(w.getLocation());
            warehouses.add(w);
        }
        int oasesCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < oasesCount; ++j) {
            Oasis o = new Oasis(
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++])
            );
            points.add(o.getLocation());
            oases.add(o);
        }

        map = new MapGraph(points);

        this.roadsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < this.roadsCount; ++j) {
            int x = Integer.parseInt(params[i++]) - 1;
            int y = Integer.parseInt(params[i++]) - 1;
            map.addBidirectionalEdge(x, y);
        }

        map.calculatePaths(warehouseCount);

        int camelsCount = Integer.parseInt(params[i++]);

        for (int j = 0; j < camelsCount; ++j) {
            CamelType c = new CamelType(
                    params[i++],
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Double.parseDouble(params[i++])
            );
            camelTypes.add(c);
        }

        int requestsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < requestsCount; j++) {
            Request r = new Request(
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++])
            );
            requests.add(r);
        }
    }

    /**
     * Heuristic function to determine point priority in A* algorithm
     *
     * @param first  -first point
     * @param second -second point
     * @return Manhattan distance between points
     */
    private int heuristic(Point first, Point second) {
        if (first == null || second == null) {
            throw new NullPointerException("two points must not be null");
        }
        return Math.abs(first.getX() - second.getX()) + Math.abs(first.getY() - second.getY());
    }


    public void showPath(int i, int j) {
        System.out.println(map.findPath(i, j));
    }

    public void showPaths(){
        for (List<Path> pathList : map.getPaths()){
            for (Path p:pathList) {
                System.out.println(p);
            }
        }
    }


    public void generateCamel() {
        for (int i = 0; i < 10; ++i) {
            Camel camel = new Camel(camelTypes.get(0));
            System.out.println(camel);
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
        str.append(ConsoleColor.ANSI_YELLOW).append("[i]<-->[j] {i,j} - indexes of oasis or warehouse\n[0,S] - warehouses, [S+1, S+O] - oases\nS - number of warehouses, O - number of oases\n\n").append(ConsoleColor.ANSI_RESET);
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
        str.append(ConsoleColor.ANSI_YELLOW).append("{type} - camel type\n{vmin} - minimal speed\n{vmax} - maximal speed\n{dmin} - minimal distance\n{dmax} - maximal distance\n{td} - time to drink\n{kd} - maximal load\n{pd} - herd proportion   \n\n").append(ConsoleColor.ANSI_RESET);
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
