package app;

import io.javalin.Javalin;
import properties.PropertyController;

/**
 * Main class to start the Real Estate server.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.ApiController;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

public class REApi {
    private static final Logger LOG = LoggerFactory.getLogger(REAnalytics.class);

    public static void main(String[] args) {
        // API implementation
        ApiController apiController = new ApiController();
        // start Javalin on port 7070 d

        var app = Javalin.create(config -> {

            config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withOpenApiInfo(info -> info.setTitle("Javalin OpenAPI example"));
                });
            }));

            config.registerPlugin(new SwaggerPlugin());
            config.registerPlugin(new ReDocPlugin());
        });

        propertyController.registerRoutes(app);
        app.start(7002);

        System.out.println("Check out ReDoc docs at http://localhost:7002/redoc");
        System.out.println("Check out Swagger UI docs at http://localhost:7002/swagger-ui");

    }
}