package sales;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestSupabaseConnection {
    // copy/paste your same JDBC_URL, USER, PASSWORD here
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    private static final String JDBC_PASSWORD = "iangortoncsw4530";

    public static void main(String[] args) {
        System.out.print("Opening Supabase connection… ");
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Optional: check validity
            if (conn.isValid(5)) {
                System.out.println("SUCCESS!");
            } else {
                System.out.println("CONNECTED, but isValid() returned false");
            }
        } catch (SQLException e) {
            System.err.println("FAILED:");
            e.printStackTrace();
        }
    }
}