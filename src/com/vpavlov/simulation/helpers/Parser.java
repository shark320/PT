package com.vpavlov.simulation.helpers;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Abstract file parser class
 *
 * @author vpavlov
 */
public abstract class Parser {

    /**
     * Reading the file
     *
     * @param file -file to read
     * @return file content
     */
    private static String getData(File file) {
        StringBuilder out = new StringBuilder();
        Stream<String> lines;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            lines = input.lines();
            List<String> list = lines.toList();
            for (String line : list) {
                out.append(line).append(" ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toString();
    }

    /**
     * Parsing the file
     *
     * @param file - file to parse
     * @return array of values from file without comments and white symbols
     */
    public static String[] parseFile(File file) {
        StringBuilder str = new StringBuilder(getData(file));
        Deque<Integer> commentsStarts = new LinkedList<>();
        int i = 0;
        while (i < str.length()) {
            int code = str.charAt(i);
            if (code == '\uD83D' && i + 1 < str.length() && str.charAt(i + 1) == '\uDC2A') {
                commentsStarts.add(i);
                i++;
            } else {
                if (!commentsStarts.isEmpty() && code == '\uD83C' && i + 1 < str.length() && str.charAt(i + 1) == '\uDFDC') {
                    int begin = commentsStarts.removeLast();
                    str.replace(begin, i + 2, " ");
                    i = begin;
                } else {
                    i++;
                }
            }
        }

        return str.toString().trim().split("\\s+");
    }
}
