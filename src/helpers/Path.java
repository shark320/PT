package helpers;

import java.util.*;

/**
 * Path from warehouse to oasis
 */
public class Path {

    /**
     * Path from warehouse to oasis
     */
    private final List<Point> points;

    /**
     * Path distance
     */
    private final double distance;

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
        this.points = points;
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
        for (int i = 0; i < points.size() - 1; ++i) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            distance += Point.getDistance(p1, p2);
        }

        return distance;
    }

    @Override
    public String toString() {
        return "Path: "+(warehouse_id+1)+" --> "+(oasis_id+1) +" "+points;
    }
}
