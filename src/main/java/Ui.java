import java.util.Scanner;

/**
 * Handles Samantha's console-based interactions with the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads one command line from the user.
     *
     * @return the command line entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the given welcome banner and Samantha's greeting.
     *
     * @param banner the banner to display before the greeting
     */
    public void showWelcome(String banner) {
        showResponse(banner
                + "Hello! I'm Samantha.\n"
                + "What can I do for you?");
    }

    /**
     * Displays one formatted response in the console.
     *
     * @param content the response content to display
     */
    public void showResponse(String content) {
        System.out.println(LINE + "\n" + content + "\n" + LINE);
    }

    /**
     * Displays an error message in the standard response format.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        showResponse(message);
    }

    /**
     * Displays a warning that the saved task file is malformed.
     */
    public void showCorruptedFileWarning() {
        showResponse("Warning: The saved task file is corrupted. Starting with an empty task list.");
    }

    /**
     * Displays a warning that the saved task file could not be read.
     */
    public void showFileReadErrorWarning() {
        showResponse("Warning: I couldn't read the saved tasks. Starting with an empty task list.");
    }

    /**
     * Displays Samantha's farewell message.
     */
    public void showGoodbye() {
        showResponse("Bye. Let's talk next time!");
    }
}
