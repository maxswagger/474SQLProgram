package UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.soap.Detail;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import Backend.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * UI.Window class which contains the main window and starting page
 * to search database and see results
 */
public class Window extends JFrame {

    private SQLServer server;

    private JPanel topPanel;
    private JButton searchButton;
    private JTextField searchField;
    private JButton settingsButton;
    private JPanel mainPanel;
    private JPanel currentPanel;

    private DefaultListModel<TupleResult> listModel;
    private JList<TupleResult> searchList;
    private JScrollPane listScroll;
    private searchListListener listener;
    private Stack<JPanel> screenStack;
    private DetailPane dPane;

    /**
     * Set up content of the window
     */
    public Window(){
        server = new SQLServer();
        try {
            server.connectTo("localhost", 3306, "imdb", "root", "");
        } catch (SQLException ignored) {}
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        topPanel = new JPanel();
        searchButton = new JButton("Search");
        searchButton.addActionListener(searchFunction());
        searchField = new JTextField(35);
        settingsButton = new JButton("Settings");

        listModel = new DefaultListModel<TupleResult>();
        searchList = new JList<TupleResult>(listModel);
        listener = new searchListListener();
        searchList.addListSelectionListener(listener);
        listScroll = new JScrollPane(searchList);

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(settingsButton);

        currentPanel = mainPanel;
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(listScroll, BorderLayout.CENTER);
        mainPanel.setVisible(true);
        add(mainPanel);
        screenStack = new Stack<>();
        screenStack.push(mainPanel);
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
                    refreshSearch();
                    ArrayList<ProductionResult> resultList = server.productionRead(searchField.getText());
                    ArrayList<PersonResult> personList = server.personRead(searchField.getText());
                    //ArrayList<TupleResult> resultList = server.simpleRead(searchField.getText());
                    populateList(resultList);
                    populatePersonList(personList);
                } catch (SQLException e) {System.out.println("Failed to read");}
            }
        };
    }

    /**
     * function to populate the results into the list
     * @param list
     */
    private void populateList(List<ProductionResult> list){
        for (ProductionResult i : list){
            listModel.addElement(i);
        }
    }

    /**
     * function to populate the results into the list
     * @param list
     */
    private void populatePersonList(List<PersonResult> list){
        for (PersonResult i : list){
            listModel.addElement(i);
        }
    }

    /**
     * Clear the list of previous entries before searching again
     */
    private void refreshSearch() {

        //searchList.removeAll();
        searchList.removeListSelectionListener(listener);
        searchList.clearSelection();
        listModel.clear();
        listModel = new DefaultListModel<TupleResult>();
        searchList.removeAll();
        searchList.setModel(listModel);
        searchList.addListSelectionListener(listener);
    }

    /**
     * Display the mains search screen again
     */
    public void mainScreen() {
        dPane.setVisible(false);
        mainPanel.setVisible(true);
        add(mainPanel);
    }

    /**
     *  Push a new screen onto the stack and set it as the current screen.
     * @param screen - The screen to add to the stack.
     */
    public void pushScreen(JPanel screen) {
        currentPanel.setVisible(false);
        currentPanel = screen;
        screenStack.push(screen);
        screen.setVisible(true);
        add(screen);
    }

    /**
     * Allows the JFrame to return to a previous screen.
     * Used for back buttons.
     */
    public void goBack() {
        JPanel curPanel = screenStack.pop();
        JPanel prevPanel = screenStack.pop();
        curPanel.setVisible(false);
        currentPanel = prevPanel;
        screenStack.push(prevPanel);
        prevPanel.setVisible(true);
        add(prevPanel);
    }

    private Window getFrame() {
        return this;
    }

    /**
     * Modified list listener to help with debounce
     */
    private class searchListListener implements ListSelectionListener
    {
        @Override
        public void valueChanged(ListSelectionEvent e) {

            if(!searchList.isSelectionEmpty() && !searchList.getValueIsAdjusting()) {
                System.out.println("Clicked "+ listModel.get(e.getFirstIndex()) );
                TupleResult selectedResult = (TupleResult) searchList.getSelectedValue();

                if(selectedResult.getType().equals("Movie")) {
                    ProductionResult netResult = (ProductionResult) selectedResult;
                    dPane = new ProductionPane(getFrame(), (ProductionResult) netResult);
                } else if(selectedResult.getType().equals("Person")) {
                    PersonResult netResult = (PersonResult) selectedResult;
                    dPane = new PersonPane(getFrame(), (PersonResult) netResult);
                }

                currentPanel = dPane;
                mainPanel.setVisible(false);
                dPane.setVisible(true);
                add(dPane, BorderLayout.CENTER);
                screenStack.push(dPane);

            }
        }
    }
}
