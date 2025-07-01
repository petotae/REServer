package sales;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesDAO {

    // List to hold test data
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    private static final String JDBC_PASSWORD = "iangortoncsw4530";

    public boolean newSale(HomeSale homeSale) throws SQLException {
        String sql = "INSERT INTO nsw_property_data (saleID, postcode, salePrice) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, homeSale.getID());
            stmt.setString(2, homeSale.getPostCode());
            stmt.setString(3, homeSale.getSalePrice());
            stmt.executeUpdate();
            return true;
        }
    }

    // returns Optional wrapping a HomeSale if id is found, empty Optional otherwise
    public Optional<HomeSale> getSaleById(String saleID) throws SQLException {
        String sql = "SELECT * FROM nsw_property_data WHERE property_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, saleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HomeSale sale = new HomeSale(
                        rs.getString("property_id"),
                        rs.getString("post_code"),
                        rs.getString("settlement_date"));
                return Optional.of(sale);
            }
        }
        return Optional.empty();
    }

    // returns a List of homesales in a given postCode
    public List<HomeSale> getSalesByPostCode(String postCode) throws SQLException {
        String sql = "SELECT * FROM homesales WHERE postcode = ?";
        List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, postCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new HomeSale(
                        rs.getString("saleID"),
                        rs.getString("postcode"),
                        rs.getString("salePrice")));
            }
        }
        return sales;
    }

    // returns the individual prices for all sales. Potentially large
    public List<String> getAllSalePrices() throws SQLException {
        String sql = "SELECT salePrice FROM homesales";
        List<String> prices = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                prices.add(rs.getString("salePrice"));
            }
        }
        return prices;
    }

    // returns all home sales. Potentially large
    public List<HomeSale> getAllSales() throws SQLException {
        String sql = "SELECT * FROM homesales";
        List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(new HomeSale(
                        rs.getString("saleID"),
                        rs.getString("postcode"),
                        rs.getString("salePrice")));
            }
        }
        return sales;
    }

}
