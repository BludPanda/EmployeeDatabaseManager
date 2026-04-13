import java.util.Scanner;

public class OptionMenu extends Menu {

    private Scanner input = new Scanner(System.in);

    private Option[] options;

    private int selectedOptionIndex = -1;

    public OptionMenu(String menuTitle, String menuHeader, Option[] options)
    {
        super(menuTitle, menuHeader);
        this.options = options;
    }

    public int getNumOptions()
    { return options.length; }
    
    @Override
    public void activateMenu()
    {
        // Clear terminal when switching menus
        System.out.print("\f");
        System.out.println("\n" + "\u2500".repeat(50) + "\n");

        // Name & Instructions
        System.out.println(menuTitle + "\n");
        System.out.println(menuHeader);

        // List Options
        for (int i = 0; i < options.length; i++)
        {
            System.out.println("[" + i + "] : " + options[i].optionTitle);
        }
    }

    @Override
    public void displayPrompt(int promptIndex)
    { System.out.print("\nEnter Option # -> "); }

    @Override
    public PromptType getPromptType(int promptIndex)
    { return PromptType.Index; }

    @Override
    public Boolean validPromptIndex(int index)
    { return index < options.length; }

    @Override
    public void enterPrompt(String prompt, int promptIndex)
    { selectedOptionIndex = Integer.parseInt(prompt); }

    @Override
    public String getNextMenuKey()
    { return options[selectedOptionIndex].destinationMenuKey; }
}
