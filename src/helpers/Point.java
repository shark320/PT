package helpers;

/**
 * Class encapsulates X and Y map coordinates
 * Can not be changed (read only)
 *
 * @author vpavlov
 */
public class Point {

    /**
     * X-coordinate
     */
    private final int x;

    /**
     * Y-coordinate
     */
    private final int y;


    /**
     * Constructor
     *
     * @param x - X-coordinate
     * @param y - Y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate distance between two points
     *
     * @param p1 - first point
     * @param p2 - second point
     * @return distance between points
     */
    public static double getDistance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    /**
     * X-coordinate getter
     *
     * @return X-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Y-coordinate getter
     *
     * @return Y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Check if two points are equal (the same X and Y coordinates)
     *
     * @param p - point to check
     * @return true if points are equal, else - false
     */
    public boolean isEqual(Point p) {
        return x == p.x && y == p.y;
    }

    @Override
    public String toString() {
        return "Point:[" + x + " ," + y + "]";
    }
}
