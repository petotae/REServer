package properties;

import properties.util.CaseConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.apache.logging.log4j.LogManager;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class PropertyDAO {
    // private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
    // private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
    // private static final String JDBC_PASSWORD = "iangortoncsw4530";

    // private static final String LIMIT_RECORDS = " LIMIT 100"; // Only show first 100 properties matching query
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


        for (int i = 0; i < fields.length; i++) {
            PropertyDataField f = 
            Document insertDoc = new Document(CaseConverter.camelToSnake(f.name().toLowerCase()), )
        }
        // Document insertDoc = new Document("property_id",  "download_date", "council_name", "purchase_price", "address", "post_code", "property_type", "strata_lot_number",
        //                 "property_name", "area", "area_type", "contract_date", "settlement_date", "zoning", "nature_of_property", "primary_purpose", "legal_description");

        // MongoCollection<Document> collection = this.getCollection();
        
        // PreparedStatement stmt = getCollection(sql);

        for (final PropertyDataField f : fields) {
            final String propName = CaseConverter.snakeToCamel(f.name().toLowerCase());
            final Object value = props.get(propName);
            stmt.setObject(f.getIndex(), value);
        }

        return stmt.executeUpdate() > 0;
    }

    private MongoCollection<Document> getCollection() throws SQLException {
        // java.util.Properties p = new java.util.Properties();
        // p.setProperty("database", "<databaseName>");
        // return DriverManager.getConnection("<connectionString>", p);


        // Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        // PreparedStatement stmt = conn.prepareStatement(sql);
        // return stmt;

        String uri = "mongodb+srv://vspillai02:cs4530exercise1@cs4530exercise1.a7aa12o.mongodb.net/?retryWrites=true&w=majority&appName=cs4530exercise1";
        ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();
        final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(uri))
            .serverApi(serverApi)
            .build();

        try {
            MongoClient mongoClient = MongoClients.create(settings);
            final MongoDatabase database = mongoClient.getDatabase("homesale");
            return database.getCollection("sales");
        } catch (MongoException e) {
            throw new MongoException("Failed to connect to MongoDB", e);
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

        try {
            MongoCollection<Document> collection = this.getCollection();
            // final PreparedStatement stmt = this.getConnection(sql + LIMIT_RECORDS);
            
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

    public void updateAccessData(final String param, final String paramVal) throws SQLException {
        // String sqlGet = String.format("SELECT * FROM nsw_property_data_access_data WHERE attribute_type = \'%s\' AND value = %s;", param, paramVal);
        
        // MongoCollection<Document> collection = this.getCollection();
        // final PreparedStatement stmtGet = this.getConnection(sqlGet);
        // final ResultSet resSet = stmtGet.executeQuery();

        // String sqlPost = "";
        // int count = 0;
        // while (resSet.next()) {
        //     sqlPost = String.format("UPDATE nsw_property_data_access_data SET access_count = %d WHERE attribute_type = '%s' AND value = %s", resSet.getLong("access_count") + 1, param, paramVal);
        //     count++;
        // }
        // if (count == 0) {
        //     sqlPost = String.format("INSERT INTO nsw_property_data_access_data (attribute_type, value, access_count) VALUES ('%s', %s, 1)", param, paramVal);
        // }
        // PreparedStatement stmtPost = this.getConnection(sqlPost);
        // try {
        //     stmtPost.executeQuery();
        // } catch (SQLException e) {
        //     if (!e.getMessage().contains("No results were returned by the query")) {
        //         throw new SQLException();
        //     }
        // }
    }


    public List<Property> getAllProps() throws SQLException {
        final List<Property> results = new ArrayList<>();

        try {
            MongoCollection<Document> collection = this.getCollection();
            FindIterable<Document> docs = collection.find(Filters.empty());

            for (Document doc : docs) {
                final Property prop = docToProp(doc);
                results.add(prop);
            }
        } catch (SQLException e) {
            this.debug("Database error: " + e.getMessage());
        }

        return results;
    }

    private Property docToProp(Document doc) {
        final Property prop = new Property(
            doc.getLong("property_id"),
            doc.getString("download_date"),
            doc.getString("council_name"),
            doc.getLong("purchase_price"),
            doc.getString("address"),
            doc.getLong("post_code"),
            doc.getString("property_type"),
            doc.getString("strata_lot_number"),
            doc.getString("property_name"),
            doc.getDouble("area"),
            doc.getString("area_type"),
            doc.getString("contract_date"),
            doc.getString("settlement_date"),
            doc.getString("zoning"),
            doc.getString("nature_of_property"),
            doc.getString("primary_purpose"),
            doc.getString("legal_description"));
        return prop;
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

        PreparedStatement stmt = this.getConnection(sql);

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

    // public int getParamAccessCount(String param, String paramVal) throws SQLException, IllegalArgumentException {
    //     String sql = "SELECT access_count FROM nsw_property_data_access_data";
    //     // switch (param) {
    //     //     case "property_id":
    //     //         sql += "nsw_property_id_access_data";
    //     //         break;
    //     //     case "post_code":
    //     //         sql += "nsw_post_code_access_data";
    //     //         break;
    //     //     default:
    //     //         throw new IllegalArgumentException();
    //     // }

    //     // sql += String.format(" WHERE %s = %s;", param, paramVal);

    //     // try {
    //     //     Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    //     //     PreparedStatement stmt = conn.prepareStatement(sql);
    //     //     return stmt.executeQuery();
    //     // } catch (SQLException)
    // }

    private void debug(final String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }
}