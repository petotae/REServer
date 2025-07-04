package properties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Controller to create properties and get information about properties in the database
 */
public class PropertyController {
    private final PropertyDAO propertydao = new PropertyDAO();

    /**
     * Set the endpoints to post data to the database or get data from the database
     * @param app The current Javalin app
     */
    public void registerRoutes(final Javalin app) {
        app.post("/createProperty", this::createProperty);
        app.get("/getProperties/{param}/{paramVal}", this::findPropertyByParam);
        app.get("/getAllProperties", this::getAllProperties);
        app.get("/getPropertiesByParams", this::getPropertiesByParams);
        app.get("/getPropertiesGreaterThan/{param}/{paramVal}", this::findPropertiesGreaterThan);
        app.get("/getPropertiesLessThan/{param}/{paramVal}", this::findPropertiesLessThan);
        app.get("/getAveragePurchasePrice/{param}/{paramval}", this::getAvgPurchasePrice);
    }

    /**
     * Create a property from information given in the HTTP json body and store it in the database
     * @param ctx The HTTP request
     */
    public void createProperty(final Context ctx) {
        try {
            // Extract HomeSale from request body
            final Property prop = ctx.bodyValidator(Property.class).get();
            // store new sale in database
            final boolean success = propertydao.createProp(prop);
            if (success) {
                ctx.result("Property Created");
                ctx.status(201);
            } else {
                ctx.result("Failed to add property");
                ctx.status(400);
            }
        } catch (SQLException e) {
            handleError(e, ctx);
        }
    }

    /**
     * Gets all properties where the specified parameter matches the specified value
     * @param ctx The HTTP request
     */
    public void findPropertyByParam(final Context ctx) {
        try {
            final String param = ctx.pathParam("param");
            final String paramVal = ctx.pathParam("paramVal");

            final List<Property> properties = propertydao.getPropByParam(param, paramVal);

            this.addResponseToContext(ctx, properties, properties, "No properties for " + 
                                      param + " with {" + paramVal + "} found");
        } catch (SQLException e) {
            this.handleError(e, ctx);
        }
    }

    /**
     * Gets all properties in the database
     * @param ctx The HTTP request
     */
    public void getAllProperties(final Context ctx) {
        try {
            final List<Property> properties = propertydao.getAllProps();
            this.addResponseToContext(ctx, properties, properties, "No properties found");
        } catch (SQLException e) {
            this.handleError(e, ctx);
        }
    }

    /**
     * Gets all properties where the specified parameter's value is greater than the specified value
     * @param ctx The HTTP request
     */
    public void findPropertiesGreaterThan(final Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, true);
    }

    /**
     * Gets all properties where the specified parameter's value is less than the specified value
     * @param ctx The HTTP request
     */
    public void findPropertiesLessThan(final Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, false);
    }

    /**
     * Gets all properties where the specified parameter's value is greater than or less than 
     * the specified value
     * @param ctx The HTTP request
     * @param isGreaterThan Whether the query should return records where the parameter's value is 
     *                      greater than the specified value
     */
    private void findPropertiesGreaterThanLessThan(final Context ctx, final Boolean isGreaterThan) {
        try {
            final String param = ctx.pathParam("param");
            final String paramVal = ctx.pathParam("paramVal");

            final List<String> acceptedParams = Arrays.asList("property_id", "download_date", "contract_date", "purchase_price",
                             "post_code", "settlement_date", "area");

            List<Property> properties = new ArrayList<>();
            if (acceptedParams.contains(param)) {
                properties = propertydao.getPropertiesGreaterThanLessThan(param, paramVal, isGreaterThan);
            } 
            this.addResponseToContext(ctx, properties, properties, "No properties for " + param + " greater than {" + paramVal + "} found");
        } catch (SQLException e) {
            handleError(e, ctx);
        }
    }

    /**
     * Get all properties where all the specified parameters match the specified values
     * @param ctx The HTTP request
     */
    public void getPropertiesByParams(final Context ctx) {
        try {
            // { "property_id": ["0"], "property_cost": ["10000"] }
            final var paramsMap = ctx.queryParamMap();

            final List<Property> properties = propertydao.getPropByParams(paramsMap);

            this.addResponseToContext(ctx, properties, properties, "No properties with given query vals found");
        } catch (SQLException e) {
            this.handleError(e, ctx);
        }
    }

    /**
     * Gets the average purchase price of properties where the specified parameter has 
     * the specified value
     * @param ctx The HTTP request
     */
    public void getAvgPurchasePrice(final Context ctx) {
        try {
            final String param = ctx.pathParam("param");
            final String paramval = ctx.pathParam("paramval");

            final List<Property> properties = propertydao.getPropByParam(param, paramval);

            double averagePurchasePrice = propertydao.getAverageOfField(properties, "purchasePrice");
            this.addResponseToContext(ctx, properties, averagePurchasePrice, "No properties found");
        } catch (SQLException e) {
            this.handleError(e, ctx);
        }
    }

    /**
     * Adds a response to the HTTP response based on the properties returned by the query
     * @param ctx The HTTP request
     * @param properties The records returned by the SQL query
     * @param messageIfNoProperties The message to be sent if the query returned records 
     * @param messageIfNoProperties The message to be sent if no records were returned by 
     *                              the query
     */
    private void addResponseToContext(final Context ctx, final List<Property> properties, 
                                      final Object messageIfProperties,
                                      final String messageIfNoProperties) {
        if (properties.isEmpty()) {
            ctx.result(messageIfNoProperties);
            ctx.status(404);
        } else {
            ctx.json(messageIfProperties);
            ctx.status(200);
        }
    }

    /**
     * Handles SQLException errors
     * @param e The thrown error
     * @param ctx The HTTP request
     */
    private void handleError(final SQLException e, final Context ctx) {
        e.printStackTrace();
        ctx.result("Database error: " + e.getMessage());
        ctx.status(500);
    }
}
