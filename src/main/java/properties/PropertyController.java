package properties;

import properties.util.CaseConverter;
import properties.util.ErrorResponse;
import properties.util.CaseConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.openapi.*;

/**
 * Controller to create properties and get information about properties in the
 * database
 */
public class PropertyController {
    private final PropertyDAO propertydao = new PropertyDAO();

    /**
     * Set the endpoints to post data to the database or get data from the database
     * 
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
     * Create a property from information given in the HTTP json body and store it
     * in the database
     * 
     * @param ctx The HTTP request
     */
    @OpenApi(summary = "Create property", operationId = "createProperty", path = "/createProperty", methods = HttpMethod.POST, tags = {
            "Property" }, requestBody = @OpenApiRequestBody(content = {
                    @OpenApiContent(from = Property.class)
            }), responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400", content = { @OpenApiContent(from = ErrorResponse.class) })
            })
    public void createProperty(final Context ctx) {
        try {
            final Property prop = ctx.bodyValidator(Property.class).get();
            final boolean success = propertydao.createProp(prop);

            // update access data
            propertydao.updateAccessData("property_id", prop.getPropertyId().toString());
            propertydao.updateAccessData("post_code", prop.getPostCode().toString());

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
     * 
     * @param ctx The HTTP request
     */
    @OpenApi(summary = "Find properties by parameter", operationId = "findPropertyByParam", path = "/getProperties/{param}/{paramval}", methods = HttpMethod.GET, pathParams = {
            @OpenApiParam(name = "param", type = String.class, description = "Property field to filter by"),
            @OpenApiParam(name = "paramval", type = String.class, description = "Value of the property field")
    }, tags = { "Property" }, responses = {
            @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
            @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
            @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
    })
    public void findPropertyByParam(final Context ctx) {
        try {
            final String param = ctx.pathParam("param");
            final String paramVal = ctx.pathParam("paramVal");

            final List<Property> properties = propertydao.getPropByParam(param, paramVal);

            // update access data
            if (param.equals("property_id") || param.equals("post_code")) {
                propertydao.updateAccessData(param, paramVal);
            }

            this.addResponseToContext(ctx, properties, properties, "No properties for " +
                    param + " with {" + paramVal + "} found");
        } catch (SQLException e) {
            this.handleError(e, ctx);
        }
    }

    /**
     * Gets all properties in the database
     * 
     * @param ctx The HTTP request
     */
    @OpenApi(summary = "Get all properties", operationId = "getAllProperties", path = "/getAllProperties", methods = HttpMethod.GET, tags = {
            "Property" }, responses = {
                    @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
                    @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
                    @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
            })
    public void getAllProperties(final Context ctx) {
        try {
            final List<Property> properties = propertydao.getAllProps();
            this.addResponseToContext(ctx, properties, properties, "No properties found");
        } catch (SQLException e) {
            this.handleError(e, ctx);
        }
    }

    /**
     * Get all properties where all the specified parameters match the specified
     * values
     * 
     * @param ctx The HTTP request
     */
    @OpenApi(summary = "Get properties by multiple query parameters", operationId = "getPropertiesByParams", path = "/getPropertiesByParams", methods = HttpMethod.GET, tags = {
            "Property" }, responses = {
                    @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
                    @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
                    @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
            })
    public void getPropertiesByParams(Context ctx) {
        try {
            // { "property_id": ["0"], "property_cost": ["10000"] }
            var paramsMap = ctx.queryParamMap();

            List<Property> properties = propertydao.getPropByParams(paramsMap);

            // update access data
            if (paramsMap.containsKey("property_id") && paramsMap.containsKey("post_code")) {
                for (Property property : properties) {
                    String prop_val = property.get("property_id").toString();
                    propertydao.updateAccessData("property_id", prop_val);
                    String post_val = property.get("post_code").toString();
                    propertydao.updateAccessData("post_code", post_val);
                }
            } else if (paramsMap.containsKey("property_id")) {
                for (Property property : properties) {
                    propertydao.updateAccessData("property_id", property.getPropertyId().toString());
                }
            } else if (paramsMap.containsKey("post_code")) {
                for (Property property : properties) {
                    propertydao.updateAccessData("post_code", property.getPostCode().toString());
                }
            }

            if (properties.isEmpty()) {
                ctx.result("No properties with given query vals found");
                ctx.status(404);
            } else {
                ctx.json(properties);
                ctx.status(200);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

    /**
     * Gets all properties where the specified parameter's value is greater than the
     * specified value
     * 
     * @param ctx The HTTP request
     */
    @OpenApi(summary = "Get properties with field value greater than a given value", operationId = "findPropertiesGreaterThan", path = "/getPropertiesGreaterThan/{param}/{paramVal}", methods = HttpMethod.GET, pathParams = {
            @OpenApiParam(name = "param", type = String.class, description = "Numeric or date field name"),
            @OpenApiParam(name = "paramVal", type = String.class, description = "Comparison value")
    }, tags = { "Property" }, responses = {
            @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
            @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
            @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
    })
    public void findPropertiesGreaterThan(Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, true);
    }

    /**
     * Gets all properties where the specified parameter's value is less than the
     * specified value
     * 
     * @param ctx The HTTP request
     */
    @OpenApi(summary = "Get properties with field value less than a given value", operationId = "findPropertiesLessThan", path = "/getPropertiesLessThan/{param}/{paramVal}", methods = HttpMethod.GET, pathParams = {
            @OpenApiParam(name = "param", type = String.class, description = "Numeric or date field name"),
            @OpenApiParam(name = "paramVal", type = String.class, description = "Comparison value")
    }, tags = { "Property" }, responses = {
            @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
            @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
            @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
    })
    public void findPropertiesLessThan(Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, false);
    }

    /**
     * Gets all properties where the specified parameter's value is greater than or
     * less than
     * the specified value
     * 
     * @param ctx           The HTTP request
     * @param isGreaterThan Whether the query should return records where the
     *                      parameter's value is
     *                      greater than the specified value
     */
    private void findPropertiesGreaterThanLessThan(Context ctx, Boolean isGreaterThan) {
        try {
            String param = ctx.pathParam("param");
            String paramVal = ctx.pathParam("paramVal");

            List<String> acceptedParams = Arrays.asList(
                    new String[] { "property_id", "download_date", "contract_date", "purchase_price",
                            "post_code", "settlement_date", "area" });

            List<Property> properties = new ArrayList<>();
            if (acceptedParams.contains(param)) {
                properties = propertydao.getPropertiesGreaterThanLessThan(param, paramVal, isGreaterThan);
            }
            this.addResponseToContext(ctx, properties, properties,
                    "No properties for " + param + " greater than {" + paramVal + "} found");

        } catch (SQLException e) {
            handleError(e, ctx);
        }
    }

    /**
     * Handles SQLException errors
     * 
     * @param e   The thrown error
     * @param ctx The HTTP request
     */
    private void handleError(final SQLException e, final Context ctx) {
        e.printStackTrace();
        ctx.result("Database error: " + e.getMessage());
        ctx.status(500);
    }

    /**
     * Adds a response to the HTTP response based on the properties returned by the
     * query
     * 
     * @param ctx                   The HTTP request
     * @param properties            The records returned by the SQL query
     * @param messageIfNoProperties The message to be sent if the query returned
     *                              records
     * @param messageIfNoProperties The message to be sent if no records were
     *                              returned by
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
     * Gets the average purchase price of properties where the specified parameter
     * has
     * the specified value
     * 
     * @param ctx The HTTP request
     */
    @OpenApi(summary = "Get average purchase price for properties by parameter", operationId = "getAvgPurchasePrice", path = "/getAveragePurchasePrice/{param}/{paramval}", methods = HttpMethod.GET, pathParams = {
            @OpenApiParam(name = "param", type = String.class, description = "Field to filter properties by"),
            @OpenApiParam(name = "paramval", type = String.class, description = "Value to filter by")
    }, tags = { "Property" }, responses = {
            @OpenApiResponse(status = "200", content = { @OpenApiContent(type = "application/json") }),
            @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
            @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
    })
    public void getAvgPurchasePrice(Context ctx) {
        try {
            String param = ctx.pathParam("param");
            String paramval = ctx.pathParam("paramval");

            List<Property> properties = propertydao.getPropByParam(param, paramval);

            double averagePurchasePrice = propertydao.getAverageOfField(properties, "purchasePrice");
            if (properties.isEmpty()) {
                ctx.result("No properties found");
                ctx.status(404);
            } else {
                ctx.json(averagePurchasePrice);
                ctx.status(200);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

}
