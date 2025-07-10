package analytics;

import java.sql.SQLException;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

public class AnalyticsController {
  private final AnalyticsDAO analyticsDAO = new AnalyticsDAO();
  private final static String QUEUE_NAME = "hello";

  public AnalyticsController() {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      factory.setPort(5672);
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();

      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        System.out.println(" [x] Received '" + message + "'");
      };
      channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
      });
    } catch (Exception e) {

    }
  }

  /**
   * Set the endpoints to post data to the database or get data from the database
   * 
   * @param app The current Javalin app
   */
  public void registerRoutes(final Javalin app) {
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
