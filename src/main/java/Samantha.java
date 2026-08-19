import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Samantha {
    private static final String LINE = "____________________________________________________________";
    private final List<String> tasks = new ArrayList<>();

    private static void printResponse(String content) {
        System.out.println(LINE + "\n" + content + "\n" + LINE);
    }

    private void addTask(String task) {
        tasks.add(task);
        printResponse("added: " + task);
    }

    private void printTaskList() {
        String message = "";
        for (int i = 1; i <= tasks.size(); i++) {
            message += String.format("%d. %s\n", i, tasks.get(i - 1));
        }
        printResponse(message.stripTrailing());
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

        Samantha samantha = new Samantha();
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            if (input.equals("list")) {
                samantha.printTaskList();
            } else {
                samantha.addTask(input);
            }
            input = scanner.nextLine();
        }

        printResponse("Bye. Let's talk next time!");
    }
}
