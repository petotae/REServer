package sales;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    private static final String JDBC_PASSWORD = "iangortoncsw4530";

    public boolean newSale(HomeSale homeSale) throws SQLException {
        String sql = "INSERT INTO nsw_property_data (property_id, download_date, council_name, purchase_price, address, post_code, property_type, strata_lot_number, property_name, area, area_type, contract_date, settlement_date, zoning, nature_of_property, primary_purpose, legal_description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, homeSale.getPropertyId());
            stmt.setString(2, homeSale.getDownloadDate());
            stmt.setString(3, homeSale.getCouncilName());
            stmt.setLong(4, homeSale.getPurchasePrice());
            stmt.setString(5, homeSale.getAddress());
            stmt.setLong(6, homeSale.getPostCode());
            stmt.setString(7, homeSale.getPropertyType());
            stmt.setString(8, homeSale.getStrataLotNumber());
            stmt.setString(9, homeSale.getPropertyName());
            stmt.setDouble(10, homeSale.getArea());
            stmt.setString(11, homeSale.getAreaType());
            stmt.setString(12, homeSale.getContractDate());
            stmt.setString(13, homeSale.getSettlementDate());
            stmt.setString(14, homeSale.getZoning());
            stmt.setString(15, homeSale.getNatureOfProperty());
            stmt.setString(16, homeSale.getPrimaryPurpose());
            stmt.setString(17, homeSale.getLegalDescription());

            stmt.executeUpdate();
            return true;
        }
    }

    public HomeSale getSaleById(long saleID) throws SQLException {
        String sql = "SELECT * FROM nsw_property_data WHERE property_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, saleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractHomeSaleFromResultSet(rs);
            }
        }
        return null;
    }

    public List<HomeSale> getSalesByPostCode(int postCode) throws SQLException {
        String sql = "SELECT * FROM nsw_property_data WHERE post_code = ?";
        List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(extractHomeSaleFromResultSet(rs));
            }
        }
        return sales;
    }

    public List<Long> getAllSalePrices() throws SQLException {
        String sql = "SELECT purchase_price FROM nsw_property_data";
        List<Long> prices = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                prices.add(rs.getLong("purchase_price"));
            }
        }
        return prices;
    }

    public List<HomeSale> getAllSales() throws SQLException {
        String sql = "SELECT * FROM nsw_property_data";
        List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(extractHomeSaleFromResultSet(rs));
            }
        }
        return sales;
    }

    private HomeSale extractHomeSaleFromResultSet(ResultSet rs) throws SQLException {
        return new HomeSale(
            rs.getLong("property_id"),
            rs.getString("download_date"),
            rs.getString("council_name"),
            rs.getLong("purchase_price"),
            rs.getString("address"),
            rs.getLong("post_code"),
            rs.getString("property_type"),
            rs.getString("strata_lot_number"),
            rs.getString("property_name"),
            rs.getDouble("area"),
            rs.getString("area_type"),
            rs.getString("contract_date"),
            rs.getString("settlement_date"),
            rs.getString("zoning"),
            rs.getString("nature_of_property"),
            rs.getString("primary_purpose"),
            rs.getString("legal_description")
        );
    }
}
