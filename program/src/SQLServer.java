import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interact with physical server with this class
 */
public class SQLServer {

    private Connection conn;
    private MysqlDataSource dataSource;
    private Statement stmt;
    private ResultSet result;

    SQLServer(){
    }

    public void connectTo(String address) throws SQLException{
        dataSource = new MysqlDataSource();
        dataSource.setPort(3306); // could also be 3306
        dataSource.setUseSSL(false);
        dataSource.setDatabaseName("imdb");
        dataSource.setUser("program");
        dataSource.setPassword("Yeehaw420$");
        dataSource.setServerName(address); // "mysql.cs.jmu.edu" or "71.63.48.66" or "10.0.0.154"

        System.out.println("Connecting...");
        conn = (Connection) dataSource.getConnection();
    }

    public String simpleRead() throws SQLException {

        System.out.println( "Building Unique Set . . .\n" );
        stmt = (Statement) conn.createStatement();

        // Get info from IMDB_ORG.Title_Basics
        result = stmt.executeQuery( "SELECT * FROM job;" );

//        set = new TempSet();
        ArrayList<String> list = new ArrayList<>();

        // Add to TempSet ArrayList to get unique values
        while ( result.next() ) {
            String genres = result.getString( "jobID" );
            if ( genres != null && genres.length() > 0 ) {
                String[] array = genres.split( ",|\\n" );
                if ( array.length > 0 ) {
                    for ( String item : array ) {
                        list.add( item );
                    }
                }
            }
        }

        return list.toString().replaceAll(",","\n");
    }
}
