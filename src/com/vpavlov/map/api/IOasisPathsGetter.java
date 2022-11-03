package com.vpavlov.map.api;

import com.vpavlov.map.Path;

import java.util.NoSuchElementException;
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
     * @throws NoSuchElementException if there is no next paths
     */
    PriorityQueue<Path> getNextPaths() throws NoSuchElementException;
}
