import gui.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Application Entry Point for the Travel Booking System.
 * Initializes and displays the main GUI application on the Event Dispatch Thread (EDT).
 */
public class Main {

    /**
     * Default constructor for Main.
     */
    public Main() {
    }

    /**
     * Main entry point method that launches the application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
