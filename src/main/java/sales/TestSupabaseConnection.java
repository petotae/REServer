package sales;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestSupabaseConnection is a simple Java application to test the connection
 * to a Supabase PostgreSQL database using JDBC.
 *
 * Make sure to replace the JDBC_URL, JDBC_USER, and JDBC_PASSWORD with your
 * actual Supabase credentials.
 */
public class TestSupabaseConnection {
    /**
     * JDBC URL.
     */
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    /**
     * JDBC User.
     */
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    /**
     * JDBC Pass.
     */
    private static final String JDBC_PASSWORD = "iangortoncsw4530";

    /**
     * Logger for logging messages.
     */
    private static Logger logger = LogManager.getLogger(TestSupabaseConnection.class.getName());

    /**
     * Main method to run the connection test.
     * It attempts to connect to the Supabase database and logs the result.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        debug("Opening Supabase connection… ");
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Optional: check validity
            if (conn.isValid(5)) {
                debug("Supabase connection established successfully.");
            } else {
                debug("Supabase connection established, but isValid() returned false.");
            }
        } catch (SQLException e) {
            debug("Failed to connect to Supabase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Logs a debug message if debug logging is enabled.
     *
     * @param msg the message to log
     */
    private static void debug(final String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }
}