
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;


public class UserInterface {

    // UI variables
    private static Scanner input = new Scanner(System.in);

    private static MenuCreator menuCreator = new MenuCreator();
    private static Map<String, Menu> menus = new Hashtable<>();
    private static Menu currentMenu;
    private static String currentInvalidPromptWarning = "";

    // Data handler -- SQL logic class
    private static DataHandler dataHandler = new DataHandler();

    // Variables reflecting the user generated during log in
    private static boolean isAdmin = false;
    private static int currentEmpID;

    // Temp variables for cross menu communication
    private static int currentSearchedEmpID;
    private static String[] currentSearchedParams;
    private static String[] currentSearchedSalaryRange;

    // =========================================================================== //

    // MAIN METHOD:

    // =========================================================================== //

    public static void main(String[] args) {

        // ============================================ //

        // Create menus and options
        menuCreator.createStartMenus(menus);

        // Start menu
        currentMenu = menus.get("start");

        // ============================================ //

        // Menu navigation is made with looping prompts
        Boolean validMenu = true;

        while (validMenu)
        {
            // Display menu
            currentMenu.activateMenu();

            // Data handler [if applicable] this is the SQL stuff
            runDataHandler();  
            
            // Display options if option menu
            currentMenu.showOptions();

            // Prompt
            Boolean validPrompt = false;
            String promptAnswer = "0";
            int numPrompts = currentMenu.getNumPrompts();

            for (int i = 0; i < numPrompts; i++)
            {
                // Ask for prompt
                currentMenu.displayPrompt(i);
                
                // Get prompt
                promptAnswer = input.nextLine();

                // Validate prompt
                validPrompt = validatePrompt(promptAnswer, currentMenu.getPromptType(i));

                if (validPrompt)
                { currentMenu.enterPrompt(promptAnswer, i); }

                // If invalid, refresh menu and redo prompt
                else
                {
                    currentMenu.activateMenu();
                    currentMenu.showOptions();
                    System.out.println("Warning: " + currentInvalidPromptWarning);
                    i--;
                }
            }

            // Retrieve next menu key
            String destinationMenuKey = currentMenu.getNextMenuKey();

            // Check if destination is "*ExitApp*"
            if (destinationMenuKey.contains("*ExitApp*"))
            { closeApplication(); return; }

            // Validate destination menu
            if (menus.get(destinationMenuKey) == null)
            { validMenu = false; break; }

            // Does next menu have a related SQL query?
            String sqlQueryKey = menus.get(destinationMenuKey).getSqlQueryKey();
  
            // Give params to next menu for data handling on next loop
            if (sqlQueryKey != "None")
            { menus.get(destinationMenuKey).setSqlQueryParams(currentMenu.getNextSqlQueryParams()); }

            // Go to next menu!
            currentMenu = menus.get(destinationMenuKey);
        }

        invalidMenuError();
    }

    // =========================================================================== //

    // SQL QUERY TRIGGER FOR MENUS:

    // =========================================================================== //

    // This method runs when a menu is linked with an SQL query, triggered when opening that menu. Previous prompt
    // answers can be fed into the upcoming menu!

    private static void runDataHandler()
    {
        String sqlQueryKey = currentMenu.getSqlQueryKey();

        if (sqlQueryKey != "None" && dataHandler == null)
        { invalidDataHandlerError(); }

        else if (sqlQueryKey != "None" && dataHandler != null)
        {
            System.out.println("----------------------------------------------------");

            String[] params = currentMenu.getSqlQueryParams(); // params are a list of the user's prompts from the previous menu

            switch(sqlQueryKey) {

                case "dummyMethod":
                    dataHandler.dummyMethod(params);
                    break;

                case "printUserEmployeeSummary":
                    dataHandler.printEmployeeSummary(currentEmpID);
                    break;

                case "printUserPayStatements":
                    dataHandler.printUserPayStatements(currentEmpID);
                    break;

                case "printSearchedEmployeeSummary":
                    dataHandler.printEmployeeSummary(currentSearchedEmpID);
                    break;

                case "printEditedEmployeeSummary":
                    dataHandler.printEditedEmployeeSummary(params, currentSearchedEmpID);
                    break;

                case "printNumberOfSalariesWithinRange":
                    currentSearchedSalaryRange = params;
                    dataHandler.printNumberOfSalariesWithinRange(params);
                    break;

                case "printUpdatedSalariesWithinRange":
                    dataHandler.printUpdatedSalariesWithinRange(currentSearchedSalaryRange, params[0]);
                    break;

                case "printTotalPayForThisMonthByJobRole":
                    dataHandler.printTotalPayForThisMonthByJobRole();
                    break;

                case "printTotalPayForThisMonthByDivision":
                    dataHandler.printTotalPayForThisMonthByDivision();
                    break;

                case "printListOfAllAdmins":
                    dataHandler.printListOfAllAdmins();
                    break;
            }
            
            System.out.println("\n----------------------------------------------------");
        }
    }

    // =========================================================================== //

    // ERROR & CLOSE PROGRAM:

    // =========================================================================== //

    private static void invalidMenuError()
    {
        // Close scanner and display error
        input.close();
        System.out.print("ERROR: Invalid menu reached. Make sure all Menu objects are subclass not abstract. Make sure all menus return a destination menu key.");
    }

    private static void invalidDataHandlerError()
    {
        System.out.print("ERROR: Data Handler key set for the next menu is not contained in the stored data handlers dictionary.");
    }

    private static void closeApplication()
    {
        // Close scanner
        input.close();

        // Clear terminal
        System.out.print("\f");
        System.out.println("\n" + "\u2500".repeat(50) + "\n");

        // Clear terminal
        System.out.print("Application Closed");
    }

    // =========================================================================== //

    // PROMPT VALIDATION:

    // =========================================================================== //

    private static Boolean validatePrompt(String prompt, PromptType promptType)
    {
        currentInvalidPromptWarning = "";
        
        Boolean validPrompt = false;
        if (prompt == null) { return false; }

        switch(promptType) {

            // =============== BASIC =============== //

            case PromptType.Index:
                if (prompt.length() == 1 || prompt.length() == 2) 
                {
                    try {
                        int num = Integer.parseInt(prompt);

                        if (num >= 0 && currentMenu.validPromptIndex(num))
                        { validPrompt = true; }
                    } catch (NumberFormatException nfe) {}
                }
                if (!validPrompt) { currentInvalidPromptWarning = "'" + prompt + "' is not a valid index [#]"; }
                break;

            case PromptType.Next:
                validPrompt = true;
                break;

            case PromptType.Text:
                validPrompt = true;
                break;

            // =============== LOGIN =============== //

            case PromptType.Username:
                if (prompt.length() < 50 && prompt.length() > 0) 
                { 
                    if (prompt.contains(" ") && prompt.indexOf(" ") != prompt.length() - 1)
                    { 
                        int empID = dataHandler.verifyUsername(prompt);
                        if (empID != -1)
                        {
                            currentEmpID = empID;
                            validPrompt = true;
                        }
                        else { currentInvalidPromptWarning = "Username not found"; }
                    }
                    else { currentInvalidPromptWarning = "Username must be a first and last named separated by a space"; }
                }
                else { currentInvalidPromptWarning = "Username is too long or short"; }
                break;

            case PromptType.Password:
                if (prompt.length() < 50 && prompt.length() > 0) 
                { 
                    if (dataHandler.verifyPassword(currentEmpID, prompt))
                    {
                        //Once logged in create general menus or admin menus
                        isAdmin = dataHandler.checkAdmin(currentEmpID);
                        if (isAdmin) {
                            menuCreator.createAdminMenus(menus);
                            currentMenu.setNextMenuKey("primaryAdmin");
                        }
                        else {
                            menuCreator.createGeneralMenus(menus);
                            currentMenu.setNextMenuKey("primaryGeneral");
                        }
                        
                        validPrompt = true;
                    }
                }
                if (!validPrompt) { currentInvalidPromptWarning = "Password is incorrect"; }
                break;

            // =============== EMPLOYEE SEARCH =============== //

            case PromptType.SearchEmpName:
                currentSearchedParams = new String[] { "next", "next", "next", "next" };    
            
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if  (prompt.length() < 50 && prompt.length() > 0) 
                { 
                    if (prompt.contains(" ") && prompt.indexOf(" ") != prompt.length() - 1)
                    {
                        currentSearchedParams[0] = prompt;
                        validPrompt = true;
                    }
                    else { currentInvalidPromptWarning = "Name must contain space between first and last name"; }
                }
                else { currentInvalidPromptWarning = "Name is too long or short"; }
                break;

            case PromptType.SearchEmpDateOfBirth:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if (prompt.length() == 8 && prompt.contains("/")) 
                { 
                    try {
                        int month = Integer.parseInt(prompt.substring(0, 2));
                        int day = Integer.parseInt(prompt.substring(3, 5));
                        int year = Integer.parseInt(prompt.substring(6, 8));

                        if (month >= 0 && month <= 12 && day > 0 && day < 31 && year > -1 && year < 100)
                        {
                            currentSearchedParams[1] = prompt;
                            validPrompt = true;
                        }
                    } catch (NumberFormatException nfe) {}
                }
                if (!validPrompt) { currentInvalidPromptWarning = "Date must be in this format: MM/DD/YY and contain possible dates"; }
                break;

            case PromptType.SearchEmpSSN:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if (prompt.length() == 9) 
                { 
                    try {
                        int num = Integer.parseInt(prompt);
                        currentSearchedParams[2] = prompt;
                        validPrompt = true;
                    } catch (NumberFormatException nfe) {}
                }
                if (!validPrompt) { currentInvalidPromptWarning = "SSN must be 9 characters long and consist of only numbers \n Don't include dashes"; }
                break;
            
            case PromptType.SearchEmpID:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if (prompt.length() > 0) 
                {
                    try {
                        int num = Integer.parseInt(prompt);
                        if (num >= 0)
                        {
                            currentSearchedParams[3] = prompt;
                            validPrompt = true;
                        }
                    } catch (NumberFormatException nfe) {}
                }
                else { currentInvalidPromptWarning = "EmpID must be a number bigger than 0"; }

                if (validPrompt) // Result of employee search
                {
                    currentSearchedEmpID = dataHandler.checkIfEmployeeExists(currentSearchedParams);
                    if (currentSearchedEmpID < 0)
                    {
                        if (currentSearchedEmpID == -1)
                        { currentMenu.setNextMenuKey("searchEmpErrorNoEmployeesFound"); }
                        if (currentSearchedEmpID == -2)
                        { currentMenu.setNextMenuKey("searchEmpErrorMultipleEmployeesFound"); }
                        if (currentSearchedEmpID == -3)
                        { currentMenu.setNextMenuKey("searchEmpErrorNoSearchInfo"); }
                    }
                    else
                    { currentMenu.setNextMenuKey("searchedEmployeeSummary"); }
                }

                break;

            // =============== ADMIN SEARCH =============== //

            case PromptType.RemoveAdminEmpID:
                if (prompt.length() > 0) 
                {
                    try {
                        int num = Integer.parseInt(prompt);
                        if (num >= 0)
                        {
                            if (num != currentEmpID)
                            {
                                String[] newParams = new String[] {"next", "next", "next", prompt};
                                currentSearchedEmpID = dataHandler.checkIfEmployeeExists(newParams);

                                // Result of admin search
                                if (currentSearchedEmpID < 0)
                                {
                                    if (currentSearchedEmpID == -1)
                                    { currentMenu.setNextMenuKey("searchAdminErrorNoEmployeesFound"); }
                                }
                                else
                                {
                                    dataHandler.removeAdmin(currentSearchedEmpID);
                                    currentMenu.setNextMenuKey("adminList");
                                }

                                validPrompt = true;
                            }
                            else { currentInvalidPromptWarning = "Cannot remove yourself"; }
                        }
                        else { currentInvalidPromptWarning = "EmpID must be bigger or equal to 0"; }
                    } catch (NumberFormatException nfe) {}
                }
                else { currentInvalidPromptWarning = "EmpID must be a number"; }
                break;

            case PromptType.AddAdminEmpID:
                if (prompt.length() > 0) 
                {
                    try {
                        int num = Integer.parseInt(prompt);
                        if (num >= 0)
                        {
                            if (num != currentEmpID)
                            {
                                String[] newParams = new String[] {"next", "next", "next", prompt};
                                currentSearchedEmpID = dataHandler.checkIfEmployeeExists(newParams);

                                // Result of admin search
                                if (currentSearchedEmpID < 0)
                                {
                                    if (currentSearchedEmpID == -1)
                                    { currentMenu.setNextMenuKey("searchAdminErrorNoEmployeesFound"); }
                                }
                                else
                                {
                                    dataHandler.addAdmin(currentSearchedEmpID);
                                    currentMenu.setNextMenuKey("adminList");
                                }

                                validPrompt = true;
                            }
                            else { currentInvalidPromptWarning = "Cannot remove yourself"; }
                        }
                        else { currentInvalidPromptWarning = "EmpID must be bigger or equal to 0"; }
                    } catch (NumberFormatException nfe) {}
                }
                else { currentInvalidPromptWarning = "EmpID must be a number"; }
                break;

            // =============== SALARY =============== //

            case PromptType.SalaryRangeLower:
                if (prompt.length() > 0) 
                {
                    if (prompt.indexOf("$") == 0) { prompt = prompt.substring(1); }
                    try {
                        int num = Integer.parseInt(prompt);
                        if (num >= 0)
                        {
                            currentSearchedSalaryRange = new String[2];
                            currentSearchedSalaryRange[0] = prompt;
                            validPrompt = true;
                        }
                    } catch (NumberFormatException nfe) {}
                }
                if (!validPrompt) { currentInvalidPromptWarning = "Enter a valid value like '$30000, no commas'"; }
                break;

            case PromptType.SalaryRangeUpper:
                if (prompt.length() > 0) 
                {
                    if (prompt.indexOf("$") == 0) { prompt = prompt.substring(1); }
                    try {
                        int num = Integer.parseInt(prompt);
                        if (num >= 0)
                        {
                            currentSearchedSalaryRange[1] = prompt;
                            validPrompt = true;
                        }
                    } catch (NumberFormatException nfe) {}
                }
                if (!validPrompt) { currentInvalidPromptWarning = "Enter a valid value like '$30000, no commas'"; }
                break;

            case PromptType.Percentage:
                if (prompt.length() > 0) 
                {
                    if (prompt.contains("%")) { prompt = prompt.substring(0, prompt.indexOf("%")); }
                    try {
                        int num = Integer.parseInt(prompt);
                        if (num >= 1 && num <= 500)
                        { validPrompt = true; }
                    } catch (NumberFormatException nfe) {}
                }
                if (!validPrompt) { currentInvalidPromptWarning = "Enter a valid percentage like '40%'"; }
                break;

            // =============== LABOR REPORT =============== //

            case PromptType.JobRoleOrDivision:
                if  (prompt.length() < 50 && prompt.length() > 0) 
                { validPrompt = true; }
                if (!validPrompt) { currentInvalidPromptWarning = "Title is too long or short"; }
                break;

            // =============== EDIT EMPLOYEE =============== //

            case PromptType.EditName:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if (prompt.length() < 50 && prompt.length() > 0) 
                { 
                    if (prompt.contains(" ") && prompt.indexOf(" ") != prompt.length() - 1)
                    { validPrompt = true; }
                    else { currentInvalidPromptWarning = "Name must be a first and last named separated by a space"; }
                }
                else { currentInvalidPromptWarning = "Name is too long or short"; }
                break;
            
            case PromptType.EditJobRoleOrDivision:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if  (prompt.length() < 100 && prompt.length() > 0) 
                { validPrompt = true; }
                if (!validPrompt) { currentInvalidPromptWarning = "Title is too long or short"; }
                break;

            case PromptType.EditEmail:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if  (prompt.length() < 100 && prompt.length() > 0) 
                { validPrompt = true; }
                if (!validPrompt) { currentInvalidPromptWarning = "Email is too long or short"; }
                break;

            case PromptType.EditPhone:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if  (prompt.length() == 12) 
                { validPrompt = true; }
                if (!validPrompt) { currentInvalidPromptWarning = "Input number in the format ###-###-####"; }
                break;

            case PromptType.EditAddressStreet:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if  (prompt.length() < 50 && prompt.length() > 0) 
                { validPrompt = true; }
                if (!validPrompt) { currentInvalidPromptWarning = "Street is too long or short"; }
                break;

            case PromptType.EditAddressCity:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if  (prompt.length() < 50 && prompt.length() > 0) 
                { validPrompt = true; }
                if (!validPrompt) { currentInvalidPromptWarning = "City is too long or short"; }
                break;

            case PromptType.EditAddressState:
                if (prompt.equals("next")) 
                { validPrompt = true; }
                else if  (prompt.length() == 2 && dataHandler.allStates.contains(prompt)) 
                { validPrompt = true; }
                if (!validPrompt) { currentInvalidPromptWarning = "State must be a two letter captial abbreviation"; }
                break;

            }

        return validPrompt;
    }
}
