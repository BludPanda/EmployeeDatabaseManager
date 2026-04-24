import java.sql.*;

public class SQLConnector {

    String url = "jdbc:mysql://localhost:3306/employeedatabase";
    String user = "root";
    String password = "%%3zoUid4%6_";

    // Log in with DBeaver to get a good look at the database

    public ResultSet executeQuery(String sql) {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            System.err.println("SQL Execution Error: " + e.getMessage());
            return null;
        }
    }
}
