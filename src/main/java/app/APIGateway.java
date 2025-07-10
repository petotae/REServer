package app;

import io.javalin.Javalin;
import java.net.http.*;
import java.net.URI;

public class APIGateway {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        final String PROPERTY_SERVER_URL = "http://localhost:7001/properties";
        final String ANALYTICS_SERVER_URL = "http://localhost:7002/analytics";
        final HttpClient httpClient = HttpClient.newHttpClient();

        app.post("/createProperty", ctx -> {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(PROPERTY_SERVER_URL + "/createProperty"))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

        app.get("/getProperties/{param}/{paramVal}", ctx -> {
            String param = ctx.pathParam("param");
            String paramVal = ctx.pathParam("paramVal");

            String targetUrl = PROPERTY_SERVER_URL + "/getProperties/" + param + "/" + paramVal;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(targetUrl))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

        app.get("/getAllProperties", ctx -> {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(PROPERTY_SERVER_URL + "/getAllProperties"))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

        app.get("/getPropertiesByParams", ctx -> {
            String queryString = ctx.queryString();
            String targetUrl = PROPERTY_SERVER_URL + "/getPropertiesByParams"
                    + (queryString != null ? "?" + queryString : "");

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(targetUrl))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

        app.get("/getPropertiesGreaterThan/{param}/{paramVal}", ctx -> {
            String param = ctx.pathParam("param");
            String paramVal = ctx.pathParam("paramVal");

            String targetUrl = PROPERTY_SERVER_URL + "/getPropertiesGreaterThan/" + param + "/" + paramVal;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(targetUrl))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

        app.get("/getPropertiesLessThan/{param}/{paramVal}", ctx -> {
            String param = ctx.pathParam("param");
            String paramVal = ctx.pathParam("paramVal");

            String targetUrl = PROPERTY_SERVER_URL + "/getPropertiesLessThan/" + param + "/" + paramVal;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(targetUrl))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

        app.get("/getAveragePurchasePrice/{param}/{paramval}", ctx -> {
            String param = ctx.pathParam("param");
            String paramVal = ctx.pathParam("paramVal");

            String targetUrl = PROPERTY_SERVER_URL + "/getAveragePurchasePrice/" + param + "/" + paramVal;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(targetUrl))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

        app.get("/getAccessData/{param}/{paramVal}", ctx -> {
            String param = ctx.pathParam("param");
            String paramVal = ctx.pathParam("paramVal");

            String targetUrl = ANALYTICS_SERVER_URL + "/getAccessData/" + param + "/" + paramVal;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(targetUrl))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ctx.status(resp.statusCode()).result(resp.body());
        });

    }
}