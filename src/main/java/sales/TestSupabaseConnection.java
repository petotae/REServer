package sales;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestSupabaseConnection {
    // copy/paste your same JDBC_URL, USER, PASSWORD here
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    private static final String JDBC_PASSWORD = "iangortoncsw4530";

    private static Logger logger = LogManager.getLogger(TestSupabaseConnection.class.getName());

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
            logger.debug("Failed to connect to Supabase: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }
}