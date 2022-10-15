package helpers;

import java.util.*;

public class MapGraph {

    private final List<GraphEntity> graph;

    private final Map<Integer,List<Path>> paths = new HashMap<>();

    public MapGraph(List<Point> points) {
        graph = new ArrayList<>(points.size());
        for (Point p : points) {
            graph.add(new GraphEntity(p));
        }
    }

    public void addBidirectionalEdge(int first, int second){
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
    private int heuristic(Point first, Point second) {
        if (first == null || second == null) {
            throw new NullPointerException("two points must not be null");
        }
        return Math.abs(first.getX() - second.getX()) + Math.abs(first.getY() - second.getY());
    }

    /**
     * Search the shortest path between two points
     *
     * @param start -start point
     * @param end   -end point
     * @return Linked Set of points that encapsulates path (end -> start). If there is no path, null.
     */
    public List<Point> findPath(int start, int end) {
        PriorityQueue<AStartEntity> queue = new PriorityQueue<>();
        queue.add(new AStartEntity(start, 0));
        List<Point> path = new LinkedList<>();
        Map<Integer, Integer> previous = new HashMap<>();
        previous.put(start, null);

        while (!queue.isEmpty()) {
            int current = queue.poll().pointId;

            if (current == end) {
                break;
            }

            Set<Integer> neighbours = graph.get(current).neighbours;
            if (neighbours != null) {
                for (int next : neighbours) {
                    if (!previous.containsKey(next)) {
                        int priority = heuristic(graph.get(end).point, graph.get(next).point);
                        queue.add(new AStartEntity(next, priority));
                        previous.put(next, current);
                    }
                }
            }

        }

        if (previous.containsKey(end)) {

            Integer p = end;
            path.add(graph.get(p).point);
            while ((p = previous.get(p)) != null) {
                path.add(graph.get(p).point);
            }
        }else{
            return null;
        }

        return path;
    }

    public List<GraphEntity> getGraph(){
        return graph;
    }

    public void calculatePaths(int warehouseCount){
        for (int i=warehouseCount; i<graph.size(); ++i){
            for (int j=0;j<warehouseCount; ++j) {
                List<Point> path = findPath(i,j);
                if (path != null) {
                    List<Path> pathList = paths.get(i);
                    if (pathList == null) {
                        pathList = new ArrayList<>();
                        pathList.add(new Path(path,i,j));
                        paths.put(i, pathList);
                    }else{
                        pathList.add(new Path(path,i,j));
                    }

                }
            }
        }
    }

    public List<Path> getPathsForOasis(int oasisId){
        return paths.get(oasisId);
    }

    public Collection<List<Path>> getPaths() {
        return paths.values();
    }
}