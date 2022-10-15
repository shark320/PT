package helpers;

import java.util.HashSet;
import java.util.Set;

public class GraphEntity {

    public Point point;

    public Set<Integer> neighbours = null;

    public GraphEntity(Point point) {
        this.point = point;
    }

    public void addNeighbour(int index){
        if (neighbours == null){
            neighbours = new HashSet<>();
        }
        neighbours.add(index);
    }
}
