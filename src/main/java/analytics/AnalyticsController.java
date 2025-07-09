package analytics;

import java.sql.SQLException;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class AnalyticsController {
  private final AnalyticsDAO analyticsDAO = new AnalyticsDAO();

  /**
   * Set the endpoints to post data to the database or get data from the database
   * 
   * @param app The current Javalin app
   */
  public void registerRoutes(final Javalin app) {
    app.post("/analytics/updateAccessData/{param}/{paramVal}", this::updateAccessData);
    app.get("/analytics/getAccessData/{param}/{paramVal}", this::getAccessData);
  }

  public void updateAccessData(Context ctx) {
    try {
      final String param = ctx.pathParam("param");
      final String paramVal = ctx.pathParam("paramVal");
      Boolean success = analyticsDAO.updateAccessData(param, paramVal);

      if (success) {
          ctx.result("Property Created");
          ctx.status(201);
      } else {
          ctx.result("Failed to add property");
          ctx.status(400);
      }
    } catch (SQLException e) {
      handleError(e, ctx);
    }
  }

  public void getAccessData(Context ctx) {
    try {
      final String param = ctx.pathParam("param");
      final String paramVal = ctx.pathParam("paramVal");
      int accessCount = analyticsDAO.getParamAccessCount(param, paramVal);
      ctx.json(accessCount);
      ctx.status(200);
    } catch (SQLException e) {
      handleError(e, ctx);
    } catch (IllegalArgumentException e) {
      ctx.result("Illegal argument param: " + e.getMessage());
      ctx.status(404);
    }
  }

  /**
     * Handles SQLException errors
     * 
     * @param e   The thrown error
     * @param ctx The HTTP request
     */
    private void handleError(final SQLException e, final Context ctx) {
        e.printStackTrace();
        ctx.result("Database error: " + e.getMessage());
        ctx.status(500);
    }

}
