package properties;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.SQLException;

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
    }

    public void createProperty(Context ctx) {
        try {
            // Extract HomeSale from request body
            Property prop = ctx.bodyValidator(Property.class).get();
            // store new sale in database
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

    public void findPropertiesGreaterThan(Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, true);
    }

    public void findPropertiesLessThan(Context ctx) {
        findPropertiesGreaterThanLessThan(ctx, false);
    }

    private void findPropertiesGreaterThanLessThan(Context ctx, Boolean isGreaterThan) {
        try {
            String param = ctx.pathParam("param");
            String paramVal = ctx.pathParam("paramVal");

            List<String> acceptedParams = Arrays.asList(
                new String[]{"property_id", "download_date", "contract_date", "purchase_price", 
                             "post_code", "settlement_date", "area"});

            List<Property> properties = new ArrayList<>();
            if (acceptedParams.contains(param)) {
                properties = propertydao.getPropertiesGreaterThanLessThan(param, paramVal, isGreaterThan);
            } 
            this.addResponseToContext(ctx, properties, "No properties for " + param + " greater than {" + paramVal + "} found");
        } catch (SQLException e) {
            handleError(e, ctx);
        }
    }

    public void getPropertiesByParams(Context ctx) {
        try {
            // { "property_id": ["0"], "property_cost": ["10000"] }
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
