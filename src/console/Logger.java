package console;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides logging to the log console and log file
 *
 * @author vpavlov
 */
public class Logger {

    /**
     * Text color for errors
     */
    private static final Color ERROR_TEXT_COLOR = Color.RED;

    /**
     * Text color for warnings
     */
    private static final Color WARNING_TEXT_COLOR = Color.YELLOW;

    /**
     * Text color for headers
     */
    private static final Color HEADER_TEXT_COLOR = Color.GREEN;

    /**
     * Text color for info
     */
    private static final Color INFO_TEXT_COLOR = Color.WHITE;

    /**
     * Text color for event
     */
    private static final Color EVENT_TEXT_COLOR = Color.PINK;

    /**
     * Text color for help info
     */
    private static final Color HELP_TEXT_COLOR = Color.YELLOW;

    /**
     * Text color for debug
     */
    private static final Color DEBUG_TEXT_COLOR = Color.BLUE;

    /**
     * Date formatter for timestamps
     */
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss.SSS");

    /**
     * File to log into
     */
    private PrintWriter file;

    /**
     * Console to log into
     */
    private LogConsole console;

    /**
     * Default constructor
     * Creates new console and file to log into
     *
     * @throws FileNotFoundException if there is error during file creation
     */
    public Logger() throws FileNotFoundException {
        this.file = new PrintWriter("log.txt");
        this.console = new LogConsole();
    }

    /**
     * Constructor
     *
     * @param file    file to log into
     * @param console console to log into
     * @throws FileNotFoundException if there is error during file opening
     */
    public Logger(File file, LogConsole console) throws FileNotFoundException {
        this.file = new PrintWriter(file);
        this.console = console;
    }

    /**
     * Constructor
     *
     * @param file file to log into
     * @throws FileNotFoundException if there is error during file opening
     */
    public Logger(File file) throws FileNotFoundException {
        this.file = new PrintWriter(file);
    }

    /**
     * {
     * Constructor
     *
     * @param console console to log into
     */
    public Logger(LogConsole console) {
        this.console = console;
    }

    /**
     * Creates a timestamp
     *
     * @return timestamp
     */
    private String timestamp() {

        LocalDateTime now = LocalDateTime.now();
        return "[" + DTF.format(now) + "] :";
    }

    /**
     * Log to the console
     *
     * @param message message to log
     * @throws UnsupportedOperationException if console is not attached (null)
     */
    public void logToConsole(String message) throws UnsupportedOperationException {
        if (console == null) {
            throw new UnsupportedOperationException("Console is not connected to the logger.");
        }
        console.logTimestamp(timestamp());
        console.println(message);
    }

    /**
     * Log to the console with specified color
     *
     * @param message message to log
     * @param color   specified color
     * @throws UnsupportedOperationException if console is not attached (null)
     */
    public void logToConsole(String message, Color color) throws UnsupportedOperationException {
        if (console == null) {
            throw new UnsupportedOperationException("Console is not connected to the logger.");
        }
        console.logTimestamp(timestamp());
        console.println(message, color);
    }

    /**
     * Log to the console with specified logging tag
     *
     * @param message message to log
     * @param type    logging tag
     * @throws UnsupportedOperationException if console is not attached (null)
     */
    public void logToConsole(String message, LogType type) throws UnsupportedOperationException {
        if (console == null) {
            throw new UnsupportedOperationException("Console is not connected to the logger.");
        }
        console.logTimestamp(timestamp());
        switch (type) {
            case INFO -> console.println("[INFO] " + message, INFO_TEXT_COLOR);
            case WARNING -> console.println("[WARN] " + message, WARNING_TEXT_COLOR);
            case ERROR -> console.println("[ERROR] " + message, ERROR_TEXT_COLOR);
            case HEADER -> console.println(message, HEADER_TEXT_COLOR);
            case EVENT -> console.println("[EVENT] "+message, EVENT_TEXT_COLOR);
            case HELP_INFO -> console.println("[HELP] "+message, HELP_TEXT_COLOR);
            case DEBUG -> console.println("[DEBUG] "+message, DEBUG_TEXT_COLOR);
            default -> console.println(message, INFO_TEXT_COLOR);
        }
    }

    /**
     * Log to the file
     *
     * @param message message to log
     * @throws UnsupportedOperationException if file is not attached (null)
     */
    public void logToFile(String message) throws UnsupportedOperationException {
        if (file == null) {
            throw new UnsupportedOperationException("File is not connected to the logger.");
        }
        file.println(timestamp() + "  " + message);
    }

    /**
     * Log to the file with specified logging tag
     *
     * @param message message to log
     * @param type    logging tag
     * @throws UnsupportedOperationException if file is not attached (null)
     */
    public void logToFile(String message, LogType type) throws UnsupportedOperationException {
        if (file == null) {
            throw new UnsupportedOperationException("File is not connected to the logger.");
        }
        switch (type) {
            case INFO -> file.println(timestamp() + "  " + "[INFO] " + message);

            case WARNING -> file.println(timestamp() + "  " + "[WARN] " + message);

            case ERROR -> file.println(timestamp() + "  " + "[ERROR] " + message);

            case EVENT -> file.println(timestamp() + " " + "[EVENT] " + message);

            default -> file.println(timestamp() + "  " + message);

        }

    }

    /**
     * Log message to the console and to the file at the same time (if there are connected) <br>
     * If one of the outputs are not connected, then log is carried out only for onew
     *
     * @param message message to log
     * @param logType logging tag
     * @throws UnsupportedOperationException if both outputs are not connected.
     */
    public void log(String message, LogType logType) throws UnsupportedOperationException {
        boolean success = false;
        if (file != null) {
            logToFile(message, logType);
            success = true;
        }
        if (console != null) {
            logToConsole(message, logType);
            success = true;
        }
        if (!success) {
            throw new UnsupportedOperationException("Console and file are not connected to the logger.");
        }
    }

    /**
     * Close the logging file
     */
    public void close() {
        file.close();
    }
}
