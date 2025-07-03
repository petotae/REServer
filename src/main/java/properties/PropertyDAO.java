package properties;

import properties.util.CaseConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyDAO {
    private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    private static final String JDBC_PASSWORD = "iangortoncsw4530";
    private static final String LIMIT_RECORDS = " LIMIT 100"; // Only show first 100 properties matching query
    private static final List<String> LONG_ATTRIBUTES = Arrays
            .asList(new String[] { "property_id", "purchase_price", "post_code" });
    private static final List<String> DOUBLE_ATTRIBUTES = Arrays.asList(new String[] { "area" });

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public boolean createProp(Property property) throws SQLException {
        Map<String, Object> props = MAPPER.convertValue(
                property,
                new TypeReference<Map<String, Object>>() {
                });
        props.remove("class");

        System.out.println(props);

        PropertyDataField[] fields = PropertyDataField.values();
        String columnList = Arrays.stream(fields)
                .map(f -> CaseConverter.camelToSnake(f.name().toLowerCase()))
                .collect(Collectors.joining(", "));
        String placeholders = String.join(", ",
                Collections.nCopies(fields.length, "?"));

        String sql = "INSERT INTO nsw_property_data (" + columnList + ") VALUES (" + placeholders + ")";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (PropertyDataField f : fields) {
                String propName = CaseConverter.snakeToCamel(f.name().toLowerCase());
                Object value = props.get(propName);
                stmt.setObject(f.getIndex(), value);
            }

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Property> getPropByParam(String param, Object paramVal) throws SQLException {
        PropertyDataField field = PropertyDataField.valueOf(param.toUpperCase());
        String column = field.name().toLowerCase();
        String sql = "SELECT * FROM nsw_property_data WHERE " + column + " = ?";
        return this.getPropertiesFromDatabase(sql, column, paramVal);
    }

    public List<Property> getAllProperties() throws SQLException {
        return this.getPropertiesFromDatabase("SELECT * FROM nsw_property_data", null, null);
    }

    public List<Property> getPropertiesGreaterThanLessThan(String param, Object paramVal, Boolean isGreaterThan)
            throws SQLException {
        PropertyDataField field = PropertyDataField.valueOf(param.toUpperCase());
        String column = field.name().toLowerCase();
        String sql = String.format("SELECT * FROM nsw_property_data WHERE %s %s ?", column, isGreaterThan ? ">" : "<");
        return this.getPropertiesFromDatabase(sql, column, paramVal);
    }

    private List<Property> getPropertiesFromDatabase(String sql, String column, Object paramVal) throws SQLException {
        List<Property> results = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql + LIMIT_RECORDS);
            if (column != null) {
                case POST_CODE:
                if (LONG_ATTRIBUTES.contains(column)) {
                    // parse the incoming String (e.g. "1234") to long
                    long longVal = Long.parseLong(paramVal.toString());
                    stmt.setLong(1, longVal);
                } else if (DOUBLE_ATTRIBUTES.contains(column)) {
                    // parse the incoming String (e.g. "12.34") to double
                    Double doubleVal = Double.parseDouble(paramVal.toString());
                    stmt.setDouble(1, doubleVal);
                } else {
                    stmt.setString(1, paramVal.toString());
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Property p = new Property(
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
                        rs.getString("legal_description"));
                results.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return results;
    }

    public List<Property> getAllProps() throws SQLException {
        String sql = "SELECT * FROM nsw_property_data";
        List<Property> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Property p = new Property(
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
                        rs.getString("legal_description"));
                results.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return results;
    }

    public double getAverageOfField(List<Property> properties, String param) throws SQLException {
        List<Number> values = properties.stream()
            .map(p -> p.get(param))  
            .filter(val -> val instanceof Number)
            .map(val -> (Number) val)
            .collect(Collectors.toList());

        return values.stream()
            .mapToDouble(Number::doubleValue)
            .average()
            .orElse(0.0);
    }



    public List<Property> getPropByParams(Map<String, List<String>> filters) throws SQLException {
        String sql = "SELECT * FROM nsw_property_data";
        List<Object> paramVals = new ArrayList<>();
        List<String> paramKeys = new ArrayList<>();

        if (!filters.isEmpty()) {
            sql += " WHERE ";
            int keyIndex = 0;
            int numKeys = filters.size();

            for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();

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
                String field = paramKeys.get(i).toLowerCase();
                Object paramVal = paramVals.get(i);

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

            try (ResultSet rs = stmt.executeQuery()) {
                List<Property> results = new ArrayList<>();
                while (rs.next()) {
                    Property p = new Property(
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
                            rs.getString("legal_description"));
                    results.add(p);
                }
                return results;
            }
        }
    }
}