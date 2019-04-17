import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    private DefaultListModel<TupleResult> listModel;
    private JList searchList;
    private JScrollPane listScroll;

    /**
     * Set up content of the window
     */
    Window(){
        server = new SQLServer();
        try {
            server.connectTo("71.63.48.66", 3306, "imdb", "program", "Yeehaw420$");
        } catch (SQLException ignored) {}

        topPanel = new JPanel();
        searchButton = new JButton("Search");
        searchButton.addActionListener(searchFunction());
        searchField = new JTextField(35);
        settingsButton = new JButton("Settings");

        listModel = new DefaultListModel();
        searchList = new JList(listModel);
        searchList.addListSelectionListener(searchResultListener());
        listScroll = new JScrollPane(searchList);

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(settingsButton);

        add(topPanel, BorderLayout.NORTH);
        add(listScroll, BorderLayout.CENTER);
    }

    /**
     * action listener for the search button
     * @return
     */
    private ActionListener searchFunction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    ArrayList<TupleResult> resultList = server.simpleRead(searchField.getText());
                    populateList(resultList);
                } catch (SQLException e) {System.out.println("Failed to read");}
            }
        };
    }

    /**
     * function to populate the results into the list
     * @param list
     */
    private void populateList(List<TupleResult> list){
        listModel.clear();
        for (TupleResult i : list){
            listModel.addElement(i);
        }
    }

    private ListSelectionListener searchResultListener(){
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("Clicked "+ listModel.get(e.getFirstIndex()) );
            }
        };
    }
}
