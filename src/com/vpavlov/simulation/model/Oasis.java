package com.vpavlov.simulation.model;

import com.vpavlov.map.Point;

/**
 * Class represents oasis
 *
 * @author vpavlov
 */
public class Oasis {

    private final int id;
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
    public Oasis(int id, double x, double y) {
        this.id = id;
        location = new Point(x, y, this.id);
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
        return "<OASIS ["+id+"]>[x=" + this.location.x() + ", y=" + this.location.y() + "]";
    }
}
