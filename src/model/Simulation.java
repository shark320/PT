package model;

import helpers.AStartEntity;
import helpers.ConsoleColor;
import helpers.Point;

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
    private final List<Camel> camels = new LinkedList<>();

    /**
     * List of map points (means warehouses and oases)
     * Point id is equal to the corresponding oasis or warehouse
     */
    private final List<Point> points = new ArrayList<>();

    /**
     * List of requests
     */
    private final List<Request> requests = new LinkedList<>();

    /**
     * Road map warehouses <--> oases and oases <--> oases
     */
    private boolean[][] roads;

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
        roads = new boolean[warehouseCount + oasesCount][oasesCount + warehouseCount];
        this.roadsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < this.roadsCount; ++j) {
            int x = Integer.parseInt(params[i++]) - 1;
            int y = Integer.parseInt(params[i++]) - 1;
            roads[x][y] = true;
            roads[y][x] = true;
        }

        int camelsCount = Integer.parseInt(params[i++]);

        for (int j = 0; j < camelsCount; ++j) {
            Camel c = new Camel(
                    params[i++],
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Double.parseDouble(params[i++])
            );
            camels.add(c);
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

    /**
     * Searches all neighbours to the point
     *
     * @param p -point to search for
     * @return list of neighbours (points)
     */
    private List<Point> getNeighbours(Point p) {
        int id = points.indexOf(p);
        ArrayList<Point> neighbours = new ArrayList<Point>(roads[id].length);
        for (int i = 0; i < roads[id].length; i++) {
            if (roads[id][i]) {
                neighbours.add(points.get(i));
            }
        }

        return neighbours;
    }


    public void showPath(int i, int j) {
        Point start = points.get(i);
        Point end = points.get(j);
        System.out.println(findPath(start, end));
    }

    /**
     * Search the shortest path between two points
     *
     * @param start -start point
     * @param end   -end point
     * @return Linked Set of points that encapsulates path (end -> start). If there is no path, null.
     */
    private LinkedHashSet<Point> findPath(Point start, Point end) {
        PriorityQueue<AStartEntity> queue = new PriorityQueue<>();
        queue.add(new AStartEntity(start, 0));
        LinkedHashSet<Point> path = new LinkedHashSet<>();
        Map<Point, Point> previous = new HashMap<>();
        previous.put(start, null);

        while (!queue.isEmpty()) {
            Point current = queue.poll().point;

            if (current.isEqual(end)) {
                break;
            }

            for (Point next : getNeighbours(current)) {
                if (!previous.containsKey(next)) {
                    int priority = heuristic(end, next);
                    queue.add(new AStartEntity(next, priority));
                    previous.put(next, current);
                }
            }

        }

        if (previous.containsKey(end)) {

            Point p = end;
            path.add(p);
            while ((p = previous.get(p)) != null) {
                path.add(p);
            }
        }else{
            return null;
        }

        return path;
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
        for (int i = 0; i < roads.length; ++i) {
            for (int j = 0; j < roads[i].length; ++j) {
                if (roads[i][j]) {
                    str.append("[").append(++road).append("] ").append(i + 1).append("<-->").append(j + 1).append("\n");
                }
            }
        }

        str.append(ConsoleColor.ANSI_GREEN).append("\n-------------------------------->>CAMELS [").append(camels.size()).append("]<<--------------------------------\n\n").append(ConsoleColor.ANSI_RESET);
        str.append(ConsoleColor.ANSI_YELLOW).append("{tz} - request arrival time\n{tp} - request timeout\n{op} - oasis index to deliver\n{kp} - goods count\n\n").append(ConsoleColor.ANSI_RESET);
        for (Camel camel : camels) {
            str.append(camel.toString()).append("\n");
        }

        str.append(ConsoleColor.ANSI_GREEN).append("\n-------------------------------->>REQUESTS [").append(requests.size()).append("]<<--------------------------------\n\n").append(ConsoleColor.ANSI_RESET);
        str.append(ConsoleColor.ANSI_YELLOW).append("{tz} - request arrival time\n{tp} - request timeout\n{op} - oasis index to deliver\n{kp} - goods count\n\n").append(ConsoleColor.ANSI_RESET);
        for (Request request : requests) {
            str.append(request.toString()).append("\n");
        }

        return str.toString();
    }
}
