package helpers;

import model.CamelType;
import model.Warehouse;

import java.util.*;

/**
 * Simulation map represented as a graph
 *
 * @author vpavlov
 */
public class MapGraph {

    /**
     * Inner class to get paths for oasis
     *
     * @author vpavlov
     */
    private class OasisPathsGetter implements IOasisPathsGetter {

        /**
         * Current warehouse index (from the sorted list)
         */
        private int currentWarehouse = 0;

        /**
         * Oasis id
         */
        private final int oasisId;

        /**
         * Path for the oasis
         */
        private HashMap<Warehouse, PriorityQueue<Path>> oasisPaths;

        /**
         * Constructor
         *
         * @param oasisId oasis id to het path for
         * @throws IllegalArgumentException if oasis id is out of bounds
         */
        OasisPathsGetter(int oasisId) throws IllegalArgumentException {
            //System.out.printf("[DEBUG] PathGetter: oasisId=%d\n", oasisId);
            if (oasisId < warehouses.size()) {
                throw new IllegalArgumentException(String.format("Illegal oasis id %d. Oasis id should be in [%d,%d]", oasisId, warehouseCount, graph.size() - 1));
            }
            warehouses.sort(null);
            this.oasisId = oasisId;
            oasisPaths = paths.get(oasisId);
            if (oasisPaths == null) {
                oasisPaths = new HashMap<>();
                paths.put(oasisId, oasisPaths);
            }
        }

        @Override
        public boolean hasNext() {
            return currentWarehouse < warehouses.size();
        }

        @Override
        public PriorityQueue<Path> getNextPaths() {
            if (warehouseCount <= currentWarehouse) {
                throw new NoSuchElementException("Oasis has no next paths");
            }
            Warehouse warehouse = warehouses.get(currentWarehouse);

            //System.out.printf("[DEBUG] Warehouse %d goods=%d priority=%.2f\n",warehouse.getId(), warehouse.getGoodsAmount(), warehouse.getPriority());
            PriorityQueue<Path> pathsQueue = oasisPaths.get(warehouse);
            if (pathsQueue == null) {
                pathsQueue = findEffectivePaths(oasisId, warehouse.getId());
//                if (warehouse.getId()==2){
//                    System.out.println("TEST");
//                    System.out.println(warehouseCount);
//                    System.out.println(oasisId);
//                }
                //System.out.printf("[DEBUG] oasisId=%d\n %s\n",oasisId,pathsQueue);
                oasisPaths.put(warehouse, pathsQueue);
            }
            ++currentWarehouse;
            return pathsQueue;
        }


    }

    /**
     * Graph structure represented by neighbours list
     */
    private final List<GraphEntity> graph;

    /**
     * The shortest path between path --> oasis
     */
    private final Map<Integer, HashMap<Warehouse, PriorityQueue<Path>>> paths = new HashMap<>();

    /**
     * List of warehouses
     */
    private final List<Warehouse> warehouses;

    /**
     * Warehouses count
     */
    private final int warehouseCount;

    /**
     * Camel types
     */
    private final PriorityQueue<CamelType> camelTypes;

    /**
     * Constructor
     *
     * @param points -graph vertexes
     */
    public MapGraph(List<Point> points, PriorityQueue<CamelType> camelTypes, List<Warehouse> warehouses) {
            graph = new ArrayList<>(points.size());
            for (Point p : points) {
                graph.add(new GraphEntity(p));
            }
            this.camelTypes = camelTypes;
            this.warehouses = new ArrayList<>(warehouses);
            this.warehouseCount = warehouses.size();
            initPaths();
    }

    /**
     * Initializes paths map with warehouses and null paths sets
     */
    private void initPaths() {
//        HashMap<Warehouse, PriorityQueue<Path>> oasisPaths;
        for (int i = warehouseCount; i < graph.size(); i++) {
//            oasisPaths = new HashMap<>();
//            for (Warehouse w : warehouses) {
//                oasisPaths.put(w, null);
//            }
            paths.put(i, null);
        }
    }

    /**
     * OasisPathsGetter getter
     *
     * @param oasisId oasis id
     * @return OasisPathsGetter for the specified oasis
     */
    public OasisPathsGetter getPathsForOasis(int oasisId) {
        return new OasisPathsGetter(oasisId);
    }

    /**
     * Find all effective paths from start point to end point according to camel types
     *
     * @param start start path point
     * @param end   end path point
     * @return priority queue with paths
     */
    private PriorityQueue<Path> findEffectivePaths(int start, int end) {
        Set<Path> oasisPaths = new HashSet<>();
        for (CamelType camelType : camelTypes) {
            List<Point> path = findEffectivePath(start, end, camelType);
            if (path != null) {
                Path p = new Path(path, start, end);
                oasisPaths.add(p);
            }
        }

        return new PriorityQueue<>(oasisPaths);
    }


    /**
     * Creates new bidirectional edge between two vertexes
     *
     * @param first  -first vertex
     * @param second -second vertex
     */
    public void addBidirectionalEdge(int first, int second) {
        GraphEntity firstVertex = graph.get(first);
        GraphEntity secondVertex = graph.get(second);
        firstVertex.addNeighbour(second);
        secondVertex.addNeighbour(first);
    }

    /**
     * Heuristic function to determine point priority in A* algorithm
     *
     * @param first  -first point
     * @param second -second point
     * @return Manhattan distance between points
     */
    private double heuristic(Point first, Point second) {
        if (first == null || second == null) {
            throw new NullPointerException("two points must not be null");
        }
        return Math.abs(first.x() - second.x()) + Math.abs(first.y() - second.y());
    }

    /**
     * Search the shortest path between two points using A* algorithm
     *
     * @param start -start point
     * @param end   -end point
     * @return Linked Set of points that encapsulates path (end -> start). If there is no path, null.
     */
    public List<Point> findPath(int start, int end) {
        PriorityQueue<AStartEntity> queue = new PriorityQueue<>();
        queue.add(new AStartEntity(start, 0));
        Map<Integer, Integer> previous = new HashMap<>();
        previous.put(start, null);

        calculatePath(end, previous, queue, null);

        return generatePathPoints(end, previous);
    }


    /**
     * Helper method <br>
     * Checks if specified camel camelType can pass the distance between two points <br>
     * Checks does not occur if camel camelType is null
     *
     * @param point1    first point
     * @param point2    second point
     * @param camelType camel camelType to check
     * @return true if this camel camelType can pass the distance between two points, else false
     */
    private boolean checkDistance(int point1, int point2, CamelType camelType) {
        if (camelType == null) {
            return true;
        }
        Point p1 = graph.get(point1).point;
        Point p2 = graph.get(point2).point;
        double distance = Point.getDistance(p1, p2);
        return distance < camelType.getEffectiveDistance();
    }

    /**
     * Helper method <br>
     * Calculates the shortest paths between previous points and the end, that can be pass by camel camelType
     *
     * @param end       path target
     * @param previous  previous points in the path
     * @param queue     points queue to check
     * @param camelType camel camelType to check the path. If camelType is null, then check will not occur
     */

    private void calculatePath(int end, Map<Integer, Integer> previous, PriorityQueue<AStartEntity> queue, CamelType camelType) {
        while (!queue.isEmpty()) {
            int current = queue.poll().pointId;

            if (current == end) {
                break;
            }

            Set<Integer> neighbours = graph.get(current).neighbours;
            if (neighbours != null) {
                for (int next : neighbours) {
                    if (!previous.containsKey(next)) {
                        if (checkDistance(current, next, camelType)) {
                            double priority = heuristic(graph.get(end).point, graph.get(next).point);
                            queue.add(new AStartEntity(next, priority));
                            previous.put(next, current);
                        }
                    }
                }
            }

        }
    }

    /**
     * Helper method <br>
     * Generates a list of points from the path map
     *
     * @param end      path target
     * @param previous path map
     * @return list of points represents the path
     */
    private List<Point> generatePathPoints(int end, Map<Integer, Integer> previous) {
        List<Point> path = new LinkedList<>();
        if (previous.containsKey(end)) {
            Integer p = end;
            path.add(graph.get(p).point);
            while ((p = previous.get(p)) != null) {
                path.add(graph.get(p).point);
            }
        } else {
            return null;
        }

        return path;
    }

    /**
     * Searches the shortest path between two points using A* algorithm <br>
     * Checks if the specified camel camelType can pass this path
     *
     * @param start     start point
     * @param end       end point
     * @param camelType camel camelType to check
     * @return Linked Set of points that encapsulates path (end -> start). If there is no path, null.
     */
    public List<Point> findEffectivePath(int start, int end, CamelType camelType) {
        PriorityQueue<AStartEntity> queue = new PriorityQueue<>();
        queue.add(new AStartEntity(start, 0));

        Map<Integer, Integer> previous = new HashMap<>();
        previous.put(start, null);

        calculatePath(end, previous, queue, camelType);

        return generatePathPoints(end, previous);
    }

    /**
     * Graph structure getter
     *
     * @return graph structure
     */
    public List<GraphEntity> getGraph() {
        return graph;
    }

    public void printForOasis(int id){
        System.out.println(warehouseCount);
        System.out.println(id);
        System.out.println(graph.get(id+warehouseCount).neighbours);
        System.out.println(findEffectivePaths(id+warehouseCount,0));
        Point p1 = graph.get(id+warehouseCount).point;
        Point p2 = graph.get(0).point;
        System.out.printf("[DEBUG] Distance between %d and %d  =  %f",0, id+warehouseCount, Point.getDistance(p1, p2));
        System.out.println("Camel types:");
        for (CamelType ct : camelTypes){
            System.out.println(ct);
        }
    }

    /*
     * Calculate the shortest paths between all path --> oasis
     *
     * @param warehouseCount - count of path
     */
/*    public void calculatePaths(int warehouseCount) {
        for (int i = warehouseCount; i < graph.size(); ++i) {
            for (int j = 0; j < warehouseCount; ++j) {
                List<Point> path = findPath(i, j);
                if (path != null) {
                    List<Path> pathList = paths.get(i);
                    if (pathList == null) {
                        pathList = new ArrayList<>();
                        pathList.add(new Path(path, i, j));
                        paths.put(i, pathList);
                    } else {
                        pathList.add(new Path(path, i, j));
                    }

                }
            }
        }
    }*/

/*    private List<Path> addPathsForOasis(int oasisId, Set<Path> pathList, Set<Path> toAdd) {
        if (!toAdd.isEmpty()) {
            if (pathList == null) {
                pathList = new ArrayList<>(toAdd);
                paths.put(oasisId, pathList);
            } else {
                pathList.addAll(toAdd);
            }

        }
        return pathList;
    }*/

    /*
     * Calculate the shortest and effective paths between all warehouse --> oasis
     */
/*    public void calculateEffectivePaths() {
        for (int i = warehouseCount; i < graph.size(); ++i) {
            Set<Path> pathList = paths.get(i);
            long start = System.currentTimeMillis();
            for (int j = 0; j < warehouseCount; ++j) {
                Set<Path> oasisPaths = new HashSet<>();
                for (CamelType camelType : camelTypes) {
                    List<Point> path = findEffectivePath(i, j, camelType);
                    if (path != null) {
                        Path p = new Path(path, i, j);
                        oasisPaths.add(p);
                    }
                }

                pathList = addPathsForOasis(i, pathList, oasisPaths);
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("[DEBUG %.2f%%] calculating path  for oasis %d  elapsed: %d ms\n", (i - warehouseCount) / (double) (graph.size() - warehouseCount) * 100, i, elapsed);
        }
    }*/

/*    private List<Path> calculatePathsForOasis(int oasisId) {
        List<Path> pathList = paths.get(oasisId);
        for (int j = 0; j < warehouseCount; ++j) {
            Set<Path> oasisPaths = new HashSet<>();
            for (CamelType camelType : camelTypes) {
                List<Point> path = findEffectivePath(oasisId, j, camelType);
                if (path != null) {
                    Path p = new Path(path, oasisId, j);
                    oasisPaths.add(p);
                }
            }

            pathList = addPathsForOasis(oasisId, pathList, oasisPaths);
        }

        return pathList;
    }*/


    /*
     * Oasis paths getter
     *
     * @param oasisId -oasisId to get paths for
     * @return all the shortest paths between the given oasis and warehouses
     */
/*    public PriorityQueue<Path> getPathsForOasis(int oasisId) {
        List<Path> result = paths.get(oasisId);
        if (result == null){
            result = calculatePathsForOasis(oasisId);
        }
        return new PriorityQueue<>(result);
        //return paths.get(oasisId);
    }*/

    /*
     * All the shortest paths getter
     *
     * @return all the shortest paths between warehouses and oasis
     */
    /*public Collection<List<Path>> getPaths() {
        return paths.values();
    }*/
}
