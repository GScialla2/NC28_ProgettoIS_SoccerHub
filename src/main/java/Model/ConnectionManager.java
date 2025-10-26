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

            // Post-initialization safety migration: ensure matches.created_by exists (for legacy DBs)
            try {
                stmt.execute("ALTER TABLE matches ADD COLUMN created_by INT NULL");
            } catch (SQLException ignore) { /* column might already exist */ }
            try {
                stmt.execute("ALTER TABLE matches ADD CONSTRAINT fk_matches_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL");
            } catch (SQLException ignore) { /* FK might already exist */ }

            // Safety migration: ensure tournaments.created_by exists as well (for legacy DBs)
            try {
                stmt.execute("ALTER TABLE tournaments ADD COLUMN created_by INT NULL");
            } catch (SQLException ignore) { /* column might already exist */ }
            try {
                stmt.execute("ALTER TABLE tournaments ADD CONSTRAINT fk_tournaments_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL");
            } catch (SQLException ignore) { /* FK might already exist */ }

            // Ensure there are some tournaments to display pre-login.
            try {
                int cnt = 0;
                try (java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM tournaments")) {
                    if (rs.next()) {
                        cnt = rs.getInt("cnt");
                    }
                }
                if (cnt == 0) {
                    // Seed a few example tournaments (idempotent since only when empty)
                    stmt.executeUpdate("INSERT INTO tournaments (name, type, trophy, team_count, match_count, start_date, end_date, location, description, category, status, created_by) VALUES (" +
                            "'Coppa Italia 2025', 'A eliminazione diretta', 'Coppa Italia', 16, 15, '2025-11-01', '2026-02-28', 'Italia', 'Coppa nazionale a eliminazione diretta', 'Professional', 'upcoming', NULL)");
                    stmt.executeUpdate("INSERT INTO tournaments (name, type, trophy, team_count, match_count, start_date, end_date, location, description, category, status, created_by) VALUES (" +
                            "'Torneo Under 18 Invernale', 'A gironi', 'Coppa Primavera', 12, 30, '2025-12-10', '2026-03-15', 'Italia', 'Torneo giovanile invernale a gironi', 'Giovanili', 'upcoming', NULL)");
                    stmt.executeUpdate("INSERT INTO tournaments (name, type, trophy, team_count, match_count, start_date, end_date, location, description, category, status, created_by) VALUES (" +
                            "'Summer Cup 2025', 'Misto', 'Summer Cup', 8, 16, '2025-07-01', '2025-08-01', 'Europa', 'Torneo estivo amatoriale', 'Amatoriale', 'completed', NULL)");
                    System.out.println("Seeded sample tournaments (table was empty)");
                }
            } catch (SQLException e) {
                System.err.println("Tournament seeding skipped due to SQL error: " + e.getMessage());
            }
            
            System.out.println("Database schema initialized successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}