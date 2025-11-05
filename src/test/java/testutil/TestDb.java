package testutil;

import Model.ConnectionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class TestDb {
    private static boolean initialized = false;

    private TestDb() {}

    public static synchronized void init() {
        if (initialized) return;
        try {
            // Default to H2 in-memory if not provided
            if (System.getProperty("db.url") == null && System.getenv("DB_URL") == null) {
                System.setProperty("db.url", "jdbc:h2:mem:soccerhub;MODE=MySQL;DB_CLOSE_DELAY=-1");
                System.setProperty("db.user", "sa");
                System.setProperty("db.pass", "");
            }

            runSqlResource("database/schema-h2.sql");
            runSqlResource("database/seed-h2.sql");
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize H2 test database", e);
        }
    }

    public static void reset() {
        initialized = false;
        init();
    }

    private static void runSqlResource(String resourcePath) throws SQLException {
        try (Connection c = ConnectionManager.getConnection(); Statement st = c.createStatement()) {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                throw new IllegalStateException("Resource not found: " + resourcePath);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                // naive split by semicolon; schema files are simple
                for (String stmt : sb.toString().split(";")) {
                    String sql = stmt.trim();
                    if (sql.isEmpty()) continue;
                    st.execute(sql);
                }
            }
        } catch (SQLException e) {
            throw e;
        } catch (Exception ex) {
            throw new SQLException("Error reading SQL resource: " + resourcePath, ex);
        }
    }
}
