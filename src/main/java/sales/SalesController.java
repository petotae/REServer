package sales;

import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

public class SalesController {

    private final SalesDAO homeSales;

    public SalesController(SalesDAO homeSales) {
        this.homeSales = homeSales;
    }

    // implements POST /sales
    public void createSale(Context ctx) {
        try {
            // Extract HomeSale from request body
            HomeSale sale = ctx.bodyValidator(HomeSale.class).get();

            // store new sale in database
            boolean success = homeSales.newSale(sale);
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
            List<HomeSale> allSales = homeSales.getAllSales();
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
    public void getSaleByID(Context ctx, String id) {
        try {
            Optional<HomeSale> sale = homeSales.getSaleById(id);
            sale.map(ctx::json)
                .orElseGet(() -> error(ctx, "Sale not found", 404));
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.result("Database error: " + e.getMessage());
            ctx.status(500);
        }
    }

    // Implements GET /sales/postcode/{postcodeID}
    public void findSaleByPostCode(Context ctx, String postCode) {
        try {
            List<HomeSale> sales = homeSales.getSalesByPostCode(postCode);
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
