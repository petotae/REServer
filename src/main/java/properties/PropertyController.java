package properties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class PropertyController {
    private final PropertyDAO propertydao = new PropertyDAO();

    public void registerRoutes(final Javalin app) {
        app.post("/createProperty", this::createProperty);
        app.get("/getProperties/{param}/{paramVal}", this::findPropertyByParam);
        app.get("/getAllProperties", this::getAllProperties);
        app.get("/getPropertiesByParams", this::getPropertiesByParams);
        app.get("/getPropertiesGreaterThan/{param}/{paramVal}", this::findPropertiesGreaterThan);
        app.get("/getPropertiesLessThan/{param}/{paramVal}", this::findPropertiesLessThan);
        app.get("/getAveragePurchasePrice/{param}/{paramval}", this::getAvgPurchasePrice);
    }

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

    public void findPropertyByParam(final Context ctx) {
        try {
            final String param = ctx.pathParam("param");
            final String paramVal = ctx.pathParam("paramVal");

            final List<Property> properties = propertydao.getPropByParam(param, paramVal);

            if (properties.isEmpty()) {
                ctx.result("No properties for " + param + " with {" + paramVal + "} found");
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

    public void getAllProperties(final Context ctx) {
        try {
            final List<Property> properties = propertydao.getAllProps();

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

    public void findPropertiesGreaterThan(final Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, true);
    }

    public void findPropertiesLessThan(final Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, false);
    }

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
            this.addResponseToContext(ctx, properties, "No properties for " + param + " greater than {" + paramVal + "} found");
        } catch (SQLException e) {
            handleError(e, ctx);
        }
    }

    public void getPropertiesByParams(final Context ctx) {
        try {
            // { "property_id": ["0"], "property_cost": ["10000"] }
            final var paramsMap = ctx.queryParamMap();

            final List<Property> properties = propertydao.getPropByParams(paramsMap);

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

    private void handleError(final SQLException except, final Context ctx) {
        except.printStackTrace();
        ctx.result("Database error: " + except.getMessage());
        ctx.status(500);
    }

    private void addResponseToContext(final Context ctx, final List<Property> properties, final String msgNoProp) {
        if (properties.isEmpty()) {
            ctx.result(msgNoProp);
            ctx.status(404);
        } else {
            ctx.json(properties);
            ctx.status(200);
        }
    }
    public void getAvgPurchasePrice(final Context ctx) {
        try {
            final String param = ctx.pathParam("param");
            final String paramval = ctx.pathParam("paramval");

            final List<Property> properties = propertydao.getPropByParam(param, paramval);

            final double avgPurPrice = propertydao.getAverageOfField(properties, "purchasePrice");
            if (properties.isEmpty()) {
                ctx.result("No properties found");
                ctx.status(404);
            } else {
                ctx.json(avgPurPrice);
                ctx.status(200);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

}
