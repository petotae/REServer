package app;

import properties.PropertyController;

import io.javalin.Javalin;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

public class Main {

    public static void main(String[] args) {
        // Instantiate controller
        PropertyController propertyController = new PropertyController();

        // Create Javalin app with OpenAPI, Swagger, and ReDoc plugins
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withOpenApiInfo(info -> info.setTitle("Javalin OpenAPI example"));
                });
            }));
            config.registerPlugin(new SwaggerPlugin());
            config.registerPlugin(new ReDocPlugin());
        });

        // Register property routes
        propertyController.registerRoutes(app);

        // Start app
        app.start(7002);

        System.out.println("Check out ReDoc docs at http://localhost:7002/redoc");
        System.out.println("Check out Swagger UI docs at http://localhost:7002/swagger-ui");
    }
}