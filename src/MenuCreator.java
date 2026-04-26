import java.util.Map;

public class MenuCreator {

    public static void createStartMenus(Map<String, Menu> menus)
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
    }

    public static void createGeneralMenus(Map<String, Menu> menus)
    {
        Option[] options;
        Prompt[] prompts;

        // PRIMARY GENERAL
        options = new Option[] {
            new Option("Your Employee Summary", "userEmployeeSummary"),
            new Option("Your Pay Statements", "payStatements"),
            new Option("Switch User", "start")
        };
        Menu primaryGeneral = new OptionMenu(
            "EMPLOYEE MANAGEMENT SYSTEM", 
            "Welcome Employee", 
            options);
        menus.put("primaryGeneral", primaryGeneral);

            // USER EMPLOYEE SUMMARY
            prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back")
            };
            Menu userEmployeeSummary = new PromptMenu(
                "EMPLOYEE SUMMARY", 
                "",
                prompts,
                "primaryGeneral");
            userEmployeeSummary.setSqlQueryKey("printUserEmployeeSummary");
            menus.put("userEmployeeSummary", userEmployeeSummary);

            // PAY STATEMENTS
            prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back")
            };
            Menu payStatements = new PromptMenu(
                "EMPLOYEE PAY STATEMENTS", 
                "",
                prompts,
                "primaryGeneral");
            payStatements.setSqlQueryKey("printUserPayStatements");
            menus.put("payStatements", payStatements);

            // TEST PROMPTS
            prompts = new Prompt[] {
                new Prompt(PromptType.Text, "Enter Text"),
                new Prompt(PromptType.Percentage, "Enter A Percentage%")
            };
            Menu testPrompts = new PromptMenu(
                "This is to test prompting", 
                "...",
                prompts,
                "test");
            menus.put("testPrompts", testPrompts);
            
                // TEST
                prompts = new Prompt[] {
                    new Prompt(PromptType.Next, "Enter To Go Back")
                };
                Menu test = new PromptMenu(
                    "This is to test the data handler", 
                    "...",
                    prompts,
                    "primaryGeneral");
                test.setSqlQueryKey("dummyMethod");
                menus.put("test", test);
    }

    public static void createAdminMenus(Map<String, Menu> menus)
    {
        Option[] options;
        Prompt[] prompts;

        // PRIMARY ADMIN
        options = new Option[] {
            new Option("Your Employee Summary", "userEmployeeSummary"),
            new Option("Your Pay Statements", "payStatements"),
            new Option("Search Employee", "searchEmployeePrompts"),
            new Option("Update Salaries", "updateSalariesRangePrompts"),
            new Option("Labor Report", "laborReport"),
            new Option("Administration", "adminList"),
            new Option("Recent Hires", "recentHires"),
            new Option("Switch User", "start"),
        };
        Menu primaryAdmin = new OptionMenu(
            "EMPLOYEE MANAGEMENT SYSTEM", 
            "Welcome Admin", 
            options);
        menus.put("primaryAdmin", primaryAdmin);

            // USER EMPLOYEE SUMMARY
            prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back")
            };
            Menu userEmployeeSummary = new PromptMenu(
                "EMPLOYEE SUMMARY", 
                "",
                prompts,
                "primaryAdmin");
            userEmployeeSummary.setSqlQueryKey("printUserEmployeeSummary");
            menus.put("userEmployeeSummary", userEmployeeSummary);

            // PAY STATEMENTS
            prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back")
            };
            Menu payStatements = new PromptMenu(
                "EMPLOYEE PAY STATEMENTS", 
                "",
                prompts,
                "primaryAdmin");
            payStatements.setSqlQueryKey("printUserPayStatements");
            menus.put("payStatements", payStatements);

            // SEARCH EMPLOYEE PROMPTS
            prompts = new Prompt[] {
                new Prompt(PromptType.SearchEmpName, "Enter Name"),
                new Prompt(PromptType.SearchEmpDateOfBirth, "Enter DOB"),
                new Prompt(PromptType.SearchEmpSSN, "Enter SSN"),
                new Prompt(PromptType.SearchEmpID, "Enter EmpID"),
            };
            Menu searchEmployeePrompts = new PromptMenu(
                "SEARCH FOR EMPLOYEE", 
                "Search using Name, DOB, SSN, or EmpID, 'next' to skip",
                prompts,
                "searchedEmployeeSummary");
            menus.put("searchEmployeePrompts", searchEmployeePrompts);

                // SEARCH ERRORS

                // No search results error
                prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back") };
                Menu searchEmpErrorNoEmployeesFound = new PromptMenu("SEARCH ERROR",
                "No employees found", prompts,
                    "primaryAdmin");
                menus.put("searchEmpErrorNoEmployeesFound", searchEmpErrorNoEmployeesFound);
                
                // Multiple search error info
                prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back") };
                Menu searchEmpErrorMultipleEmployeesFound = new PromptMenu("SEARCH ERROR",
                "Multiple employees found, refine your search", prompts,
                    "primaryAdmin");
                menus.put("searchEmpErrorMultipleEmployeesFound", searchEmpErrorMultipleEmployeesFound);

                // No search info error
                prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back") };
                Menu searchEmpErrorNoSearchInfo = new PromptMenu("SEARCH ERROR",
                "No search info was provided", prompts,
                    "primaryAdmin");
                menus.put("searchEmpErrorNoSearchInfo", searchEmpErrorNoSearchInfo);

                // VIEW SEARCHED EMPLOYEE SUMMARY
                options = new Option[] {
                    new Option("Edit Employee", "editEmployeePrompts"),
                    new Option("Go Back", "primaryAdmin"),
                };
                Menu searchedEmployeeSummary = new OptionMenu(
                    "SEARCHED EMPLOYEE", 
                    "", 
                    options);
                searchedEmployeeSummary.setSqlQueryKey("printSearchedEmployeeSummary");
                menus.put("searchedEmployeeSummary", searchedEmployeeSummary);
                    
                    // EDIT EMPLOYEE PROMPTS
                    prompts = new Prompt[] {
                        new Prompt(PromptType.EditName, "Enter New Name"),
                        new Prompt(PromptType.EditJobRoleOrDivision, "Enter New Job Role"),
                        new Prompt(PromptType.EditJobRoleOrDivision, "Enter New Division"),
                        new Prompt(PromptType.EditEmail, "Enter New Email"),
                        new Prompt(PromptType.EditPhone, "Enter New Phone"),
                        new Prompt(PromptType.EditAddressStreet, "Enter Address Street"),
                        new Prompt(PromptType.EditAddressCity, "Enter Address City"),
                        new Prompt(PromptType.EditAddressState, "Enter Address State")
                    };
                    Menu editEmployeePrompts = new PromptMenu(
                        "EDIT EMPLOYEE", 
                        "Edit basic information, 'next' to skip",
                        prompts,
                        "editedEmployeeSummary");
                    menus.put("editEmployeePrompts", editEmployeePrompts);

                        // EDIT EMPLOYEE SUMMARY
                        prompts = new Prompt[] {
                            new Prompt(PromptType.Next, "Enter To Go Back")
                        };
                        Menu editedEmployeeSummary = new PromptMenu(
                            "EDITED EMPLOYEE", 
                            "",
                            prompts,
                            "primaryAdmin");
                        editedEmployeeSummary.setSqlQueryKey("printEditedEmployeeSummary");
                        menus.put("editedEmployeeSummary", editedEmployeeSummary);

            // UPDATE SALARIES RANGE PROMPTS
            prompts = new Prompt[] {
                new Prompt(PromptType.SalaryRangeLower, "Enter Lower Bound"),
                new Prompt(PromptType.SalaryRangeUpper, "Enter Upper Bound")
            };
            Menu updateSalariesRangePrompts = new PromptMenu(
                "UPDATE SALARIES", 
                "You can update multiple salaries by providing a range of salaries to edit",
                prompts,
                "viewSalariesInRange");
            menus.put("updateSalariesRangePrompts", updateSalariesRangePrompts);

                // VIEW SALARIES IN RANGE
                options = new Option[] {
                    new Option("Update These Salaries", "updateSalariesPercentPrompt"),
                    new Option("Try New Range", "updateSalariesRangePrompts"),
                    new Option("Go Back", "primaryAdmin"),
                };
                Menu viewSalariesInRange = new OptionMenu(
                    "EMPLOYEE SALARIES WITHIN RANGE", 
                    "", 
                    options);
                viewSalariesInRange.setSqlQueryKey("printNumberOfSalariesWithinRange");
                menus.put("viewSalariesInRange", viewSalariesInRange);

                    // UPDATE SALARIES 
                    prompts = new Prompt[] {
                        new Prompt(PromptType.Percentage, "Enter Percentage To Change By")
                    };
                    Menu updateSalariesPercentPrompt = new PromptMenu(
                        "UPDATE SALARIES", 
                        "What percentage would you like to increase/decrease?",
                        prompts,
                        "viewUpdatedSalaries");
                    menus.put("updateSalariesPercentPrompt", updateSalariesPercentPrompt);

                        // VIEW UPDATED SALARIES 
                        prompts = new Prompt[] {
                            new Prompt(PromptType.Next, "Enter To Go Back")
                        };
                        Menu viewUpdatedSalaries = new PromptMenu(
                            "UPDATED SALARIES", 
                            "",
                            prompts,
                            "primaryAdmin");
                        viewUpdatedSalaries.setSqlQueryKey("printUpdatedSalariesWithinRange");
                        menus.put("viewUpdatedSalaries", viewUpdatedSalaries);

            // LABOR REPORT
            options = new Option[] {
                new Option("Report by Job Role", "viewLaborReportByJobRole"),
                new Option("Report by Division", "viewLaborReportByDivision"),
                new Option("Go Back", "primaryAdmin"),
            };
            Menu laborReport = new OptionMenu(
                "LABOR REPORT", 
                "View total employee pay for this month by either job role or division", 
                options);
            menus.put("laborReport", laborReport);

                // VIEW LABOR REPORT JOB ROLE
                prompts = new Prompt[] {
                    new Prompt(PromptType.Next, "Enter To Go Back")
                };
                Menu viewLaborReportByJobRole = new PromptMenu(
                    "LABOR REPORT BY JOB ROLE", 
                    "Total labor cost this on-going month:",
                    prompts,
                    "laborReport");
                viewLaborReportByJobRole.setSqlQueryKey("printTotalPayForThisMonthByJobRole");
                menus.put("viewLaborReportByJobRole", viewLaborReportByJobRole);

                // VIEW LABOR REPORT JOB ROLE PROMPT
                prompts = new Prompt[] {
                    new Prompt(PromptType.Next, "Enter To Go Back")
                };
                Menu viewLaborReportByDivision = new PromptMenu(
                    "LABOR REPORT BY DIVISION", 
                    "Total labor cost this on-going month:",
                    prompts,
                    "laborReport");
                viewLaborReportByDivision.setSqlQueryKey("printTotalPayForThisMonthByDivision");
                menus.put("viewLaborReportByDivision", viewLaborReportByDivision);

            // ADMIN LIST
            options = new Option[] {
                new Option("Add Admin", "addAdmin"),
                new Option("Remove Admin", "removeAdmin"),
                new Option("Go Back", "primaryAdmin"),
            };
            Menu adminList = new OptionMenu(
                "ADMIN LISTING", 
                "All employees who have admin access like you", 
                options);
            adminList.setSqlQueryKey("printListOfAllAdmins");
            menus.put("adminList", adminList);

                // REMOVE ADMIN PROMPT
                prompts = new Prompt[] {
                    new Prompt(PromptType.RemoveAdminEmpID, "Enter EmpID"),
                };
                Menu removeAdmin = new PromptMenu(
                    "REMOVE ADMIN", 
                    "Search for employee to remove using empID",
                    prompts,
                    "TBD");
                menus.put("removeAdmin", removeAdmin);

                // ADD ADMIN PROMPT
                prompts = new Prompt[] {
                    new Prompt(PromptType.AddAdminEmpID, "Enter EmpID"),
                };
                Menu addAdmin = new PromptMenu(
                    "ADD ADMIN", 
                    "Search for employee to add using empID",
                    prompts,
                    "TBD");
                menus.put("addAdmin", addAdmin);

                // SEARCH ERRORS

                // No search results error
                prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back") };
                Menu searchAdminErrorNoEmployeesFound = new PromptMenu("SEARCH ERROR",
                "No employees found", prompts,
                    "adminList");
                menus.put("searchAdminErrorNoEmployeesFound", searchAdminErrorNoEmployeesFound);

            // RECENT HIRES
            prompts = new Prompt[] {
                new Prompt(PromptType.Next, "Enter To Go Back") };
            Menu recentHires = new PromptMenu(
                "RECENTLY HIRED EMPLOYEES",
                "Recent hires in past 60 days: ", 
                prompts, 
                "primaryAdmin");
            recentHires.setSqlQueryKey("printRecentHires");
            menus.put("recentHires", recentHires);
    }
}
