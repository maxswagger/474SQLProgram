import UI.Window;

import java.sql.SQLException;
import javax.swing.*;

/**
 * 474 SQL Server interacting program
 * By Maksim Samoylov, ... add your names lads
 */
public class Main {

    /**
     * Main method to just launch the program
     */
    public static void main( String[] args ) throws SQLException {
        try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception ignored){}

        Window window = new Window();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800,600);
        window.setVisible(true);
    }
}
