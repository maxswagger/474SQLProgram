package UI;

import Backend.PersonResult;
import Backend.ProductionResult;
import Backend.TupleResult;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Main class for building and displaying a rich content pane for search results
 * Author: Tyree Mitchell
 */
public class DetailPane extends JPanel {
    Window windowFrame;

    JPanel headerPanel;
    JPanel detailPanel;
    JScrollPane castScrollPane;

    JTabbedPane extrasPane;
    JList<PersonResult> list;
    JList<ProductionResult> prodList;

    /**
     * Create a new panel.
     * @param frame
     */
    public DetailPane(Window frame) {
        setLayout(new BorderLayout());
        windowFrame = frame;
        setVisible(false);
    }

    /**
     * Build a content pane to display information related to the result,
     * used for building content for movies and other forms of entertainment.
     */
    public void buildEntertainmentPanel(ProductionResult result) {
            JLabel titleLabel = new JLabel();
            JLabel ratingsLabel;

            JLabel adultRating;
            JLabel startEndYears;
            GridBagConstraints gridConstraints;

            JLabel runTime;
            JLabel genreLabel;

            //Build various labels
            if(result.getName() != null)
               titleLabel.setText(result.getName());


            String yearString = "Years running: ";
            yearString += result.getStartYear() + " - " + result.getEndYear();
            startEndYears = new JLabel(yearString);


            runTime = new JLabel("Run time: " + result.getRunTime());
            genreLabel = new JLabel("Genre: " + result.getGenre());


            String aRating = "";
            if(result.isAdult())
                aRating = "Adult Rating: Mature audiences only";
            else
                aRating = "Adult Rating: Everyone";

            adultRating = new JLabel(aRating);

            headerPanel = new JPanel();
            headerPanel.setLayout(new GridBagLayout());

            JButton backButton = new JButton("Back");
            backButton.addActionListener(new ButtonListener());

            //Build the layout of the content page
            gridConstraints = new GridBagConstraints();
            gridConstraints.insets = new Insets(1, 1, 2, 2);

            gridConstraints.gridx = 0;
            gridConstraints.gridy = 0;
            gridConstraints.gridwidth = 1;
            headerPanel.add(backButton, gridConstraints);

            gridConstraints.gridx = 5;
            gridConstraints.gridy = 0;
            gridConstraints.gridwidth = 6;
            headerPanel.add(titleLabel, gridConstraints);

            gridConstraints.gridx = 0;
            gridConstraints.gridy = 1;
            gridConstraints.gridwidth = 2;
            headerPanel.add(adultRating, gridConstraints);

            gridConstraints.gridx = 3;
            gridConstraints.gridy = 1;
            gridConstraints.gridwidth = 3;
            headerPanel.add(startEndYears, gridConstraints);

            gridConstraints.gridx = 7;
            gridConstraints.gridy = 1;
            gridConstraints.gridwidth = 3;
            headerPanel.add(runTime, gridConstraints);

            gridConstraints.gridx = 12;
            gridConstraints.gridy = 1;
            gridConstraints.gridwidth = 3;
            headerPanel.add(genreLabel, gridConstraints);

            //Finally add it to the main frame.
            this.add(headerPanel, BorderLayout.NORTH);




            //Create a cast and crew panel
             DefaultListModel<PersonResult> listModel = new DefaultListModel();
             list = new JList(listModel);
             list.addListSelectionListener(new PersonListener());
             castScrollPane = new JScrollPane(list);
             result.addCastCrew();

             for(PersonResult person : result.getCastCrew()) {
                 listModel.addElement(person);
             }

             this.add(castScrollPane, BorderLayout.EAST);



             //Create a main section
    }

    /**
     * Create a pane for a specific person that shows relevant person info and relevant productions
     * @param result
     */
    public void createPersonPane(PersonResult result) {
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());


        JLabel nameLabel = new JLabel(result.getName());
        headerPanel.add(nameLabel);


        String livedString = "Born: " + result.getBirthYear() + " - Death: " + result.getDeathYear();
        JLabel livedYears = new JLabel(livedString);

        headerPanel.add(livedYears);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ButtonListener());
        headerPanel.add(backButton, BorderLayout.WEST);

        //Create a known for productions panel
        DefaultListModel<ProductionResult> listModel = new DefaultListModel();
        prodList = new JList(listModel);
        prodList.addListSelectionListener(new ProductionListener());
        castScrollPane = new JScrollPane(prodList);
        result.getKnownProductions();

        for(ProductionResult person : result.getProductions()) {
            listModel.addElement(person);
        }

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(castScrollPane, BorderLayout.CENTER);


    }

    /**
     * Button listener
     * To add more button listeners just copy the if block and modify getActionCommand
     */
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Back")) {
                //windowFrame.mainScreen();
                windowFrame.goBack();
            }
        }
    }

    /**
     * Generate a person pane when clicking a person
     */
    private class PersonListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!list.isSelectionEmpty() && !list.getValueIsAdjusting()) {
                PersonResult result = list.getSelectedValue();
                DetailPane newPersonPane = new DetailPane(windowFrame);
                newPersonPane.createPersonPane(result);
                windowFrame.pushScreen(newPersonPane);
            }
        }
    }

    /**
     * Generate a production pane when clicking a production
     */
    private class ProductionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!prodList.isSelectionEmpty() && !prodList.getValueIsAdjusting()) {
                ProductionResult result = prodList.getSelectedValue();
                DetailPane newProductionPane = new DetailPane(windowFrame);
                newProductionPane.buildEntertainmentPanel(result);
                windowFrame.pushScreen(newProductionPane);
            }
        }
    }
}
