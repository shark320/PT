package helpers;

import model.CamelType;

import java.util.*;

/**
 * Simulation map represented as a graph
 *
 * @author vpavlov
 */
public class MapGraph {

    /**
     * Graph structure represented by neighbours list
     */
    private final List<GraphEntity> graph;

    /**
     * The shortest path between path --> oasis
     */
    private final Map<Integer, List<Path>> paths = new HashMap<>();

    private final int warehouseCount;

    private final PriorityQueue<CamelType> camelTypes;

    /**
     * Constructor
     *
     * @param points -graph vertexes
     */
    public MapGraph(List<Point> points,PriorityQueue<CamelType> camelTypes, int warehouseCount) {
        graph = new ArrayList<>(points.size());
        for (Point p : points) {
            graph.add(new GraphEntity(p));
        }
        this.camelTypes = camelTypes;
        this.warehouseCount = warehouseCount;
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
        return Math.abs(first.getX() - second.getX()) + Math.abs(first.getY() - second.getY());
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

    /**
     * Calculate the shortest paths between all path --> oasis
     *
     * @param warehouseCount - count of path
     */
    public void calculatePaths(int warehouseCount) {
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
    }

    private List<Path> addPathsForOasis(int oasisId, List<Path> pathList, Set<Path> toAdd) {
        if (!toAdd.isEmpty()) {
            if (pathList == null) {
                pathList = new ArrayList<>(toAdd);
                paths.put(oasisId, pathList);
            } else {
                pathList.addAll(toAdd);
            }

        }
        return pathList;
    }

    /**
     * Calculate the shortest and effective paths between all path --> oasis
     *
     * @param warehouseCount - count of path
     */
    public void calculateEffectivePaths(int warehouseCount, PriorityQueue<CamelType> camelTypes) {
        for (int i = warehouseCount; i < graph.size(); ++i) {
            List<Path> pathList = paths.get(i);
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
            System.out.printf("[DEBUG %.2f%%] calculating path  for oasis %d  elapsed: %d ms\n",(i-warehouseCount)/(double)(graph.size()-warehouseCount)*100,i,elapsed);
        }
    }

    private List<Path> calculatePathsForOasis(int oasisId){
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
    }


    /**
     * Oasis paths getter
     *
     * @param oasisId -oasisId to get paths for
     * @return all the shortest paths between the given oasis and warehouses
     */
    public PriorityQueue<Path> getPathsForOasis(int oasisId) {
        List<Path> result = paths.get(oasisId);
        if (result == null){
            result = calculatePathsForOasis(oasisId);
        }
        return new PriorityQueue<>(result);
        //return paths.get(oasisId);
    }

    /**
     * All the shortest paths getter
     *
     * @return all the shortest paths between warehouses and oasis
     */
    public Collection<List<Path>> getPaths() {
        return paths.values();
    }
}
