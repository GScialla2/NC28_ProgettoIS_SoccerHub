package Model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Database initializer that runs when the application starts
 * Initializes the database schema if it doesn't exist
 */
@WebListener
public class DatabaseInitializer implements ServletContextListener {
    
    /**
     * Called when the application is starting
     * @param sce The servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing database...");
        try {
            // Initialize the database schema
            ConnectionManager.initializeDatabase();
            System.out.println("Database initialization completed successfully");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Called when the application is shutting down
     * @param sce The servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing to do here
    }
}