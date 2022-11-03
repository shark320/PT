package console;

import javax.swing.*;
import javax.swing.text.*;
import java.util.*;
import java.awt.*;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Log console in a special window with multiply colors logs
 *
 * @author vpavlov
 */
public class LogConsole {

    /**
     * Inner class represents styled string for java awt StyledDocument
     *
     * @author vpavlov
     */
    private static final class StyledString {
        /**
         * String
         */
        public String string;

        /**
         * Attributes set
         */
        public AttributeSet attributes;

        /**
         * Constructor
         *
         * @param string     string
         * @param attributes attributes set
         */
        public StyledString(String string, AttributeSet attributes) {
            this.string = string;
            this.attributes = attributes;
        }
    }

    /**
     * TextPanel update frequency [milliseconds]
     */
    private static final long UPDATE_FREQUENCY = 200;

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
     * Output buffer
     */
    private final BlockingQueue<StyledString> buffer = new LinkedBlockingQueue<>();


    /**
     * Constructor
     */
    public LogConsole() {
        textArea.setBackground(DEFAULT_BG_COLOR);
        textArea.setForeground(DEFAULT_TEXT_COLOR);
        textArea.setFont(DEFAULT_FONT);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 300);
        frame.add(scrollPane);
        frame.setTitle("Log Console");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        standWithUkraine();

        Timer tm = new Timer();
        tm.schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, UPDATE_FREQUENCY);
    }

    /**
     * Print Stand with Ukraine styled header
     */
    private void standWithUkraine() {

        println("\t   ###########", Color.BLUE);
        println("\t   ###########", Color.BLUE);
        print("\tSTAND WITH ", Color.BLUE);
        println("UKRAINE", Color.YELLOW);
        println("\t   ###########", Color.YELLOW);
        println("\t   ###########", Color.YELLOW);
        println("\n");
        repaint();
    }

    /**
     * Print all strings from the buffer and repaint the TextPane
     */
    private void repaint() {
        //System.out.println("REPAINT " + buffer.isEmpty());
        if (!buffer.isEmpty()) {
            StyledDocument document = textArea.getStyledDocument();
            textArea.setDocument(new DefaultStyledDocument());
            while (!buffer.isEmpty()) {
                StyledString line = buffer.poll();
                try {
                    document.insertString(document.getLength(), line.string, line.attributes);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
            textArea.setDocument(document);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    /**
     * Logs a timestamp to the console
     *
     * @param timestamp timestamp to log
     */
    public void logTimestamp(String timestamp) {
        print(timestamp + "\t", TIMESTAMP_COLOR);
    }

    /**
     * Log a message to the console
     *
     * @param message message to log
     */
    public void print(String message) {
        buffer.add(new StyledString(message, setColor(DEFAULT_TEXT_COLOR)));
    }

    /**
     * Log a message to the console with specified color
     *
     * @param message message to log
     * @param color   text color
     */
    public void print(String message, Color color) {
        buffer.add(new StyledString(message, setColor(color)));
    }

    /**
     * Print message and new line symbol ('\n')
     *
     * @param message message to print
     */
    public void println(String message) {
        print(message + "\n");
    }

    /**
     * Print message and new line symbol ('\n') with specified color
     *
     * @param message message to print
     * @param color   color to use
     */
    public void println(String message, Color color) {
        print(message + "\n", color);
    }

    /**
     * Set the console text color
     *
     * @param color color to set
     */
    private AttributeSet setColor(Color color) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset;
        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        //textArea.setCharacterAttributes(aset, false);
        return aset;
    }


}
