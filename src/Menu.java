
public abstract class Menu {
    protected String menuTitle;
    protected String menuHeader;
    private int numPrompts = 1;

    private String dataHandlerKey = "None";
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

    public PromptType getPromptType(int promptIndex)
    { return null; }

    public int getNumPrompts()
    { return numPrompts; }

    public String getMenuName()
    { return menuTitle; }

    public String getDataHandlerKey()
    { return dataHandlerKey; }

    public void setDataHandlerKey(String dataHandlerKey)
    { this.dataHandlerKey = dataHandlerKey; }

    public String[] getMenuDataHandlerParams()
    { return dataHandlerParams; }

    public void setMenuDataHandlerParams(String[] params)
    { dataHandlerParams = params; }

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

    public String[] getNextMenuDataHandlerParams()
    { return new String[0]; }
}
