import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        searchButton.addActionListener(searchFunction());
        searchField = new JTextField(35);
        settingsButton = new JButton("Settings");

        centerPanel = new JPanel();
        searchResult = new JTextArea();
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(settingsButton);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(searchResult, BorderLayout.CENTER);
    }

    private ActionListener searchFunction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try { searchResult.append(server.simpleRead());
                } catch (SQLException e) {System.out.println("Failed to read");}
            }
        };
    }


}
