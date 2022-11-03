import com.vpavlov.console.Logger;
import com.vpavlov.simulation.helpers.Parser;
import com.vpavlov.simulation.model.Simulation;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


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
    public static void main(String[] args) throws InterruptedException, IOException {
        Locale.setDefault(Locale.US);
        String[] res = Parser.parseFile(new File("data_v2/dense_small.txt"));
        Logger logger = new Logger(true);
        Simulation simulation = new Simulation(res, logger);
        simulation.simulate();
        logger.close();
    }
}
