package Backend;

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
    public SQLServer(){
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
            TupleResult result = new TupleResult(description, "Movie", ID) {
            };
            list.add(result);
        }

        System.out.println("Built "+list.size()+" items\n");
        return list;
    }

    /**
     * A modified method for obtaining various entertainment results.
     * @param query - The search statement.
     * @return - A set of 50 elements that best match a user's search result.
     * @throws SQLException
     */
    public ArrayList<ProductionResult> productionRead(String query) throws SQLException {

        System.out.println( "Building Unique Set . . ." );
        stmt = (Statement) conn.createStatement();
        result = stmt.executeQuery( "SELECT * FROM production WHERE primaryTitle LIKE '%"+query+"%' LIMIT 50;" );

        ArrayList<ProductionResult> list = new ArrayList<>();
        while ( result.next() ) {
            String type = "Movie";
            String ID = result.getString( "prodID" );
            String description = result.getString( "primaryTitle");
            String originalTitle = result.getString("originalTitle");
            int isAdult = result.getInt("isAdult");
            int startYear = result.getInt("startYear");
            int endYear = result.getInt("endYear");
            int runTime = result.getInt("runTime");
            ProductionResult result = new ProductionResult(ID, type, description, originalTitle,
                    isAdult, startYear, endYear, runTime);
            list.add(result);
        }

        System.out.println("Built "+list.size()+" items\n");
        return list;
    }

    public String executeStatement(String executableStatement, String[] columns, String information) throws SQLException {
        stmt = (Statement) conn.createStatement();

        result = stmt.executeQuery(executableStatement);

        return "";
    }
}
