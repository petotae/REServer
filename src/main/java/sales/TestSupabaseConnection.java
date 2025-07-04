package sales;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class TestSupabaseConnection {
    // copy/paste your same JDBC_URL, USER, PASSWORD here
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    private static final String JDBC_PASSWORD = "iangortoncsw4530";

    private static Logger logger = Logger.getLogger(TestSupabaseConnection.class.getName());

    public static void main(String[] args) {
        logger.fine("Opening Supabase connection… ");
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Optional: check validity
            if (conn.isValid(5)) {
                logger.fine ("Supabase connection established successfully.");
            } else {
                logger.fine( "Supabase connection established, but isValid() returned false.");
            }
        } catch (SQLException e) {
            logger.severe("Failed to connect to Supabase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}