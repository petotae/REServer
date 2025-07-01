package sales;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

public class SalesController {

    public final Logger log = LoggerFactory.getLogger(SalesController.class);
    private final SalesDAO salesdao = new SalesDAO();

    public void registerRoutes(Javalin app) {
        app.post("/createSale", this::createSale);
        app.get("/sales/{id}", this::getSaleByID);
        app.get("/sales", this::getAllSales);
        app.put("/sales/{postCode}", this::findSaleByPostCode);
    }

    // implements POST /sales
    public void createSale(Context ctx) {
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
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

    // implements GET /sales
    public void getAllSales(Context ctx) {
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
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

    // implements GET /sales/{saleID}
    public void getSaleByID(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Optional<HomeSale> sale = salesdao.getSaleById(id);
            if (sale != null) {
                ctx.json(sale);
            } else {
                ctx.status(404).result("Sale with id {" + id + "} not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

    // Implements GET /sales/postcode/{postcodeID}
    public void findSaleByPostCode(Context ctx) {
        try {
            String postCode = ctx.pathParam("postCode");
            List<HomeSale> sales = salesdao.getSalesByPostCode(postCode);
            if (sales.isEmpty()) {
                ctx.result("No sales for postcode found");
                ctx.status(404);
            } else {
                ctx.json(sales);
                ctx.status(200);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

    private Context error(Context ctx, String msg, int code) {
        ctx.result(msg);
        ctx.status(code);
        return ctx;
    }
}
