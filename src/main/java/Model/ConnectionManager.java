package Model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.DriverManager;

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

        // Allow overriding DB connection via system properties or environment variables for tests
        String url = System.getProperty("db.url");
        if (url == null || url.isEmpty()) {
            url = System.getenv("DB_URL");
        }
        if (url == null || url.isEmpty()) {
            url = DB_URL;
        }
        String user = System.getProperty("db.user");
        if (user == null || user.isEmpty()) {
            user = System.getenv("DB_USER");
        }
        if (user == null || user.isEmpty()) {
            user = DB_USER;
        }
        String pass = System.getProperty("db.pass");
        if (pass == null) {
            pass = System.getenv("DB_PASSWORD");
        }
        if (pass == null) {
            pass = DB_PASSWORD;
        }

        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        
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
        // Allow tests to override with an in-memory DB (e.g., H2) via system properties or env
        String overrideUrl = System.getProperty("db.url");
        if (overrideUrl == null || overrideUrl.isEmpty()) {
            overrideUrl = System.getenv("DB_URL");
        }
        if (overrideUrl != null && !overrideUrl.isEmpty() && overrideUrl.startsWith("jdbc:")) {
            String u = System.getProperty("db.user");
            if (u == null) u = System.getenv("DB_USER");
            String p = System.getProperty("db.pass");
            if (p == null) p = System.getenv("DB_PASSWORD");
            return DriverManager.getConnection(overrideUrl, (u != null ? u : ""), (p != null ? p : ""));
        }
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
            java.sql.Statement stmt = conn.createStatement();

            boolean errorOccurred = false;

            // Read the schema.sql file
            InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("database/schema.sql");
            if (inputStream == null) {
                System.err.println("[DB_INIT] Could not find schema.sql file");
            } else {
                // Execute the SQL script statement-by-statement, never abort the whole init
                java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter(";");
                while (scanner.hasNext()) {
                    String sql = scanner.next().trim();
                    if (sql.isEmpty()) continue;
                    try {
                        stmt.execute(sql);
                    } catch (SQLException e) {
                        errorOccurred = true;
                        String snippet = sql.replaceAll("\n", " ");
                        if (snippet.length() > 180) snippet = snippet.substring(0, 180) + "...";
                        System.err.println("[DB_INIT] Failed to execute SQL: " + snippet + " | cause=" + e.getMessage());
                    }
                }
            }

            // Safety migrations — must run regardless of previous errors
            // Ensure matches.created_by exists (for legacy DBs)
            try { stmt.execute("ALTER TABLE matches ADD COLUMN created_by INT NULL"); } catch (SQLException ignore) {}
            try { stmt.execute("ALTER TABLE matches ADD CONSTRAINT fk_matches_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL"); } catch (SQLException ignore) {}

            // Ensure tournaments.created_by exists (for legacy DBs)
            try { stmt.execute("ALTER TABLE tournaments ADD COLUMN created_by INT NULL"); } catch (SQLException ignore) {}
            try { stmt.execute("ALTER TABLE tournaments ADD CONSTRAINT fk_tournaments_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL"); } catch (SQLException ignore) {}

            // Ensure coach.team_name exists (legacy DBs)
            try { stmt.execute("ALTER TABLE coach ADD COLUMN team_name VARCHAR(100) NULL"); } catch (SQLException ignore) {}
            // Ensure player.team_name exists (legacy DBs) — fixes current error path
            try { stmt.execute("ALTER TABLE player ADD COLUMN team_name VARCHAR(100) NULL"); } catch (SQLException ignore) {}

            // Create table for fan followed matches (idempotent)
            try {
                stmt.execute("CREATE TABLE IF NOT EXISTS fan_follow_matches (" +
                        "fan_id INT NOT NULL, " +
                        "match_id INT NOT NULL, " +
                        "PRIMARY KEY (fan_id, match_id), " +
                        "FOREIGN KEY (fan_id) REFERENCES users(id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE)"
                );
            } catch (SQLException e) {
                errorOccurred = true;
                System.err.println("[DB_INIT] Failed to ensure fan_follow_matches table: " + e.getMessage());
            }

            // Create table for fan followed tournaments (idempotent)
            try {
                stmt.execute("CREATE TABLE IF NOT EXISTS fan_follow_tournaments (" +
                        "fan_id INT NOT NULL, " +
                        "tournament_id INT NOT NULL, " +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "PRIMARY KEY (fan_id, tournament_id), " +
                        "FOREIGN KEY (fan_id) REFERENCES users(id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE)"
                );
            } catch (SQLException e) {
                errorOccurred = true;
                System.err.println("[DB_INIT] Failed to ensure fan_follow_tournaments table: " + e.getMessage());
            }

            // Safety fix: correct FK on matches.tournament_id to reference tournaments(id) (some DBs had wrong FK)
            try { stmt.execute("ALTER TABLE matches DROP FOREIGN KEY matches_ibfk_1"); } catch (SQLException ignore) {}
            try { stmt.execute("ALTER TABLE matches MODIFY COLUMN tournament_id INT NULL"); } catch (SQLException ignore) {}
            try { stmt.execute("ALTER TABLE matches ADD CONSTRAINT fk_matches_tournament FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE SET NULL"); } catch (SQLException ignore) {}

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
                    System.out.println("[DB_INIT] Seeded sample tournaments (table was empty)");
                }
            } catch (SQLException e) {
                errorOccurred = true;
                System.err.println("[DB_INIT] Tournament seeding skipped due to SQL error: " + e.getMessage());
            }

            if (errorOccurred) {
                System.out.println("[DB_INIT] Database initialization completed with warnings (some statements failed, but safety migrations ran)");
            } else {
                System.out.println("[DB_INIT] Database schema initialized successfully");
            }
        } catch (SQLException e) {
            System.err.println("[DB_INIT] Fatal error during DB initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}