import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Samantha {
    private static final String LINE = "____________________________________________________________";
    private final List<Task> tasks = new ArrayList<>();

    private static void printResponse(String content) {
        System.out.println(LINE + "\n" + content + "\n" + LINE);
    }

    private void addTask(String taskName) {
        tasks.add(new Task(taskName));
        printResponse("added: " + taskName);
    }

    private void printTaskList() {
        String message = "";
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
                default -> samantha.addTask(input);
            }

            input = scanner.nextLine();
        }

        printResponse("Bye. Let's talk next time!");
    }

    private static class Task {
        private boolean isDone;
        private final String taskName; //cannot change task name once it's set

        public Task(String name) {
            this.taskName = name;
            this.isDone = false;
        }

        public void markDone() {
            this.isDone = true;
        }

        public void markNotDone() {
            this.isDone = false;
        }

        public String getTaskName() {
            return this.taskName;
        }

        public boolean getStatus() {
            return isDone;
        }

        @Override
        public String toString() {
            String flag = isDone ? "[X] " : "[ ] ";
            return flag + this.taskName;
        }
    }
}
