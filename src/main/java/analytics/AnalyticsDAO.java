package analytics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalyticsDAO {
  private static final String JDBC_URL = "jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:6543/postgres?sslmode=require";
  private static final String JDBC_USER = "postgres.jnghzszlarsaxxhiavcv";
  private static final String JDBC_PASSWORD = "iangortoncsw4530";

  public Boolean updateAccessData(final String param, final String paramVal) throws SQLException {
        Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        String sqlGet = String.format("SELECT * FROM nsw_property_data_access_data WHERE attribute_type = \'%s\' AND value = %s;", param, paramVal);
        PreparedStatement stmtGet = conn.prepareStatement(sqlGet);
        final ResultSet resSet = stmtGet.executeQuery();

        String sqlPost = "";
        int count = 0;
        while (resSet.next()) {
            sqlPost = String.format("UPDATE nsw_property_data_access_data SET access_count = %d WHERE attribute_type = '%s' AND value = %s", resSet.getLong("access_count") + 1, param, paramVal);
            count++;
        }
        if (count == 0) {
            sqlPost = String.format("INSERT INTO nsw_property_data_access_data (attribute_type, value, access_count) VALUES ('%s', %s, 1)", param, paramVal);
        }
        PreparedStatement stmtPost = conn.prepareStatement(sqlPost);
        try {
            stmtPost.executeQuery();
        } catch (SQLException e) {
            if (!e.getMessage().contains("No results were returned by the query")) {
                throw new SQLException();
            } else {
              return false;
            }
        }
        return true;
    }

    public int getParamAccessCount(String param, String paramVal) throws SQLException, IllegalArgumentException {
        String sql = "SELECT access_count FROM nsw_property_data_access_data";
        if (!param.equals("property_id") && !param.equals("post_code")) {
          throw new IllegalArgumentException();
        }
        sql += String.format(" WHERE attribute_type = \'%s\' AND value = %s;", param, paramVal);

        Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          return rs.getInt("access_count");
        }
        throw new SQLException();
    }
}
