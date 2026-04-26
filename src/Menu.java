
public abstract class Menu {
    protected String menuTitle;
    protected String menuHeader;
    private int numPrompts = 1;

    private String sqlQueryKey = "None";
    private String[] dataHandlerParams;

    public Menu(String menuTitle, String menuHeader)
    {
        this.menuTitle = menuTitle;
        this.menuHeader = menuHeader;
    }

    public Menu(String menuTitle, String menuHeader, int numPrompts)
    { 
        this.menuTitle = menuTitle;
        this.menuHeader = menuHeader;
        this.numPrompts = numPrompts;
    }

    // ================================================= //

    public PromptType getPromptType(int promptIndex)
    { return null; }

    public int getNumPrompts()
    { return numPrompts; }

    public String getMenuName()
    { return menuTitle; }

    public void activateMenu()
    { }

    public void displayPrompt(int promptIndex)
    { }

    public Boolean validPromptIndex(int index)
    { return false; }

    public void enterPrompt(String prompt, int promptIndex)
    { }

    public String getNextMenuKey()
    { return ""; }

    public void setNextMenuKey(String key)
    { }

    public void showOptions()
    { }

    // ================================================= //

    public String getSqlQueryKey()
    { return sqlQueryKey; }

    public void setSqlQueryKey(String SqlQueryKey)
    { this.sqlQueryKey = SqlQueryKey; }

    public String[] getSqlQueryParams()
    { return dataHandlerParams; }

    public void setSqlQueryParams(String[] params)
    { dataHandlerParams = params; }

    public String[] getNextSqlQueryParams()
    { return new String[0]; }
}
