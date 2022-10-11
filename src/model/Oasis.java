package model;

import helpers.Point;

public class Oasis {

    private final Point location;

    public Oasis(int x, int y) {
        location = new Point(x, y);
    }

    public Point getLocation(){
        return location;
    }

    @Override
    public String toString() {
        return "<OASIS>[x="+this.location.getX()+", y="+this.location.getY()+"]";
    }
}
