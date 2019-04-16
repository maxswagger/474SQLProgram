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

    /**
     * no args constructor
     */
    SQLServer(){
    }

    /**
     * set up the connection to a server
     * @param address
     * @throws SQLException
     */
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
        System.out.println("Connected\n");
    }

    /**
     * perform a search query on the server
     * @return list of the tuples from query
     * @throws SQLException
     */
    public ArrayList<TupleResult> simpleRead() throws SQLException {

        System.out.println( "Building Unique Set . . ." );
        stmt = (Statement) conn.createStatement();
        result = stmt.executeQuery( "SELECT * FROM production LIMIT 50;" );

        ArrayList<TupleResult> list = new ArrayList<>();
        while ( result.next() ) {
            String ID = result.getString( "prodID" );
            String description = result.getString( "primaryTitle");
            TupleResult result = new TupleResult(description, "Movie", ID);
            list.add(result);
        }

        System.out.println("Built "+list.size()+" items\n");
        return list;
    }
}
