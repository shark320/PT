import model.Simulation;

import java.io.File;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int count = 0;
        int prev = 0;
        String[] res = Parser.parseFile(new File("parser.txt"));
        System.out.println(Arrays.toString(res));
        Simulation simulation = new Simulation(res);
        System.out.println(simulation.toString());
    }
}