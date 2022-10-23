package console;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Log console in a special window with multiply colors logs
 *
 * @author vpavlov
 */
//TODO Speed up swing console using detached document and thread unlocking
public class LogConsole extends OutputStream {

    /**
     * Default background color
     */
    private static final Color DEFAULT_BG_COLOR = Color.BLACK;

    /**
     * Default text color
     */
    private static final Color DEFAULT_TEXT_COLOR = Color.WHITE;

    /**
     * Timestamp color
     */
    private static final Color TIMESTAMP_COLOR = Color.CYAN;

    /**
     * Default font name
     */
    private static final String DEFAULT_FONT_NAME = "Lucida Console";

    /**
     * Default font style
     */
    private static final int DEFAULT_FONT_STYLE = Font.PLAIN;

    /**
     * Default font size
     */
    private static final int DEFAULT_FONT_SIZE = 12;

    /**
     * Default font
     */
    private static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE);

    /**
     * Window JFrame Object
     */
    final JFrame frame = new JFrame();

    /**
     * Text area for logs
     */
    private final JTextPane textArea = new JTextPane();

    /**
     * Output printer for logs
     */
    final PrintStream printStream;

    /**
     * Constructor
     */
    public LogConsole() {
        textArea.setBackground(DEFAULT_BG_COLOR);
        textArea.setForeground(DEFAULT_TEXT_COLOR);
        textArea.setFont(DEFAULT_FONT);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setAutoscrolls(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 300);
        frame.add(scrollPane);
        frame.setTitle("Log Console");
        frame.setVisible(true);
        printStream = new PrintStream(this);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void write(int b) {
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        textArea.replaceSelection(String.valueOf((char) b));
    }

    /**
     * Logs a timestamp to the console
     *
     * @param timestamp timestamp to log
     */
    public void logTimestamp(String timestamp) {
        setColor(TIMESTAMP_COLOR);
        printStream.print(timestamp + "\t");
        resetColor();
    }

    /**
     * Log a message to the console
     *
     * @param message message to log
     */
    public void log(String message) {
        printStream.println(message);
        printStream.println();
    }

    /**
     * Log a message to the console with specified color
     *
     * @param message message to log
     * @param color   text color
     */
    public void log(String message, Color color) {
        setColor(color);
        printStream.println(message);
        printStream.println();
        resetColor();
    }

    /**
     * Set the console text color
     *
     * @param color color to set
     */
    public void setColor(Color color) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset;
        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        textArea.setCharacterAttributes(aset, false);
    }

    /**
     * Reset the console text color to default
     */
    public void resetColor() {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset;
        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_TEXT_COLOR);
        textArea.setCharacterAttributes(aset, false);
    }

}
