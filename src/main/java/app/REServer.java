package app;

import io.javalin.Javalin;
import properties.PropertyController;

public class REServer {
    public static void main(String[] args) {
        PropertyController propertyController = new PropertyController();
        // start Javalin on port 7070
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Real Estate server is running"))
                .start(7070);

        propertyController.registerRoutes(app);
    }
}