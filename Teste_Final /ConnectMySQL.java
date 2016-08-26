
import java.sql.*;

public class ConnectMySQL  {
    public Connection conn;
    private static ConnectMySQL instance;

    // Initializes singleton: connect to MySQL database
    private ConnectMySQL() {

        // JDBC driver name and database URL
        String driverName = "com.mysql.jdbc.Driver";
        String serverName = "150.164.10.32:3306";
        String myDatabase = "maniot";
        String url = "jdbc:mysql://localhost:3306/maniot";
        // Database credentials
        String username = "user";
        String password = "password";
        try {
            // Register JDBC driver
            Class.forName(driverName);
            // Open a connection
            conn = DriverManager.getConnection(url, username, password);
            // Driver not found
        } catch (ClassNotFoundException cnfex) {
            System.out.println(cnfex.getMessage());
            // Not able to connect to the database
        } catch (SQLException sqlex) {
            System.out.println(sqlex.getMessage());
            // Other error
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Return a MySQL connection object
    public static ConnectMySQL getInstance() {
        if (instance == null)
            instance = new ConnectMySQL();
        return instance;
    }

    // Close the database connection
    public static int closeConnection(Connection connection) {
        try {
            connection.close();
            return 1;
        } catch (SQLException sqlex) {
            System.out.println(sqlex.getMessage());
            return 0;
        }
    }
}
