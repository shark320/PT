package model;

public class Oasis {

    private final int x;

    private final int y;

    public Oasis(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "<OASIS>[x="+this.x+", y="+this.y+"]";
    }
}
