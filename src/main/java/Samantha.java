import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Samantha {
    private static final String LINE = "____________________________________________________________";
    private final List<Task> tasks = new ArrayList<>();

    private static void printResponse(String content) {
        System.out.println(LINE + "\n" + content + "\n" + LINE);
    }

    private void addDeadline(String taskName, String deadline) {
        Task newTask = new Deadline(taskName, deadline);
        tasks.add(newTask);
        printAddTaskMsg(newTask);
    }

    private void addEvent(String taskName, String from, String to) {
        Task newTask = new Event(taskName, from, to);
        tasks.add(newTask);
        printAddTaskMsg(newTask);
    }

    private void addToDo(String taskName) {
        Task newTask = new Todo(taskName);
        tasks.add(newTask);
        printAddTaskMsg(newTask);
    }

    private void printAddTaskMsg(Task task) {
        printResponse("Got it. I've added this task: \n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", tasks.size())
        );
    }

    private void printTaskList() {
        String message = "Here are the tasks in your list:\n";
        for (int i = 1; i <= tasks.size(); i++) {
            message += String.format("%d. %s\n", i, tasks.get(i - 1));
        }
        printResponse(message.stripTrailing());
    }

    private void markDone(int id) {
        Task task = tasks.get(id - 1);
        task.markDone();
        printResponse("Nice! I've marked this task as done:\n  " + task);
    }

    private void markNotDone(int id) {
        Task task = tasks.get(id - 1);
        task.markNotDone();
        printResponse("OK, I've marked this task as not done yet:\n  " + task);
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
            String[] parts = input.split(" ");
            switch (parts[0]) {
                case "mark" -> samantha.markDone(Integer.parseInt(parts[1]));
                case "unmark" -> samantha.markNotDone(Integer.parseInt(parts[1]));
                case "list" -> samantha.printTaskList();
                case "todo" -> {
                    String taskName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    samantha.addToDo(taskName);
                }
                case "deadline" -> {
                    String content = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    String[] segments = content.split("/by");
                    samantha.addDeadline(segments[0].trim(), segments[1].trim());
                }
                case "event" -> {
                    String content = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    String[] segments = content.split("/from|/to");
                    samantha.addEvent(segments[0].trim(), segments[1].trim(), segments[2].trim());
                }
            }

            input = scanner.nextLine();
        }

        printResponse("Bye. Let's talk next time!");
    }
}
