package properties;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class PropertyController {

    public final Logger log = LoggerFactory.getLogger(PropertyController.class);
    private final PropertyDAO propertydao = new PropertyDAO();

    public void registerRoutes(Javalin app) {
        app.post("/createProperty", this::createProperty);
        app.get("/getProperties/{param}/{paramval}", this::findPropertyByParam);
        app.get("/getAllProperties", this::getAllProperties);
        app.get("/getAveragePurchasePrice/{param}/{paramval}", this::getAvgPurchasePrice);
    }

    public void createProperty(Context ctx) {
        try {
            // Extract HomeSale from request body
            Property prop = ctx.bodyValidator(Property.class).get();
            // store new sale in database
            boolean success = propertydao.newProperty(prop);
            if (success) {
                ctx.result("Property Created");
                ctx.status(201);
            } else {
                ctx.result("Failed to add property");
                ctx.status(400);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

    public void findPropertyByParam(Context ctx) {
        try {
            String param = ctx.pathParam("param");
            String paramval = ctx.pathParam("paramval");

            List<Property> properties = propertydao.getPropertiesByField(param, paramval);

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
            List<Property> properties = propertydao.getAllProperties();

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

    public void getAvgPurchasePrice(Context ctx) {
        try {
            String param = ctx.pathParam("param");
            String paramval = ctx.pathParam("paramval");

            List<Property> properties = propertydao.getPropertiesByField(param, paramval);

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
