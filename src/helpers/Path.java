package helpers;

import java.util.*;

/**
 * Path from warehouse to oasis
 *
 * @author vpavlov
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

    /**
     * Maximal transition distance between two points in the path
     */
    private double maxTransitionDistance = -1;

    /**
     * Oasis id (destination point)
     */
    private final int oasisId;

    /**
     * Warehouse id (starting point)
     */
    private final int warehouseId;

    /**
     * Constructor
     *
     * @param points      - list of path points (in order warehouse --> oasis)
     * @param oasisId     - id of oasis (destination)
     * @param warehouseId - id of warehouse (starting point)
     */
    public Path(List<Point> points, int oasisId, int warehouseId) {
        this.points = new ArrayList<>(points);
        this.oasisId = oasisId;
        this.warehouseId = warehouseId;
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
            transition = Point.getDistance(p1, p2);
            distance += transition;
            maxTransitionDistance = Math.max(maxTransitionDistance, transition);
        }

        return distance;
    }

    /**
     * Warehouse id getter
     *
     * @return warehouse id
     */
    public int getWarehouseId() {
        return warehouseId;
    }

    /**
     * Oasis id getter
     *
     * @return return oasis id
     */
    public int getOasisId() {
        return oasisId;
    }

    /**
     * Maximal transition distance getter
     *
     * @return maximal transition distance
     */
    public double getMaxTransitionDistance() {
        return maxTransitionDistance;
    }

    /**
     * Path points getter (from warehouse to oasis)<br>
     * Shallow copying
     *
     * @return shallow copy of path points
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Path total distance getter
     *
     * @return path total distance
     */
    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return String.format("Path [%f  |  %f]: (%d --> %d) %s", distance, maxTransitionDistance, warehouseId + 1, oasisId + 1, points);
        //return "Path []: " + (warehouse_id + 1) + " --> " + (oasis_id + 1) + " " + points;
    }

    @Override
    public int compareTo(Path o) {
        return Double.compare(distance, o.distance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Path) {
            return this.points.equals(((Path) obj).points) && this.oasisId == ((Path) obj).oasisId && this.warehouseId == ((Path) obj).warehouseId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return points.hashCode();
    }
}
