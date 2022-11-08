package com.vpavlov.map;

/**
 * Class encapsulates X and Y map coordinates
 * Can not be changed (read only)
 *
 * @param x  X-coordinate
 * @param y  Y-coordinate
 * @param id Oasis or warehouse id where this point represents location
 * @author vpavlov
 */
public record Point(double x, double y, int id) {

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
    @Override
    public double x() {
        return x;
    }

    /**
     * Y-coordinate getter
     *
     * @return Y-coordinate
     */
    @Override
    public double y() {
        return y;
    }

    /**
     * Id getter
     *
     * @return Oasis or warehouse id where this point represents location
     */
    @Override
    public int id() {
        return id;
    }


    /**
     * Check if two points are equal (the same X and Y coordinates)
     *
     * @param o - point to check
     * @return true if points are equal, else - false
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Point p) {
            return Double.compare(x, p.x) == 0 && Double.compare(y, p.y) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Point:[" + x + " ," + y + "]";
    }
}
