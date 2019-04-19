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

    private DefaultListModel<ProductionResult> listModel;
    private JList<ProductionResult> searchList;
    private JScrollPane listScroll;

    private DetailPane dPane;

    /**
     * Set up content of the window
     */
    public Window(){
        server = new SQLServer();
        try {
            server.connectTo("71.63.48.66", 3306, "imdb", "program", "Yeehaw420$");
        } catch (SQLException ignored) {}
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        topPanel = new JPanel();
        searchButton = new JButton("Search");
        searchButton.addActionListener(searchFunction());
        searchField = new JTextField(35);
        settingsButton = new JButton("Settings");

        listModel = new DefaultListModel<ProductionResult>();
        searchList = new JList<ProductionResult>(listModel);
        searchList.addListSelectionListener(new searchListListener());
        listScroll = new JScrollPane(searchList);

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(settingsButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(listScroll, BorderLayout.CENTER);
        mainPanel.setVisible(true);
        add(mainPanel);
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
                    //ArrayList<TupleResult> resultList = server.simpleRead(searchField.getText());
                    populateList(resultList);
                } catch (SQLException e) {System.out.println("Failed to read");}
            }
        };
    }

    /**
     * function to populate the results into the list
     * @param list
     */
    private void populateList(List<ProductionResult> list){
        listModel.clear();
        for (ProductionResult i : list){
            listModel.addElement(i);
        }
    }

    /**
     * Clear the list of previous entries before searching again
     */
    private void refreshSearch() {

        searchList.addListSelectionListener(new searchListListener());
        searchList.removeAll();
        searchList.clearSelection();
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
     * Show the content page for that specific result
     * @param result
     */
    private void goToDetailPage(ProductionResult result) {
        dPane = new DetailPane(this);
        mainPanel.setVisible(false);
        dPane.buildEntertainmentPanel(result);
        dPane.setVisible(true);
        add(dPane, BorderLayout.CENTER);
    }
/*
    private ListSelectionListener searchResultListener(){
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("Clicked "+ listModel.get(e.getFirstIndex()) );
                if(!searchList.isSelectionEmpty() && !searchList.getValueIsAdjusting()) {
                    TupleResult selectedResult = searchList.getSelectedValue();
                }
            }
        };
    } */

    /**
     * Modified list listener to help with debounce
     */
    private class searchListListener implements ListSelectionListener
    {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            System.out.println("Clicked "+ listModel.get(e.getFirstIndex()) );
            if(!searchList.isSelectionEmpty() && !searchList.getValueIsAdjusting()) {
                TupleResult selectedResult = (TupleResult) searchList.getSelectedValue();

                if(selectedResult.getType().equals("Movie")) {
                    ProductionResult netResult = (ProductionResult) selectedResult;
                    goToDetailPage(netResult);
                }
            }
        }
    }
}
