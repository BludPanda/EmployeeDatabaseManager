public class PromptMenu extends Menu {

    private Prompt[] prompts;

    private String destinationMenuKey = "";

    public PromptMenu(String menuTitle, String menuHeader, Prompt[] prompts, String destinationMenuKey)
    {
        super(menuTitle, menuHeader, prompts.length);
        this.prompts = prompts;
        this.destinationMenuKey = destinationMenuKey;
    }

    @Override
    public PromptType getPromptType(int promptIndex)
    { return prompts[promptIndex].getPromptType(); }

    @Override
    public void activateMenu()
    {
        // Clear terminal when switching menus
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n" + "\u2500".repeat(50) + "\n");

        // Name & Instructions
        System.out.println(menuTitle + "\n");
        if (menuHeader.length() != 0) { System.out.println(menuHeader); }
    }

    @Override
    public void displayPrompt(int promptIndex)
    { System.out.print("\n" + prompts[promptIndex].getPromptText() + " -> "); }

    @Override
    public void enterPrompt(String prompt, int promptIndex)
    { prompts[promptIndex].setPromptAnswer(prompt);; }

    @Override
    public String getNextMenuKey()
    { return destinationMenuKey; }

    @Override
    public void setNextMenuKey(String key)
    { destinationMenuKey = key; }

    @Override
    public String[] getNextSqlQueryParams()
    {
        String[] menuParams = new String[prompts.length];
        for (int i = 0; i < menuParams.length; i++) {
            menuParams[i] = prompts[i].getPromptAnswer();
        }
        return menuParams;
    }
}
