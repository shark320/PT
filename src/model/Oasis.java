package model;

import helpers.Point;

/**
 * Class represents oasis
 *
 * @author vpavlov
 */
public class Oasis {

    /**
     * Oasis location {x , y}
     */
    private final Point location;

    /**
     * Constructor
     *
     * @param x - X-coordinate
     * @param y - Y-coordinate
     */
    public Oasis(int x, int y) {
        location = new Point(x, y);
    }

    /**
     * Location getter
     *
     * @return oasis location
     */
    public Point getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "<OASIS>[x=" + this.location.getX() + ", y=" + this.location.getY() + "]";
    }
}
