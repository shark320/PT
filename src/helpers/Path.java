package helpers;

import java.util.*;


public class Path {

    Set<Integer> indexes = new LinkedHashSet<>();

    public Path() {
    }

    public void add(Integer index) {
        indexes.add(index);
    }

    public Set<Integer> getPath(){
        return indexes;
    }

    @Override
    public String toString() {
        return "Path: " + indexes.toString();
    }
}
