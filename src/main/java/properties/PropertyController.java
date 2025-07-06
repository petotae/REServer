package properties;

import properties.util.ErrorResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.openapi.*;

public class PropertyController {

    public final Logger log = LoggerFactory.getLogger(PropertyController.class);
    private final PropertyDAO propertydao = new PropertyDAO();

    public void registerRoutes(Javalin app) {
        app.post("/createProperty", this::createProperty);
        app.get("/getProperties/{param}/{paramVal}", this::findPropertyByParam);
        app.get("/getAllProperties", this::getAllProperties);
        app.get("/getPropertiesByParams", this::getPropertiesByParams);
        app.get("/getPropertiesGreaterThan/{param}/{paramVal}", this::findPropertiesGreaterThan);
        app.get("/getPropertiesLessThan/{param}/{paramVal}", this::findPropertiesLessThan);
        app.get("/getAveragePurchasePrice/{param}/{paramval}", this::getAvgPurchasePrice);
    }

    @OpenApi(summary = "Create property", operationId = "createProperty", path = "/createProperty", methods = HttpMethod.POST, tags = {
            "Property" }, requestBody = @OpenApiRequestBody(content = {
                    @OpenApiContent(from = Property.class)
            }), responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400", content = { @OpenApiContent(from = ErrorResponse.class) })
            })
    public void createProperty(Context ctx) {
        try {
            Property prop = ctx.bodyValidator(Property.class).get();
            boolean success = propertydao.createProp(prop);
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

    @OpenApi(summary = "Find properties by parameter", operationId = "findPropertyByParam", path = "/getProperties/{param}/{paramval}", methods = HttpMethod.GET, pathParams = {
            @OpenApiParam(name = "param", type = String.class, description = "Property field to filter by"),
            @OpenApiParam(name = "paramval", type = String.class, description = "Value of the property field")
    }, tags = { "Property" }, responses = {
            @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
            @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
            @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
    })
    public void findPropertyByParam(Context ctx) {
        try {
            String param = ctx.pathParam("param");
            String paramval = ctx.pathParam("paramval");

            List<Property> properties = propertydao.getPropByParam(param, paramval);

            if (properties.isEmpty()) {
                ctx.result("No properties for " + param + " with {" + paramval + "} found");
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

    @OpenApi(summary = "Get all properties", operationId = "getAllProperties", path = "/getAllProperties", methods = HttpMethod.GET, tags = {
            "Property" }, responses = {
                    @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
                    @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
                    @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
            })
    public void getAllProperties(Context ctx) {
        try {
            List<Property> properties = propertydao.getAllProps();

            if (properties.isEmpty()) {
                ctx.result("No properties found");
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

    @OpenApi(summary = "Get properties by multiple query parameters", operationId = "getPropertiesByParams", path = "/getPropertiesByParams", methods = HttpMethod.GET, tags = {
            "Property" }, responses = {
                    @OpenApiResponse(status = "200", content = { @OpenApiContent(from = Property[].class) }),
                    @OpenApiResponse(status = "404", content = { @OpenApiContent(from = ErrorResponse.class) }),
                    @OpenApiResponse(status = "500", content = { @OpenApiContent(from = ErrorResponse.class) })
            })
    public void getPropertiesByParams(Context ctx) {
        try {
            var paramsMap = ctx.queryParamMap();

            List<Property> properties = propertydao.getPropByParams(paramsMap);

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
            this.addResponseToContext(ctx, properties,
                    "No properties for " + param + " greater than {" + paramVal + "} found");
        } catch (SQLException e) {
            handleError(e, ctx);
        }
    }

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

    private void handleError(SQLException e, Context ctx) {
        e.printStackTrace();
        ctx.result("Database error: " + e.getMessage());
        ctx.status(500);
    }

    private void addResponseToContext(Context ctx, List<Property> properties, String messageIfNoProperties) {
        if (properties.isEmpty()) {
            ctx.result(messageIfNoProperties);
            ctx.status(404);
        } else {
            ctx.json(properties);
            ctx.status(200);
        }
    }
}