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
    public int pointId;

    /**
     * Priority of the entity
     */
    public int priority;

    /**
     * Constructor
     *
     * @param pointId    - point to save
     * @param priority - priority of the entity
     */
    public AStartEntity(int pointId, int priority) {
        this.pointId = pointId;
        this.priority = priority;
    }

    @Override
    public int compareTo(AStartEntity o) {
        return o.priority - this.priority;
    }
}
