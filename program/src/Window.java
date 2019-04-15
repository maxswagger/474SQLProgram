import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Window class which contains the main window and starting page
 * to search database and see results
 */
public class Window extends JFrame {

    private SQLServer server;

    private JPanel topPanel;
    private JButton searchButton;
    private JTextField searchField;
    private JButton settingsButton;

    private JPanel centerPanel;
    private JTextArea searchResult;

    /**
     * Set up content of the window
     */
    Window(){
        server = new SQLServer();
        try { server.connectTo("71.63.48.66");
        } catch (SQLException ignored) {}

        topPanel = new JPanel();
        searchButton = new JButton("Search");
        searchField = new JTextField(35);
        settingsButton = new JButton("Settings");

        centerPanel = new JPanel();
        try { searchResult = new JTextArea(server.simpleRead());
        } catch (SQLException ignored) {}
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(settingsButton);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(searchResult, BorderLayout.CENTER);
    }
}
