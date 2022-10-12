package helpers;


/**
 * Class-container for Point for priority queue
 *
 * @author vpavlov
 */
public class AStartEntity implements Comparable<AStartEntity> {

    /**
     * Map point to save
     */
    public Point point;

    /**
     * Priority of the entity
     */
    public int priority;

    /**
     * Constructor
     *
     * @param point    - point to save
     * @param priority - priority of the entity
     */
    public AStartEntity(Point point, int priority) {
        this.point = point;
        this.priority = priority;
    }

    @Override
    public int compareTo(AStartEntity o) {
        return o.priority - this.priority;
    }
}
