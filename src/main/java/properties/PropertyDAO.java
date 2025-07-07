package properties;

import properties.util.CaseConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class PropertyDAO {
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    private static final String JDBC_PASSWORD = "iangortoncsw4530";
    private static final String LIMIT_RECORDS = " LIMIT 100"; // Only show first 100 properties matching query
    private static final List<String> LONG_ATTRIBUTES = Arrays
            .asList("property_id", "purchase_price", "post_code");
    private static final List<String> DOUBLE_ATTRIBUTES = Arrays.asList("area");

    private static final ObjectMapper MAPPER = new ObjectMapper();

    Logger logger = LogManager.getLogger(PropertyDAO.class.getName());

    public boolean createProp(final Property property) throws SQLException {
        final Map<String, Object> props = MAPPER.convertValue(
                property,
                new TypeReference<Map<String, Object>>() {
                });
        props.remove("class");
        
        this.debug("Creating property: " + props.toString());

        final PropertyDataField[] fields = PropertyDataField.values();
        final String columnList = Arrays.stream(fields)
                .map(f -> CaseConverter.camelToSnake(f.name().toLowerCase()))
                .collect(Collectors.joining(", "));
        final String placeholders = String.join(", ",
                Collections.nCopies(fields.length, "?"));

        final String sql = "INSERT INTO nsw_property_data (" + columnList + ") VALUES (" + placeholders + ")";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (final PropertyDataField f : fields) {
                final String propName = CaseConverter.snakeToCamel(f.name().toLowerCase());
                final Object value = props.get(propName);
                stmt.setObject(f.getIndex(), value);
            }

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Property> getPropByParam(final String param, final Object paramVal) throws SQLException {
        final PropertyDataField field = PropertyDataField.valueOf(param.toUpperCase());
        final String column = field.name().toLowerCase();
        final String sql = "SELECT * FROM nsw_property_data WHERE " + column + " = ?";
        return this.getPropertiesFromDatabase(sql, column, paramVal);
    }

    public List<Property> getAllProperties() throws SQLException {
        return this.getPropertiesFromDatabase("SELECT * FROM nsw_property_data", null, null);
    }

    public List<Property> getPropertiesGreaterThanLessThan(final String param, final Object paramVal, final Boolean isGreaterThan)
            throws SQLException {
        final PropertyDataField field = PropertyDataField.valueOf(param.toUpperCase());
        final String column = field.name().toLowerCase();
        final String sql = String.format("SELECT * FROM nsw_property_data WHERE %s %s ?", column, isGreaterThan ? ">" : "<");
        return this.getPropertiesFromDatabase(sql, column, paramVal);
    }

    private List<Property> getPropertiesFromDatabase(final String sql, final String column, final Object paramVal) throws SQLException {
        final List<Property> results = new ArrayList<>();

        updateAccessDataTables(sql);

        try {
            final Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            final PreparedStatement stmt = conn.prepareStatement(sql + LIMIT_RECORDS);
            if (column != null) {
                if (LONG_ATTRIBUTES.contains(column)) {
                    // parse the incoming String (e.g. "1234") to long
                    final long longVal = Long.parseLong(paramVal.toString());
                    stmt.setLong(1, longVal);
                } else if (DOUBLE_ATTRIBUTES.contains(column)) {
                    // parse the incoming String (e.g. "12.34") to double
                    final Double doubleVal = Double.parseDouble(paramVal.toString());
                    stmt.setDouble(1, doubleVal);
                } else {
                    stmt.setString(1, paramVal.toString());
                }
            }

            final ResultSet resSet = stmt.executeQuery();
            while (resSet.next()) {
                final Property prop = new Property(
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
                        resSet.getString("legal_description"));
                results.add(prop);
            }
        } catch (SQLException e) {
            this.debug("Database error: " + e.getMessage());
        }
        return results;
    }

    private void updateAccessDataTables(final String sql) {
        // Need new 2 tables "access property_id table" with two columns: "property_id" "count" 
        // and "access post_code table" with two columns: "post_code" "count"
        // You can't add post_code access #s to every property with that post code
        if (isSearchingOnAttribute(sql, "property_id")) {
            // If property_id exists in "access property_id table"
                // count++
            // Else
                // add property_id to table w/ count 1
        } else if (isSearchingOnAttribute(sql, "post_code")) {
            // Same idea as above
        }
    }

    private boolean isSearchingOnAttribute(final String sql, final String attribute) {
        return sql.contains("WHERE " + attribute) || sql.contains("AND " + attribute) || sql.contains("OR " + attribute);
    }

    public List<Property> getAllProps() throws SQLException {
        final String sql = "SELECT * FROM nsw_property_data";
        final List<Property> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet resSet = stmt.executeQuery()) {

            while (resSet.next()) {
                final Property prop = new Property(
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
                        resSet.getString("legal_description"));
                results.add(prop);
            }
        } catch (SQLException e) {
            this.debug("Database error: " + e.getMessage());
        }

        return results;
    }

    public double getAverageOfField(final List<Property> properties, final String param) throws SQLException {
        final List<Number> values = properties.stream()
            .map(p -> (Number) p.get(param))  
            .filter(val -> val instanceof Number)
            .map(val -> (Number) val)
            .collect(Collectors.toList());

        return values.stream()
            .mapToDouble(Number::doubleValue)
            .average()
            .orElse(0.0);
    }



    public List<Property> getPropByParams(final Map<String, List<String>> filters) throws SQLException {
        String sql = "SELECT * FROM nsw_property_data";
        final List<Object> paramVals = new ArrayList<>();
        final List<String> paramKeys = new ArrayList<>();

        if (!filters.isEmpty()) {
            sql += " WHERE ";
            int keyIndex = 0;
            final int numKeys = filters.size();

            for (final Map.Entry<String, List<String>> entry : filters.entrySet()) {
                final String key = entry.getKey();
                final List<String> values = entry.getValue();

                String statement;

                if (values.size() == 1) {
                    statement = key + " = ?";
                    paramVals.add(values.get(0));
                    paramKeys.add(key);
                } else {
                    statement = key + " IN (";
                    for (int i = 0; i < values.size(); i++) {
                        statement += "?";
                        paramVals.add(values.get(i));
                        paramKeys.add(key);
                        if (i != values.size() - 1) {
                            statement += ",";
                        }
                    }
                    statement += ")";
                }
                sql += statement;

                if (++keyIndex < numKeys) {
                    sql += " AND ";
                }
            }
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Fill in ? parameters with correct types
            for (int i = 0; i < paramVals.size(); i++) {
                final String field = paramKeys.get(i).toLowerCase();
                final Object paramVal = paramVals.get(i);

                switch (field) {
                    case "property_id":
                    case "purchase_price":
                    case "post_code":
                        stmt.setLong(i + 1, Long.parseLong(paramVal.toString()));
                        break;
                    case "area":
                        stmt.setDouble(i + 1, Double.parseDouble(paramVal.toString()));
                        break;
                    default:
                        stmt.setString(i + 1, paramVal.toString());
                }
            }

            try (ResultSet resSet = stmt.executeQuery()) {
                final List<Property> results = new ArrayList<>();
                while (resSet.next()) {
                    final Property prop = new Property(
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
                            resSet.getString("legal_description"));
                    results.add(prop);
                }
                return results;
            }
        }
    }

    public int getParamAccessCount(String param, String paramVal) throws SQLException, IllegalArgumentException {
        String sql = "SELECT count FROM ";
        switch (param) {
            case "property_id":
                sql += "nsw_property_id_access_data";
                break;
            case "post_code":
                sql += "nsw_post_code_access_data";
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void debug(final String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }
}