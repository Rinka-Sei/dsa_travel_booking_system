import gui.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Application Entry Point.
 * Launches the LoginFrame upon startup.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}