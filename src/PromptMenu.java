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
        System.out.print("\f");
        System.out.println("\n" + "\u2500".repeat(50) + "\n");

        // Name & Instructions
        System.out.println(menuTitle + "\n");
        System.out.println(menuHeader);
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
    public String[] getNextMenuDataHandlerParams()
    {
        String[] menuParams = new String[prompts.length];
        for (int i = 0; i < menuParams.length; i++) {
            menuParams[i] = prompts[i].getPromptAnswer();
        }
        return menuParams;
    }
}
