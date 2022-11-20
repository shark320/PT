import com.vpavlov.console.LogType;
import com.vpavlov.console.Logger;
import com.vpavlov.simulation.helpers.Parser;
import com.vpavlov.simulation.model.Simulation;

import java.io.File;
import java.io.FileNotFoundException;
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
     */
    public static void main(String[] args){
        if (args.length !=1){
            System.err.println("Wrong number of arguments");
            return;
        }
        Locale.setDefault(Locale.US);
        Logger logger ;
        try {
            logger = new Logger(true);
        } catch (FileNotFoundException e) {
            System.err.println("Could not create log file");
            return;
        }
        logger.log(String.format("Available memory: %f MB",Runtime.getRuntime().freeMemory()/1e6), LogType.WARNING);

        String path = args[0];
        File file = new File(path);
        if (!file.exists()){
            System.err.println("File does not exist.");
            return;
        }

        String[] res = Parser.parseFile(file);

        Simulation simulation = new Simulation(res, logger);
        simulation.simulate();
        logger.close();
    }
}
