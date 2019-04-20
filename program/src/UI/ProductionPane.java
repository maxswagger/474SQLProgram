package UI;

import Backend.PersonResult;
import Backend.ProductionResult;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ProductionPane extends DetailPane {
    private ProductionResult result;
    private JList<PersonResult> list;
    private DefaultListModel<PersonResult> listModel;

    public ProductionPane(Window frame, ProductionResult result) {
        super(frame);
        this.result = result;

        //Construct entire pane
        constructUI();
        buildCastAndCrewPanel();
    }

    private void constructUI() {
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
    }

    private void buildCastAndCrewPanel() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        list.addListSelectionListener(new PersonListener());
        scrollPane = new JScrollPane(list);
        result.addCastCrew();

        for(PersonResult person : result.getCastCrew()) {
            listModel.addElement(person);
        }

        this.add(scrollPane, BorderLayout.EAST);
    }

    /**
     * Generate a person pane when clicking a person
     */
    private class PersonListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!list.isSelectionEmpty() && !list.getValueIsAdjusting()) {
                PersonResult result = list.getSelectedValue();
                DetailPane newPersonPane = new PersonPane(windowFrame, result);
                windowFrame.pushScreen(newPersonPane);
            }
        }
    }
}
