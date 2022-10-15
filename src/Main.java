import helpers.Point;
import model.Simulation;

import java.io.File;


/**
 * Main program class, program entry point
 *
 * @author vpavlov
 */
public class Main {

    /**
     * Main program entry point
     *
     * @param args - program arguments
     * @author vpavlov
     */
    public static void main(String[] args) {
        String[] res = Parser.parseFile(new File("test_input.txt"));
        Simulation simulation = new Simulation(res);
        System.out.println(simulation.toString());
        //simulation.generateCamel();
        //simulation.showPath(0,29);
        simulation.showPaths();
    }
}
