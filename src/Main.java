import model.Simulation;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
        String[] res = Parser.parseFile(new File("tutorial.txt"));
        //System.out.println(Arrays.toString(res));
        Simulation simulation = new Simulation(res);
        //System.out.println(simulation.toString());
        simulation.showPath(0,9);
    }
}