package UI;

import Backend.PersonResult;
import Backend.ProductionResult;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class PersonPane extends DetailPane {
    PersonResult result;
    private JList<ProductionResult> prodList;

    public PersonPane(Window frame, PersonResult result) {
        super(frame);
        this.result = result;

        constructUI();
        buildProductionPanel();
    }

    private void constructUI() {
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


    }

    private void buildProductionPanel() {
        //Create a known for productions panel
        DefaultListModel<ProductionResult> listModel = new DefaultListModel();
        prodList = new JList(listModel);
        prodList.addListSelectionListener(new ProductionListener());

        scrollPane = new JScrollPane(prodList);
        result.getKnownProductions();

        for(ProductionResult person : result.getProductions()) {
            listModel.addElement(person);
        }

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }


    /**
     * Generate a production pane when clicking a production
     */
    private class ProductionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!prodList.isSelectionEmpty() && !prodList.getValueIsAdjusting()) {
                ProductionResult result = prodList.getSelectedValue();
                DetailPane newProductionPane = new ProductionPane(windowFrame, result);
                windowFrame.pushScreen(newProductionPane);
            }
        }
    }
}
