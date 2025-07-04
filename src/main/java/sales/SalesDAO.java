package sales;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SalesDAO provides methods to interact with the sales database.
 * It allows for creating new sales, retrieving sales by ID or postcode,
 * and fetching all sales or sale prices.
 */
public class SalesDAO {

    /**
     * JDBC URL
     */
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    /**
     * JDBC User
     */
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    /**
     * JDBC Pass
     */
    private static final String JDBC_PASSWORD = "iangortoncsw4530";

    /**
     * Inserts a new home sale into the database.
     *
     * @param homeSale the HomeSale object containing sale details
     * @return true if the sale was successfully added, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean newSale(final HomeSale homeSale) throws SQLException {
        final String sql = "INSERT INTO nsw_property_data (property_id, download_date, council_name, purchase_price, address, post_code, property_type, strata_lot_number, property_name, area, area_type, contract_date, settlement_date, zoning, nature_of_property, primary_purpose, legal_description) " +
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

    /**
     * Retrieves a home sale by its ID.
     *
     * @param saleID the ID of the sale to retrieve
     * @return the HomeSale object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public HomeSale getSaleById(final long saleID) throws SQLException {
        final String sql = "SELECT * FROM nsw_property_data WHERE property_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, saleID);
            final ResultSet resSet = stmt.executeQuery();
            if (resSet.next()) {
                return extractHomeSaleFromResultSet(resSet);
            }
        }
        return null;
    }

    /**
     * Retrieves all home sales for a given postcode.
     *
     * @param postCode the postcode to filter sales by
     * @return a list of HomeSale objects for the specified postcode
     * @throws SQLException if a database access error occurs
     */
    public List<HomeSale> getSalesByPostCode(final int postCode) throws SQLException {
        final String sql = "SELECT * FROM nsw_property_data WHERE post_code = ?";
        final List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postCode);
            final ResultSet resSet = stmt.executeQuery();
            while (resSet.next()) {
                sales.add(extractHomeSaleFromResultSet(resSet));
            }
        }
        return sales;
    }

    /**
     * Retrieves all sale prices from the database.
     *
     * @return a list of sale prices
     * @throws SQLException if a database access error occurs
     */
    public List<Long> getAllSalePrices() throws SQLException {
        final String sql = "SELECT purchase_price FROM nsw_property_data";
        final List<Long> prices = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet resSet = stmt.executeQuery(sql)) {
            while (resSet.next()) {
                prices.add(resSet.getLong("purchase_price"));
            }
        }
        return prices;
    }

/**
     * Retrieves all home sales from the database.
     *
     * @return a list of HomeSale objects
     * @throws SQLException if a database access error occurs
     */
    public List<HomeSale> getAllSales() throws SQLException {
        final String sql = "SELECT * FROM nsw_property_data";
        final List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet resSet = stmt.executeQuery(sql)) {
            while (resSet.next()) {
                sales.add(extractHomeSaleFromResultSet(resSet));
            }
        }
        return sales;
    }

    private HomeSale extractHomeSaleFromResultSet(final ResultSet resSet) throws SQLException {
        return new HomeSale(
            resSet.getLong("property_id"),
            resSet.getString("download_date"),
            resSet.getString("council_name"),
            resSet.getLong("purchase_price"),
            resSet.getString("address"),
            resSet.getLong("post_code"),
            resSet.getString("property_type"),
            resSet.getString("strata_lot_number"),
            resSet.getString("property_name"),
            resSet.getDouble("area"),
            resSet.getString("area_type"),
            resSet.getString("contract_date"),
            resSet.getString("settlement_date"),
            resSet.getString("zoning"),
            resSet.getString("nature_of_property"),
            resSet.getString("primary_purpose"),
            resSet.getString("legal_description")
        );
    }
}
