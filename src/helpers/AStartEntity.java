package helpers;

public class AStartEntity implements Comparable<AStartEntity>{
    public Point point;

    public int priority;

    public AStartEntity(Point point, int priority) {
        this.point = point;
        this.priority = priority;
    }

    @Override
    public int compareTo(AStartEntity o) {
        return o.priority - this.priority;
    }
}
