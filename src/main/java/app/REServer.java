package app;

import io.javalin.Javalin;
import properties.PropertyController;

/**
 * Main class to start the Real Estate server.
 */
public class REServer {
    /**
     * Main method to start the Javalin server.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final PropertyController propertyCon = new PropertyController();
        // start Javalin on port 7070
        final var app = Javalin.create()
                .get("/", ctx -> ctx.result("Real Estate server is running"))
                .start(7070);

        propertyCon.registerRoutes(app);
    }
}