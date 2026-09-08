package samantha.ui;

import java.util.Scanner;

/**
 * Handles Samantha's console-based interactions with the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a console UI that reads from standard input and writes to standard output.
     */
    public Ui() {
    }

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
     * Displays Samantha's farewell message.
     */
    public void showGoodbye() {
        showResponse("Bye. Let's talk next time!");
    }
}
