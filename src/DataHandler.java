
import java.sql.*;


public class DataHandler {
    
    protected static SQLConnector mySQLConnector = new SQLConnector();

    public String allStates = """
    AL, AK, AZ, AR, CA, CO, CT, DE, FL, GA, HI, ID, IL, IN, IA, KS, 
    KY, LA, ME, MD, MA, MI, MN, MS, MO, MT, NE, NV, NH, NJ, NM, NY,
    NC, ND, OH, OK, OR,PA, RI, SC, SD, TN, TX, UT, VT, VA, WA, WV,
    WI, WY""";

    // ATTENTION:
    /*
        Any method that has a parameter input was previously validated meaning you don't have to check whether
        a string can convert to an int, whether it's the right size, etc. unless you want to do extremely rare
        error catching but ultimately the methods should never result in an error involving the params. If
        something needs to be done with the input it is explained in a comment.
    */

    // =============================== DUMMY METHOD =============================== //

    public void dummyMethod(String[] params)
    {
        // USE THIS AS GUIDE
        /*
        String query = "...";

        try (ResultSet rs = mySQLConnector.executeQuery(query)) {

            // Query connection error or query output is empty
            if (rs == null || !rs.next())
            {
                // return some sort of null return and or error print statement
            }
            
            // Iterates Through Rows of Query Table
            while (rs.next()) {
                
            }
            
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
        */

        System.out.println("This is just to show an option triggers the Data Handler!");
        System.out.println("These are the prompts entered in the last menu: ");
        for (String p : params) { System.out.println(p); }
    }

    // =============================== VERIFICATION =============================== //

    
    public int verifyUsername(String name)
    {
        if (!name.contains(" ") || name.length() == 0) { return -1; }

        String[] nameParts = name.split(" ");
        String sql = "SELECT empID FROM employees WHERE firstName = '" + nameParts[0] + "' AND lastName = '" + nameParts[1] + "'";
        int empID = -1;

        ResultSet rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) {
                empID = rs.getInt("empID"); // empID with username
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        return empID; // No empID with username
    }

    public boolean verifyPassword(int empID, String password)
    {
        String sql = "SELECT empID FROM employees WHERE empID = " + empID + " AND password = '" + password + "'";
        Boolean verified = false;

        ResultSet rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) {
                verified = true; //Correct password
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        return verified; //Wrong password
    }

    public boolean checkAdmin(int empID)
    {
        String sql = "SELECT empID FROM admins WHERE empID = " + empID;
        Boolean isAdmin = false;

        ResultSet rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) {
                isAdmin = true; // Admin
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
        
        return isAdmin; // Not admin
    }

    // =============================== EMPLOYEE SUMMARY & PAY STATEMENTS =============================== //

    public void printEmployeeSummary(int empID)
    {
        String sql;
        ResultSet rs;

        int divID = -1;
        int addressID = -1;
        int roleID = -1;

        System.out.println("\n--- Employee Info ---");

        // Employees table
        sql = "SELECT * FROM employees WHERE empID = " + empID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) {
                System.out.println("ID:\t\t" + rs.getInt("empID"));
                System.out.println("Name:\t\t" + rs.getString("firstName") + " " + rs.getString("lastName"));
                System.out.println("DOB:\t\t" + rs.getString("DOB"));
                System.out.println("SSN:\t\t" + rs.getString("SSN"));

                divID = rs.getInt("divID");
                addressID = rs.getInt("addressID");
                roleID = rs.getInt("roleID");
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        System.out.println("---------------------");

        // Divisions table
        sql = "SELECT * FROM divisions WHERE divID = " + divID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) { System.out.println("Division:\t" + rs.getString("divisionName")); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        // Roles table
        sql = "SELECT * FROM roles WHERE roleID = " + roleID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) { System.out.println("Job Title:\t" + rs.getString("jobTitle")); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        System.out.println("---------------------");

        // Payroll table
        sql = "SELECT * FROM payroll WHERE empID = " + empID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next())
            {
                System.out.println("Salary:\t\t" + rs.getInt("salary"));
                System.out.println("hireDate:\t" + rs.getString("hireDate"));
                
                if (rs.getString("leaveDate") == null) { System.out.println("Status:\t\tActive"); }
                else { System.out.println("Status:\t\tInactive"); }
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        System.out.println("---------------------");

        // Contact Info table
        sql = "SELECT * FROM contactinfo WHERE empID = " + empID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next())
            {
                System.out.println("Phone:\t\t" + rs.getString("phone"));
                System.out.println("Email:\t\t" + rs.getString("email"));
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        // Addresses table
        sql = "SELECT " +
            "CONCAT_WS(', ', a.street, c.cityName, s.abbreviation) AS fullAddress " +
            "FROM employees e " +
            "JOIN addresses a ON e.addressID = a.addressID " +
            "JOIN cities c    ON a.cityID = c.cityID " +
            "JOIN states s    ON c.stateID = s.stateID " +
            "WHERE e.empID = " + empID;

        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) { System.out.println("Address:\t" + rs.getString("fullAddress")); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
    }

    public void printEditedEmployeeSummary(String[] editPrompts, int empID)
    {
        // GET CURRENT VALUES
        String v_newFirstName = "";
        String v_newLastName = "";
        String v_newEmail = "";
        String v_newPhone = "";
        String v_newAddress = "";
        String v_newDivisionName = "";
        String v_newJobTitle = "";

        String sql;
        ResultSet rs;

        int divID = -1;
        int addressID = -1;
        int roleID = -1;

        // Employees table
        sql = "SELECT * FROM employees WHERE empID = " + empID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) {
                v_newFirstName = rs.getString("firstName");
                v_newLastName = rs.getString("lastName");

                divID = rs.getInt("divID");
                addressID = rs.getInt("addressID");
                roleID = rs.getInt("roleID");
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        // Divisions table
        sql = "SELECT * FROM divisions WHERE divID = " + divID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) { v_newDivisionName = rs.getString("divisionName"); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        // Roles table
        sql = "SELECT * FROM roles WHERE roleID = " + roleID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) { v_newJobTitle = rs.getString("jobTitle"); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        // Contact Info table
        sql = "SELECT * FROM contactinfo WHERE empID = " + empID;
        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next())
            {
                v_newEmail = rs.getString("phone");
                v_newPhone = rs.getString("email");
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        // Addresses table
        sql = "SELECT " +
            "CONCAT_WS(', ', a.street, c.cityName, s.abbreviation) AS fullAddress " +
            "FROM employees e " +
            "JOIN addresses a ON e.addressID = a.addressID " +
            "JOIN cities c    ON a.cityID = c.cityID " +
            "JOIN states s    ON c.stateID = s.stateID " +
            "WHERE e.empID = " + empID;

        rs = mySQLConnector.executeQuery(sql);
        try {
            if (rs != null && rs.next()) { v_newAddress = rs.getString("fullAddress"); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }

        // SET NEW VALUES
        String[] nameParts = new String[] {"", ""};
        if (editPrompts[0].contains(" "))
        { nameParts = editPrompts[0].split(" "); }

        int[] DOB = formatDateFromPrompt(editPrompts[1]);
        String sqlDOB = DOB[0] + "-" + DOB[1] + "-" + DOB[2];

        // Name
        if (!editPrompts[0].equalsIgnoreCase("next") && editPrompts[0].length() > 0)
        { 
            v_newFirstName = nameParts[0];
            v_newFirstName = nameParts[1];
        }

        // Job title
        if (!editPrompts[1].equalsIgnoreCase("next"))
        { v_newJobTitle = editPrompts[1]; }

        // Division
        if (!editPrompts[2].equalsIgnoreCase("next"))
        { v_newDivisionName = editPrompts[2]; }
        
        // Email
        if (!editPrompts[3].equalsIgnoreCase("next"))
        { v_newEmail = editPrompts[3]; }

        // Phone
        if (!editPrompts[4].equalsIgnoreCase("next"))
        { v_newPhone = editPrompts[4]; }

        // Email
        if (!editPrompts[5].equalsIgnoreCase("next"))
        { v_newAddress = editPrompts[5]; }

        // Execute changes
        mySQLConnector.updateEmployee(empID, v_newFirstName, v_newLastName, v_newEmail, v_newPhone, v_newAddress, v_newDivisionName, v_newJobTitle);

        // Show changes
        printEmployeeSummary(empID);
    }

    public int checkIfEmployeeExists(String[] searchPrompts)
    {
        // Return -1 if the employee does not exist
        // Return -2 if there are multiple matches
        // Return -3 if all prompts are "next" / no info to search with

        // Setup all search commands
        String[] nameParts = new String[] {"", ""};
        if (searchPrompts[0].contains(" "))
        { nameParts = searchPrompts[0].split(" "); }

        int[] DOB = formatDateFromPrompt(searchPrompts[1]);
        String sqlDOB = DOB[0] + "-" + DOB[1] + "-" + DOB[2];

        String[] searchSQLCommands = new String[] {
            "SELECT empID FROM employees WHERE firstName = '" + nameParts[0] + "' AND lastName = '" + nameParts[1] + "'",
            "SELECT empID FROM employees WHERE DOB = " + "'" + sqlDOB + "'",
            "SELECT empID FROM employees WHERE SSN = " + searchPrompts[2],
            "SELECT empID FROM employees WHERE empID = " + searchPrompts[3]
        };

        // Check for results for each search
        int empID = -1;
        int numEmployeesFound = 0;
        int nextCount = 0;
        ResultSet rs;

        for (int i = 0; i < 4; i++)
        {
            if (searchPrompts[i].equalsIgnoreCase("next") || searchPrompts[i].equals(""))
            { nextCount++; continue; }

            rs = mySQLConnector.executeQuery(searchSQLCommands[i]);
            try {
                while (rs != null && rs.next()) {
                    empID = rs.getInt("empID");
                    numEmployeesFound++;
                }
                rs.getStatement().getConnection().close();
            } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
        }

        // Interpet results
        if (numEmployeesFound == 1 && empID != -1)
        { return empID; }
        else if (numEmployeesFound > 1)
        { return -2; }
        else if (numEmployeesFound == 0 && nextCount == 4)
        { return -3; }
        else
        { return -1; }
    }

    private int[] formatDateFromPrompt(String DOB)
    {
        // input format: MM/DD/YY
        int month = -1; int day = -1; int year = -1;
        try {
            month = Integer.parseInt(DOB.substring(0, 2));
            day = Integer.parseInt(DOB.substring(3, 5));
            year = Integer.parseInt(DOB.substring(6, 8));
        } catch (NumberFormatException nfe)
        { return new int[3]; }

        // Range is between 1950 and 2049
        if (year > 49) { year += 1900; }
        else { year += 2000; }

        if (month == -1 || day == -1 || year == -1)
        { return new int[3]; }

        // output format for SQL: YYYY/MM/DD
        return new int[]{year, month, day};
    }

    public void printUserPayStatements(int empID)
    {
        String selectQuery = "SELECT * FROM paystatements WHERE empID = " + empID + " ORDER BY payDate DESC";
        
        try (ResultSet rs = mySQLConnector.executeQuery(selectQuery))
        {
            System.out.println("");

            while (rs != null && rs.next())
            {
                String date = rs.getString("payDate");
                int amount = rs.getInt("payAmount");

                System.out.println(date + " - $" + amount);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
    }

    // =============================== SALARY UPDATE =============================== //

    public void printNumberOfSalariesWithinRange(String[] range)
    {
        int min = Integer.parseInt(range[0].replace("$", "").trim());
        int max = Integer.parseInt(range[1].replace("$", "").trim());
        String selectQuery = "SELECT p.empID, p.salary, e.firstName, e.lastName " +
                            "FROM payroll p " +
                            "JOIN employees e ON p.empID = e.empID " +
                            "WHERE p.salary >= " + min + " AND p.salary <= " + max + " " +
                            "AND p.leaveDate IS NULL";
        
        System.out.println("\n--- Salary Range Results ---");

        try (ResultSet rs = mySQLConnector.executeQuery(selectQuery))
        {
            while (rs != null && rs.next())
            {
                // Print old and new salary
                int currentSalary = rs.getInt("salary");
                String name = rs.getString("firstName") + " " + rs.getString("lastName");

                System.out.println("\n" + name + ":\n$" + currentSalary);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
    }

    public void printUpdatedSalariesWithinRange(String[] range, String percentage)
    {
        int min = Integer.parseInt(range[0].replace("$", "").trim());
        int max = Integer.parseInt(range[1].replace("$", "").trim());
        int pct = Integer.parseInt(percentage.replace("%", "").trim());
        float multiplier = pct / 100.0f;
        String selectQuery = "SELECT p.empID, p.salary, e.firstName, e.lastName " +
                            "FROM payroll p " +
                            "JOIN employees e ON p.empID = e.empID " +
                            "WHERE p.salary >= " + min + " AND p.salary <= " + max + " " +
                            "AND p.leaveDate IS NULL";
        
        System.out.println("\n--- Salary Update Results (+" + pct + "%) ---");

        try (ResultSet rs = mySQLConnector.executeQuery(selectQuery))
        {
            while (rs != null && rs.next())
            {
                // Print old and new salary
                int empID = rs.getInt("empID");
                int oldSalary = rs.getInt("salary");
                int newSalary = (int)(oldSalary * multiplier);
                String name = rs.getString("firstName") + " " + rs.getString("lastName");

                System.out.println("\n" + name + ":\n$" + oldSalary + " -> $" + newSalary);
            
                // Update each employee's salary
                String updateQuery = "UPDATE payroll" +
                                    " SET salary = " + newSalary +
                                    " WHERE empID = " + empID + " AND leaveDate IS NULL";
                mySQLConnector.writeQuery(updateQuery);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
    }

    // =============================== LABOR REPORT =============================== //

    public void printTotalPayForThisMonthByJobRole()
    {
        String sql = "SELECT " +
                        "r.jobTitle, " +
                        "SUM(ps.payAmount) AS totalLaborCost " +
                        "FROM paystatements ps " +
                        "JOIN employees e ON ps.empID = e.empID " +
                        "JOIN roles r ON e.roleID = r.roleID " +
                        "WHERE ps.payDate >= DATE_FORMAT(CURDATE(), '%Y-%m-01') " +
                        "AND ps.payDate <= LAST_DAY(CURDATE()) " +
                        "GROUP BY r.jobTitle " +
                        "ORDER BY totalLaborCost DESC";

        try (ResultSet rs = mySQLConnector.executeQuery(sql))
        {
            while (rs != null && rs.next())
            { System.out.println("\n" + rs.getString("jobTitle") + ": $" + rs.getInt("totalLaborCost")); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
    }

    public void printTotalPayForThisMonthByDivision()
    {
        String sql = "SELECT " +
                        "d.divisionName, " +
                        "SUM(ps.payAmount) AS totalLaborCost " +
                        "FROM paystatements ps " +
                        "JOIN employees e ON ps.empID = e.empID " +
                        "JOIN divisions d ON e.divID = d.divID " +
                        "WHERE ps.payDate >= DATE_FORMAT(CURDATE(), '%Y-%m-01') " +
                        "AND ps.payDate <= LAST_DAY(CURDATE()) " +
                        "GROUP BY d.divisionName " +
                        "ORDER BY totalLaborCost DESC";

        try (ResultSet rs = mySQLConnector.executeQuery(sql))
        {
            if (rs != null && rs.next())
            { System.out.println("\n" + rs.getString("divisionName") + ": $" + rs.getInt("totalLaborCost")); }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
    }

    // =============================== ADMIN ADD/REMOVE =============================== //

    public void printListOfAllAdmins()
    {
        String sql = "SELECT e.firstName, e.lastName, d.divisionName " +
             "FROM admins a " +
             "JOIN employees e ON a.empID = e.empID " +
             "JOIN divisions d ON e.divID = d.divID";

        try (ResultSet rs = mySQLConnector.executeQuery(sql))
        {
            while (rs != null && rs.next())
            {
                System.out.println("\nName: " + rs.getString("firstName") + " " + rs.getString("lastName")
                                    + " - Divsion: " + rs.getString("divisionName"));
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) { System.out.println("SQL Error: " + e.getMessage()); }
    }

    public void addAdmin(int empID)
    {
        String sql = "INSERT IGNORE INTO admins (empID) VALUES (" + empID + ")";

        mySQLConnector.writeQuery(sql);
    }

    public void removeAdmin(int empID)
    {
        String sql = "DELETE FROM admins WHERE empID = " + empID;

        mySQLConnector.writeQuery(sql);
    }
}
