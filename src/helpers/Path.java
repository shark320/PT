package helpers;

import java.util.*;

/**
 * Path from warehouse to oasis
 */
public class Path implements Comparable<Path> {

    /**
     * Path from warehouse to oasis
     */
    private final List<Point> points;

    /**
     * Path distance
     */
    private final double distance;

    private double maxTransitionDistance = -1;

    /**
     * Oasis id (destination point)
     */
    private final int oasis_id;

    /**
     * Warehouse id (starting point)
     */
    private final int warehouse_id;

    /**
     * Constructor
     *
     * @param points       - list of path points (in order warehouse --> oasis)
     * @param oasis_id     - id of oasis (destination)
     * @param warehouse_id - id of warehouse (starting point)
     */
    public Path(List<Point> points, int oasis_id, int warehouse_id) {
        this.points = new ArrayList<>(points);
        this.oasis_id = oasis_id;
        this.warehouse_id = warehouse_id;
        this.distance = calculateDistance();
    }

    /**
     * Calculate path distance
     *
     * @return path distance
     */
    private double calculateDistance() {
        double distance = 0;
        double transition;
        for (int i = 0; i < points.size() - 1; ++i) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            transition =Point.getDistance(p1, p2);
            distance += transition;
            maxTransitionDistance  = Math.max(maxTransitionDistance, transition);
        }

        return distance;
    }

    @Override
    public String toString() {
        return String.format("Path [%f  |  %f]: (%d --> %d) %s", distance,maxTransitionDistance,warehouse_id + 1,oasis_id + 1,points);
        //return "Path []: " + (warehouse_id + 1) + " --> " + (oasis_id + 1) + " " + points;
    }

    @Override
    public int compareTo(Path o) {
        double diff = this.distance - o.distance;
        if (diff == 0.0) return 0;
        return diff < 0 ? -1 : 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Path) {
            return this.points.equals(((Path)obj).points) && this.oasis_id == ((Path)obj).oasis_id && this.warehouse_id == ((Path)obj).warehouse_id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return points.hashCode();
    }
}
