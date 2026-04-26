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

    public void writeQuery(String sql)
    {
        try (
            Connection conn = DriverManager.getConnection(url, user, password);
            CallableStatement cstmt = conn.prepareCall(sql))
            {
            cstmt.execute();
        } catch (SQLException e) {
            System.out.println("Error calling procedure: " + e.getMessage());
        }
    }

    public void updateEmployee(int empID, String firstName, String lastName, String email, 
                           String phone, String address, String division, String jobTitle) {
    
        String sql = "{CALL EditEmployee(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (
            Connection conn = DriverManager.getConnection(url, user, password);
            CallableStatement cstmt = conn.prepareCall(sql))
            {

            cstmt.setInt(1, empID);
            cstmt.setString(2, firstName);
            cstmt.setString(3, lastName);
            cstmt.setString(4, email);
            cstmt.setString(5, phone);
            cstmt.setString(6, address);
            cstmt.setString(7, division);
            cstmt.setString(8, jobTitle);

            cstmt.execute();
            
            System.out.println("Employee updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error calling procedure: " + e.getMessage());
        }
    }
}
