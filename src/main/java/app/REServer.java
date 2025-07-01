package app;

import io.javalin.Javalin;
import sales.SalesController;
import properties.PropertyController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class REServer {
    private static final Logger LOG = LoggerFactory.getLogger(REServer.class);

    public static void main(String[] args) {
        // API implementation
        SalesController salescontroller = new SalesController();
        PropertyController propertyController = new PropertyController();
        // start Javalin on port 7070
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Real Estate server is running"))
                .start(7070);

        propertyController.registerRoutes(app);
    }
}