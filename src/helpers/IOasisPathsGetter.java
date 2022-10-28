package helpers;

import java.util.PriorityQueue;

/**
 * Oasis paths getter interface
 *
 * @author vpavlov
 */
public interface IOasisPathsGetter {

    /**
     * If oasis has paths to the next warehouse
     *
     * @return true if it has, else false
     */
    boolean hasNext();

    /**
     * Get paths to the next warehouse
     *
     * @return paths to the next warehouse
     */
    PriorityQueue<Path> getNextPaths();
}
