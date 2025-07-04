package sales;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class SalesController {

    public final Logger log = LoggerFactory.getLogger(SalesController.class);
    private final SalesDAO salesdao = new SalesDAO();

    public void registerRoutes(Javalin app) {
        app.post("/createSale", this::createSale);
        app.get("/sales/{id}", this::getSaleByID);
        app.get("/sales", this::getAllSales);
        app.get("/sales/postcode/{postCode}", this::findSaleByPostCode);
    }

    // implements POST /sales
    public void createSale(final Context ctx) {
        try {
            // Extract HomeSale from request body
            HomeSale sale = ctx.bodyValidator(HomeSale.class).get();
            // store new sale in database
            boolean success = salesdao.newSale(sale);
            if (success) {
                ctx.result("Sale Created");
                ctx.status(201);
            } else {
                ctx.result("Failed to add sale");
                ctx.status(400);
            }
        } catch (SQLException e) {
            this.error(ctx, "Database error: " + e.getMessage(), 500);
        }
    }

    // implements GET /sales
    public void getAllSales(final Context ctx) {
        try {
            List<HomeSale> allSales = salesdao.getAllSales();
            if (allSales.isEmpty()) {
                ctx.result("No Sales Found");
                ctx.status(404);
            } else {
                ctx.json(allSales);
                ctx.status(200);
            }
        } catch (SQLException e) {
            this.error(ctx, "Database error: " + e.getMessage(), 500);
        }
    }

    // implements GET /sales/{saleID}
    public void getSaleByID(final Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            HomeSale sale = salesdao.getSaleById(id);
            if (sale != null) {
                ctx.json(sale);
            } else {
                ctx.status(404).result("Sale with id {" + id + "} not found");
            }
        } catch (SQLException e) {
            this.error(ctx, "Database error: " + e.getMessage(), 500);
        }
    }

    // Implements GET /sales/postcode/{postcodeID}
    public void findSaleByPostCode(final Context ctx) {
        try {
            int postCode = Integer.parseInt(ctx.pathParam("postCode"));
            List<HomeSale> sales = salesdao.getSalesByPostCode(postCode);
            if (sales.isEmpty()) {
                ctx.result("No sales for postcode found");
                ctx.status(404);
            } else {
                ctx.json(sales);
                ctx.status(200);
            }
        } catch (SQLException e) {
            this.error(ctx, "Database error: " + e.getMessage(), 500);
        }
    }

    private void error(final Context ctx, final String msg, final int code) {
        ctx.result(msg);
        ctx.status(code);
    }
}
