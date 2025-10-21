package Model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * Connection manager class that implements connection pooling for database access
 */
public class ConnectionManager {
    private static MysqlConnectionPoolDataSource dataSource;
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/soccerhub";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Gabriel82@";
    
    static {
        try {
            // Initialize the data source
            initDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the data source with connection pooling
     */
    private static void initDataSource() {
        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setURL(DB_URL);
        dataSource.setUser(DB_USER);
        dataSource.setPassword(DB_PASSWORD);
        
        // Set additional connection properties
        try {
            dataSource.setCachePrepStmts(true);
            dataSource.setPrepStmtCacheSize(250);
            dataSource.setPrepStmtCacheSqlLimit(2048);
            dataSource.setUseServerPrepStmts(true);
            dataSource.setCreateDatabaseIfNotExist(true);
            dataSource.setCharacterEncoding("UTF-8");
            dataSource.setServerTimezone("UTC");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a connection from the pool
     * @return A database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initDataSource();
        }
        return dataSource.getConnection();
    }
    
    /**
     * Closes a connection and returns it to the pool
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Initializes the database schema if it doesn't exist
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Check if the database exists and create it if it doesn't
            java.sql.Statement stmt = conn.createStatement();
            
            // Read the schema.sql file
            InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("database/schema.sql");
            if (inputStream == null) {
                System.err.println("Could not find schema.sql file");
                return;
            }
            
            // Read the SQL script
            java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter(";");
            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
            
            System.out.println("Database schema initialized successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}