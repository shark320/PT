package helpers;

import java.util.HashSet;
import java.util.Set;

/**
 * Map graph entity (vertex)
 *
 * @author vpavlov
 */
public class GraphEntity {

    /**
     * Vertex coordinates
     */
    public final Point point;

    /**
     * List of neighbours (if there are no neighbours, then it's null)
     */
    public Set<Integer> neighbours = null;

    /**
     * Constructor
     *
     * @param point -vertex coordinates
     */
    public GraphEntity(Point point) {
        this.point = point;
    }

    /**
     * Adding new neighbour to the vertex
     *
     * @param index -index of neighbour in the graph
     */
    public void addNeighbour(int index) {
        if (neighbours == null) {
            neighbours = new HashSet<>();
        }
        neighbours.add(index);
    }
}
