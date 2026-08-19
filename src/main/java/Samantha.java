import java.util.Scanner;

public class Samantha {
    private static final String LINE = "____________________________________________________________";

    private static void printResponse(String content) {
        System.out.println(LINE + "\n" + content + "\n" + LINE);
    }

    public static void main(String[] args) {
        String banner = " ____    _    __  __    _    _   _ _____ _   _    _    \n"
                + "/ ___|  / \\  |  \\/  |  / \\  | \\ | |_   _| | | |  / \\   \n"
                + "\\___ \\ / _ \\ | |\\/| | / _ \\ |  \\| | | | | |_| | / _ \\  \n"
                + " ___) / ___ \\| |  | |/ ___ \\| |\\  | | | |  _  |/ ___ \\ \n"
                + "|____/_/   \\_\\_|  |_/_/   \\_\\_| \\_| |_| |_| |_/_/   \\_\\\n";

        printResponse(banner
                + "Hello! I'm Samantha.\n"
                + "What can I do for you?");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.equals("bye")) {
            printResponse(input);
            input = scanner.nextLine();
        }

        printResponse("Bye. Let's talk next time!");
    }
}
