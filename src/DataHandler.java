
public class DataHandler {
    
    protected static SQLConnector mySQLConnector = new SQLConnector();

    public String allStates = """
    AL, AK, AZ, AR, CA, CO, CT, DE, FL, GA, HI, ID, IL, IN, IA, KS, 
    KY, LA, ME, MD, MA, MI, MN, MS, MO, MT, NE, NV, NH, NJ, NM, NY,
    NC, ND, OH, OK, OR,PA, RI, SC, SD, TN, TX, UT, VT, VA, WA, WV,
    WI, WY""";
    private int[] currentFormattedDate = new int[]{4, 16, 2026};

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
        } catch (SQLException e) { e.printStackTrace(); }
        */

        System.out.println("This is just to show an option triggers the Data Handler!");
        System.out.println("These are the prompts entered in the last menu: ");
        for (String p : params) { System.out.println(p); }
    }

    // =============================== VERIFICATION =============================== //

    public int verifyUsername(String name)
    {
        // name = "first last" *includes space*
        // return the empID of the matching employee else return -1
        
        return 1; //temp return
    }

    public boolean verifyPassword(int empID, String password)
    {
        return true; //temp return
    }

    public boolean checkAdmin(int empID)
    {
        return true; //temp return
    }

    // =============================== EMPLOYEE SUMMARY & PAY STATEMENTS =============================== //

    public void printEmployeeSummary(int empID)
    {
        // Employee summary for the user or for a searched employee
        // Probably go over basic information about employee -- include stuff that is editable [method below]
        // Also maybe include whether they are currently working
    }

    public void printEditedEmployeeSummary(String[] editPrompts, int empID)
    {
        // ***** This is probably the most complicated method *****

        // You are saving the new employee info to the database and then printing out an edited summary. Feel free to use
        // the printEmployeeSummary() method, but you may need to print extra info.

        // params = [name [first last] *includes space*, division, jobRole, email, phone (###)-###-####, address street, address city, address state *abbreviated*]
            /*
            NGL I'm super tired so I didn't do a ton of prompt verification on this. All I checked for was that the
            prompts had values, name had a space, that phone was 14 characters, and that the state was a real state abbreviation. If it can
            successfully be inputted into the database then who cares. if some parameters don't work or the user has skipped
            them [parameter = "next"] then maybe print that they weren't changed. If the values given for job role or
            division don't exist in the database I guess it's up to you if you want to create them.

            Oh and fuck the zip code
            */
    }

    public int checkIfEmployeeExists(String[] searchPrompts)
    {
        // ***** This is probably the second most complicated method *****
        // Search and display the employee by using the 4 prompts: { name [first last] *includes space*, DOB, SSN, EmpID]

            /*  The prompts are able to be skipped [indicated by = "next"] so you have to check different combinations of
                the prompts. But there are some short cuts. If the empID and or SNN [totally unique] is given then we know that
                if they dont exist then the rest of the prompts don't natter. But if only the name or DOB is given then you
                need to check if an employee exists as well as if multiple employees match since names and DOB can overlap. If
                there are more than one match return -2. */

        // Use formatDateFromPrompt() to help! [month, day, year] -- could potentially return an empty array in a very rare case

        // Return -1 if the employee does not exist
        // Return -2 if there are multiple matches
        // Return -3 if all prompts are "next" / no info to search with

        return 1; //temp return
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
        { return new int[0]; }

        // Range is between 1950 and 2049
        if (year > 49) { year += 1900; }
        else { year += 2000; }

        if (month == -1 || day == -1 || year == -1)
        { return new int[0]; } // Really should never happen if input is formatted right
        return new int[]{month, day, year};
    }

    public void printUserPayStatements(int empID)
    {
        
    }

    // =============================== SALARY UPDATE =============================== //

    public void printNumberOfSalariesWithinRange(String[] range)
    {
        // params = [$number, $number] -- may or may not include the $ symbol
    }

    public void printUpdatedSalariesWithinRange(String[] range, String percentage)
    {
        // params = [$number, $number, number%] -- may or may not include the % symbol
        // Percentage is from 1% <= x <= 500%
        // this method both updates the values in the database and prints the resulting employees changed.
        // Result could just be employee names then their previous salary -> new salary
    }

    // =============================== LABOR REPORT =============================== //

    public void printTotalPayForThisMonthByJobRole(String jobRole)
    {
        // one of the more complex methods
        // if not found then print warning
    }

    public void printTotalPayForThisMonthByDivision(String division)
    {
        // one of the more complex methods
        // if not found then print warning
    }

    // =============================== ADMIN ADD/REMOVE =============================== //

    public void printListOfAllAdmins()
    {
        
    }

    public void addAdmin(int empID)
    {
        // Do not print anything here, just update database
        // if they already had admin do nothing
    }

    public void removeAdmin(int empID)
    {
        // Do not print anything here, just update database
        // if they already didn't have admin do nothing
    }
}
