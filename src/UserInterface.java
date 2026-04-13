
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;


public class UserInterface {

    static Scanner input = new Scanner(System.in);

    static Map<String, Menu> menus = new Hashtable<>();
    static Menu currentMenu;

    static Map<String, DataHandler> dataHandlers = new Hashtable<>();
    

    public static void main(String[] args) {

        // ============================================ //

        // Create menus and options
        createMenus();
        createDataHandlers();

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
                    System.out.println("Input is not valid, please try again...");
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

            // Does next menu have a related data handler?
            String dataHandlerKey = menus.get(destinationMenuKey).getDataHandlerKey();
  
            // Give params to next menu for data handling on next loop
            if (dataHandlerKey != "None")
            { menus.get(destinationMenuKey).setMenuDataHandlerParams(currentMenu.getNextMenuDataHandlerParams()); }

            // Go to next menu!
            currentMenu = menus.get(destinationMenuKey);
        }

        invalidMenuError();
    }

    private static void runDataHandler()
    {
        String dataHandlerKey = currentMenu.getDataHandlerKey();
        DataHandler dataHandler = dataHandlers.get(dataHandlerKey);

        if (dataHandlerKey != "None" && dataHandler == null)
        { invalidDataHandlerError(); }

        else if (dataHandlerKey != "None" && dataHandler != null)
        {
            // Trigger data handler / SQL output would go here
            System.out.println("===================================");
            //dataHandler.executeAndReport(currentMenu.getMenuDataHandlerParams());
            System.out.println("===================================");
        }
    }

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

    private static void createMenus()
    {
        Option[] options;
        Prompt[] prompts;

        // START
        options = new Option[] {
            new Option("Login", "login"),
            new Option("Exit Application", "*ExitApp*"),
        };
        Menu start = new OptionMenu(
            "Employee Management System", 
            "Login To Start", 
            options);
        menus.put("start", start);

        // LOGIN
        prompts = new Prompt[] {
            new Prompt(PromptType.Username, "Enter Username"),
            new Prompt(PromptType.Password, "Enter Password"),
        };
        Menu login = new PromptMenu(
            "Login In To System", 
            "Enter Your Username & Password", 
            prompts,
            "primaryGeneral");
        menus.put("login", login);

        // PRIMARY GENERAL
        options = new Option[] {
            new Option("TEST DATA HANDLER", "test"),
            new Option("Your Employee Summary", "NA"),
            new Option("Your Pay Statements", "NA"),
            new Option("Switch User", "NA")
        };
        Menu primaryGeneral = new OptionMenu(
            "Employee Management System", 
            "Welcome Employee", 
            options);
        menus.put("primaryGeneral", primaryGeneral);

        // TESTING DATA HANDLER
        prompts = new Prompt[] {
            new Prompt(PromptType.Next, "Enter To Go Back"),
        };
        Menu test = new PromptMenu(
            "This is to test that the data handler is triggered", 
            "...",
            prompts,
            "primaryGeneral");
        test.setDataHandlerKey("test");
        menus.put("test", test);
    }

    private static void createDataHandlers()
    {
        // TEST
        DataHandler test = new DataHandler_Test();
        dataHandlers.put("test", test);
    }

    // =========================================================================== //

    private static Boolean validatePrompt(String prompt, PromptType promptType)
    {
        Boolean validPrompt = false;
        if (prompt == null) { return false; }

        switch(promptType) {
            case PromptType.Index:
                if (prompt.length() == 1 || prompt.length() == 2) 
                {
                    try {
                        int num = Integer.parseInt(prompt);

                        if (num >= 0 && currentMenu.validPromptIndex(num))
                        { validPrompt = true; }
                    } catch (NumberFormatException nfe) {}
                }
                break;
            case PromptType.GenericName:
                if (prompt.length() < 50 && prompt.length() > 0) 
                { 
                    validPrompt = true;
                }
                break;
            case PromptType.Username:
                if (prompt.length() < 50 && prompt.length() > 0) 
                { 
                    // Add more rules here
                    validPrompt = true;
                }
                break;
            case PromptType.Password:
                if (prompt.length() < 50 && prompt.length() > 0) 
                { 
                    // Add more rules here
                    validPrompt = true;
                }
                break;
            case PromptType.Next:
                validPrompt = true;
                break;
            }

        return validPrompt;
    }
}
