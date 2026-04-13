public class Prompt {
    private PromptType promptType;
    private String promptText;
    private String promptAnswer;

    public Prompt(PromptType promptType, String promptText)
    {
        this.promptType = promptType;
        this.promptText = promptText;
    }

    public PromptType getPromptType()
    { return promptType; }

    public String getPromptText()
    { return promptText; }

    public void setPromptAnswer(String promptAnswer)
    { this.promptAnswer = promptAnswer; }

    public String getPromptAnswer()
    { return promptAnswer; }
}
