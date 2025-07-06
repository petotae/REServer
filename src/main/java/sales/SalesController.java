package sales;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * SalesController handles HTTP requests related to home sales.
 * It provides endpoints to create a sale, retrieve sales by ID,
 * get all sales, and find sales by postcode.
 */
public class SalesController {

    /**
     * Logger for logging messages in SalesController.
     */
    public final Logger log = LoggerFactory.getLogger(SalesController.class);
    /**
     * Instance of SalesDAO to interact with the database.
     * This is used to perform CRUD operations on home sales.
     */
    private final SalesDAO salesdao = new SalesDAO();

    /**
     * Registers the routes for the SalesController.
     *
     * @param app the Javalin application instance
     */
    public void registerRoutes(final Javalin app) {
        app.post("/createSale", this::createSale);
        app.get("/sales/{id}", this::getSaleByID);
        app.get("/sales", this::getAllSales);
        app.get("/sales/postcode/{postCode}", this::findSaleByPostCode);
    }

    /**
     * Creates a new home sale.
     * Expects a HomeSale object in the request body.
     *
     * @param ctx the Javalin context
     */
    public void createSale(final Context ctx) {
        try {
            // Extract HomeSale from request body
            final HomeSale sale = ctx.bodyValidator(HomeSale.class).get();
            // store new sale in database
            final boolean success = salesdao.newSale(sale);
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

    /**
     * Retrieves all sales from the database.
     * @param ctx
     */
    public void getAllSales(final Context ctx) {
        try {
            final List<HomeSale> allSales = salesdao.getAllSales();
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

    /**
     * Retrieves a sale by its ID.
     *
     * @param ctx the Javalin context
     */
    public void getSaleByID(final Context ctx) {
        try {
            final int saleId = Integer.parseInt(ctx.pathParam("id"));
            final HomeSale sale = salesdao.getSaleById(saleId);
            if (sale != null) {
                ctx.json(sale);
            } else {
                ctx.status(404).result("Sale with saleId {" + saleId + "} not found");
            }
        } catch (SQLException e) {
            this.error(ctx, "Database error: " + e.getMessage(), 500);
        }
    }

    /**     * Finds sales by postcode.
     *
     * @param ctx the Javalin context
     */
    public void findSaleByPostCode(final Context ctx) {
        try {
            final int postCode = Integer.parseInt(ctx.pathParam("postCode"));
            final List<HomeSale> sales = salesdao.getSalesByPostCode(postCode);
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
