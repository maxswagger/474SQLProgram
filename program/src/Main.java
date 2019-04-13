import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Load Genre into IMDB_NEW from the genre_array attribute in
 * IMDB_ORG.Title_Basics
 *
 * @author Michael Norton
 * @version GP3 (6 March 2019)
 *
 */
public class Main {

    /**
     * Main method.
     *
     * @param args Command Line Arguments
     * @throws SQLException The SQL Exceptoin
     */
    public static void main( String[] args ) throws SQLException {

        Connection conn;
        MysqlDataSource dataSource;
        PreparedStatement pstmt;
        Statement stmt;
        ResultSet rs;
        String updateString;
        TempSet set;

        System.out.println("Connecting...");

        dataSource = new MysqlDataSource();
        dataSource.setPort( 3306 ); // could also be 3306
        dataSource.setUseSSL( false );
        dataSource.setDatabaseName( "imdb" );
        dataSource.setUser( "program" );
        dataSource.setPassword( "Yeehaw420$" );
        dataSource.setServerName( "71.63.48.66" ); // "mysql.cs.jmu.edu" if
        // running from Eclipse

        conn = (Connection) dataSource.getConnection();

        System.out.println( "Building Unique Set . . .\n" );

        stmt = (Statement) conn.createStatement();

        /**
         * From this point down, change the name of the database to the
         * name of your group's database
         */

        // clear table if not empty
        //stmt.executeUpdate( "DELETE FROM IMDB_yourNames.Genre" ); //

        // Get info from IMDB_ORG.Title_Basics
        rs = stmt.executeQuery( "SELECT * FROM job;" );

        set = new TempSet();

        // Add to TempSet ArrayList to get unique values
        while ( rs.next() ) {

            String genres = rs.getString( "jobID" );

            if ( genres != null && genres.length() > 0 ) {
                String[] array = genres.split( ",|\\n" );

                if ( array.length > 0 ) {
                    for ( String item : array ) {

                        set.add( item );
                    }
                }
            }
        }

        System.out.println(set);

//        System.out.println( "Inserting into \"Genre\" . . .\n" );
//
//        // create prepared statement
//        updateString = "INSERT INTO IMDB_yourNames.Genre (genreID) VALUES (?)";
//        pstmt = (PreparedStatement) conn.prepareStatement( updateString );
//
//        // set value for prepared statement from the TempSet ArrayList
//        // and execute
//        for ( int i = 0; i < set.size(); i++ ) {
//
//            System.out.println( "\tInserting \"" + set.get( i ) + "\"" );
//
//            pstmt.setString( 1, set.get( i ) );
//            pstmt.executeUpdate();
//        }
//
//        rs = stmt.executeQuery( "SELECT COUNT(*) FROM IMDB_yourNames.Genre" );
//
//        rs.next();
//        System.out.println( "\n" + rs.getInt( 1 ) + " rows added. " );

    }

    /**
     * Inner class to get unique instances of the genre.
     *
     * @author Michael Norton
     * @version
     */
    private static class TempSet {

        private ArrayList<String> list;

        /**
         * Default constructor.
         */
        public TempSet() {

            list = new ArrayList<String>();
        }

        /**
         * Add unique instances to the ArrayList.
         *
         * @param str The string to add
         */
        public void add( String str ) {

            if ( !list.contains( str ) ) {
                list.add(  str );
            }

        }

        /**
         * Get an element from the ArrayList
         *
         * @param which Which element to get
         * @return the requested element
         */
        public String get( int which ) {

            String val = null;

            if ( which >= 0 && which < list.size() ) {

                val = list.get( which );
            }

            return val;
        }

        /**
         * Return the size of the ArrayList
         *
         * @return the size of the list
         */
        public int size() {

            return list.size();
        }

        /**
         * Return the arrayList as a String
         */
        public String toString() {

            String returnString = "";

            for ( String str: list ) {

                returnString += str + "\n";
            }

            return returnString;
        }
    }
}
