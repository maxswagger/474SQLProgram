package UI;

import Backend.SQLServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Created by Phillip Zubov
 * 4/22/2019.
 */
public class SettingsFrame extends JFrame{

    //public JFrame settingsWindow;
    private JLabel addressLabel,portLabel,nameLabel,usernameLabel,passwordLabel, turnAdultFilterOffLabel;
    private JPanel panel,changeConnectionPanel, adultFilterPanel, addressPanel, portPanel, namePanel,userPanel, passPanel;
    private JTextField address,port,databaseName,username,password;
    private JCheckBoxMenuItem adultFilter;
    private JButton changeConnectionButton;
    private SQLServer newConnectionServer;

    //private static Insets insets = new Insets(0, 0, 0, 0);

    public SettingsFrame(SQLServer server){
        newConnectionServer = server;
        new JFrame("Settings");
        changeConnectionPanel = new JPanel();
        GroupLayout layout = new GroupLayout(changeConnectionPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //changeConnectionPanel.setLayout();
        //GridBagConstraints gbc = new GridBagConstraints();

        //changeConnectionLabel = new JLabel("Type new connection parameters here(e.g. <server address> <server port> <database name> <username> <password>): ");
        //changeConnectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnAdultFilterOffLabel = new JLabel("Check this box if you are under the age of 18 or if you wish to not view NSFW content ");
        addressLabel = new JLabel("Server address: ");
        portLabel = new JLabel("Server port: ");
        nameLabel = new JLabel("Database name: ");
        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");

        address = new JTextField();
        address.setSize(new Dimension(100,24));
        port = new JTextField();
        port.setSize(new Dimension(20, 24));
        databaseName = new JTextField();
        databaseName.setSize(new Dimension(50, 24));
        username = new JTextField();
        username.setSize(new Dimension(40, 24));
        password = new JTextField();
        password.setSize(new Dimension(50, 24));

        addressPanel = new JPanel();
        portPanel = new JPanel();
        namePanel = new JPanel();
        userPanel = new JPanel();
        passPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.X_AXIS));
        portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.X_AXIS));
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));

        changeConnectionButton = new JButton("Change Server");
        changeConnectionButton.addActionListener(changeConnection());
        // changeConnectionPanel = new JPanel();

        addressPanel.add(addressLabel);
        addressPanel.add(address);
        portPanel.add(portLabel);
        portPanel.add(port);
        namePanel.add(nameLabel);
        namePanel.add(databaseName);
        userPanel.add(usernameLabel);
        userPanel.add(username);
        passPanel.add(passwordLabel);
        passPanel.add(password);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(addressPanel)
                .addComponent(namePanel)
                .addComponent(userPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(portPanel))
                .addComponent(passPanel));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressPanel)
                        .addComponent(portPanel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(namePanel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(userPanel)
                        .addComponent(passPanel))
        );

        changeConnectionPanel.setLayout(layout);
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weightx = 0.5;
//        gbc.gridwidth = 3;
//        gbc.gridx = 1;
//        gbc.gridy = 0;
        //changeConnectionPanel.add(changeConnectionLabel, gbc);
        // changeConnectionPanel.add(changeConnectionLabel);
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weightx = 0.5;
//        gbc.gridwidth = 1;
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        //changeConnectionPanel.add(changeConnectionTextField, gbc);

//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 0;
//        gbc.gridwidth = 1;
//        gbc.gridx = 2;
//        gbc.gridy = 1;
        //changeConnectionPanel.add(changeConnectionButton,gbc);

        add(changeConnectionPanel);
        //pack();


        setSize(800,300);
        setVisible(true);
    }

    /**
     * action listener for change connection button
     * @return actionlistener for changing connection button
     */
    private ActionListener changeConnection(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /*
                Split and try to connect to new server using the input in the textfield. Catch any exceptions it
                encounters and create a dialog box that will warn the user that their input is invalid.
                 */
                try {
                    newConnectionServer.connectTo(address.getText(), Integer.parseInt(port.getText()),
                            databaseName.getText(), username.getText(), password.getText());//TODO fix GUI,  implement adultFilter

                } catch (Exception s) {
                    JDialog error = new JDialog(SettingsFrame.this, "Error!");
                    error.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    JPanel panel = new JPanel();
                    BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
                    panel.setLayout(boxLayout);
                    panel.setBorder(new EmptyBorder(new Insets(15,20,15,20)));

                    JButton okButton = new JButton("OK");
                    JLabel errorLabel = new JLabel("Invalid connection input.\n" +
                            "Make sure all fields are correct.");

                    okButton.addActionListener(new ActionListener() {//closes the window when ok button pressed
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            error.dispose();
                        }
                    });

                    errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panel.add(errorLabel);
                    panel.add(okButton);
                    error.add(panel);

                    error.setLocationRelativeTo(null);
                    error.pack();
                    error.setVisible(true);
                }

            }
        };
    }
}
