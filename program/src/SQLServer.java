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
    public void connectTo(String address, int port, String database, String user, String pass) throws SQLException{
        dataSource = new MysqlDataSource();
        dataSource.setPort(port); // could also be 3306
        dataSource.setUseSSL(false);
        dataSource.setDatabaseName(database);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
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
    public ArrayList<TupleResult> simpleRead(String query) throws SQLException {

        System.out.println( "Building Unique Set . . ." );
        stmt = (Statement) conn.createStatement();
        result = stmt.executeQuery( "SELECT * FROM production WHERE primaryTitle LIKE '%"+query+"%' LIMIT 50;" );

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
