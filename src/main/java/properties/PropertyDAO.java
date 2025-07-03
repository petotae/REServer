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

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public boolean newProperty(Property property) throws SQLException {
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

    public List<Property> getPropertiesByField(String param, Object paramVal) throws SQLException {
        PropertyDataField field = PropertyDataField.valueOf(param.toUpperCase());
        String column = field.name().toLowerCase();

        String sql = "SELECT * FROM nsw_property_data WHERE " + column + " = ?";

        return this.getPropertiesFromDatabase(sql, field, paramVal);
    }

    public List<Property> getAllProperties() throws SQLException {
        return this.getPropertiesFromDatabase("SELECT * FROM nsw_property_data", null, null);
    }

    public List<Property> getPropertiesGreaterThan(String param, Object paramVal) throws SQLException {
        PropertyDataField field = PropertyDataField.valueOf(param.toUpperCase());
        String column = field.name().toLowerCase();

        String sql = "SELECT * FROM nsw_property_data WHERE " + column + " > ?";

        return this.getPropertiesFromDatabase(sql, field, paramVal);
    }

    private List<Property> getPropertiesFromDatabase(String sql, PropertyDataField field, Object paramVal) throws SQLException {
        List<Property> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (field != null) {
                switch (field) {
                    case PROPERTY_ID:
                    case PURCHASE_PRICE:
                        // parse the incoming String (e.g. "1234") to long
                        long longVal = Long.parseLong(paramVal.toString());
                        stmt.setLong(1, longVal);
                        break;
                    default:
                        stmt.setString(1, paramVal.toString());
                }
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        }

        return results;
    }
}
