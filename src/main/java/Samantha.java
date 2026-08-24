import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Samantha {
    private final TaskList tasks = new TaskList();
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates an empty Samantha instance using the default storage location.
     */
    public Samantha() {
        this(new Storage(), new Ui());
    }

    private Samantha(Storage storage, Ui ui) {
        this.storage = storage;
        this.ui = ui;
    }

    private void loadTasks() throws IOException, SamanthaException {
        tasks.addAll(storage.load());
    }

    private void saveTasks() throws SamanthaException {
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            throw new SamanthaException("I couldn't save your tasks to disk.");
        }
    }

    private void addDeadline(String taskName, String deadline) throws SamanthaException{
        if (taskName.isBlank()) {
            throw new SamanthaException("The description of a deadline cannot be empty.");
        }
        if (deadline.isBlank()) {
            throw new SamanthaException("You did not mention deadline after /by");
        }
        Task newTask = new Deadline(taskName, deadline);
        tasks.add(newTask);
        saveTasks();
        printAddTaskMsg(newTask);
    }

    private void addEvent(String taskName, String from, String to) throws SamanthaException{
        if (taskName.isBlank()) {
            throw new SamanthaException("The description of an event cannot be empty.");
        }
        if (from.isBlank()) {
            throw new SamanthaException("You need to specify a start time after /from.");
        }
        if (to.isBlank()) {
            throw new SamanthaException("You need to specify an end time after /to.");
        }
        Task newTask = new Event(taskName, from, to);
        tasks.add(newTask);
        saveTasks();
        printAddTaskMsg(newTask);
    }

    private void addToDo(String taskName) throws SamanthaException{
        if (taskName.isBlank()) {
            throw new SamanthaException("The description of a todo cannot be empty.");
        }

        Task newTask = new Todo(taskName);
        tasks.add(newTask);
        saveTasks();
        printAddTaskMsg(newTask);
    }

    private void printAddTaskMsg(Task task) {
        ui.showResponse("Got it. I've added this task: \n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", tasks.size())
        );
    }

    private void printTaskList(String[] parts) throws SamanthaException {
        if (parts.length > 2) {
            throw new SamanthaException("You entered too many parameters for this operation");
        }

        LocalDate date = parts.length == 2 ? DateTimeValue.parseDate(parts[1]) : null;
        String message = "Here are the tasks in your list:\n";
        for (int i = 1; i <= tasks.size(); i++) {
            Task task = tasks.get(i - 1);
            if (date == null || task.isOnDate(date)) {
                message += String.format("%d. %s\n", i, task);
            }
        }
        ui.showResponse(message.stripTrailing());
    }

    private void markDone(int id) throws SamanthaException {
        if (id < 1 || id > tasks.size()) {
            throw new SamanthaException("You entered a task number that does not exist.");
        }
        Task task = tasks.get(id - 1);
        task.markDone();
        saveTasks();
        ui.showResponse("Nice! I've marked this task as done:\n  " + task);
    }

    private void markNotDone(int id) throws SamanthaException {
        if (id < 1 || id > tasks.size()) {
            throw new SamanthaException("You entered a task number that does not exist.");
        }
        Task task = tasks.get(id - 1);
        task.markNotDone();
        saveTasks();
        ui.showResponse("OK, I've marked this task as not done yet:\n  " + task);
    }

    private void delete(int id) throws SamanthaException {
        if (id < 1 || id > tasks.size()) {
            throw new SamanthaException("You entered a task number that does not exist.");
        }

        Task task = tasks.get(id - 1);
        tasks.remove(id - 1);
        saveTasks();
        ui.showResponse("Noted. I've removed this task:\n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", tasks.size())
        );
    }

    private int parseID(String[] parts) throws SamanthaException {
        if (parts.length > 2) {
            throw new SamanthaException("You entered too many parameters for this operation");
        } else if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new SamanthaException("You need to enter a number for the task id.");
            }
        } else {
            throw new SamanthaException("You forgot to mention the id of the task");
        }
    }

    public enum Command {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE;

        public static Command from(String word) throws SamanthaException {
            try {
                return Command.valueOf(word.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new SamanthaException("It seems that you entered a wrong command.");
            }
        }
    }

    public static void main(String[] args) {
        String banner = " ____    _    __  __    _    _   _ _____ _   _    _    \n"
                + "/ ___|  / \\  |  \\/  |  / \\  | \\ | |_   _| | | |  / \\   \n"
                + "\\___ \\ / _ \\ | |\\/| | / _ \\ |  \\| | | | | |_| | / _ \\  \n"
                + " ___) / ___ \\| |  | |/ ___ \\| |\\  | | | |  _  |/ ___ \\ \n"
                + "|____/_/   \\_\\_|  |_/_/   \\_\\_| \\_| |_| |_| |_/_/   \\_\\\n";

        Samantha samantha = new Samantha();
        try {
            samantha.loadTasks();
        } catch (SamanthaException e) {
            samantha.ui.showCorruptedFileWarning();
        } catch (IOException e) {
            samantha.ui.showFileReadErrorWarning();
        }

        samantha.ui.showResponse(banner
                + "Hello! I'm Samantha.\n"
                + "What can I do for you?");

        Scanner scanner = new Scanner(System.in);

        boolean isRunning = true;
        while (isRunning) {
            try {
                String[] parts = scanner.nextLine().split(" ");
                switch (Command.from(parts[0])) {
                    case BYE -> isRunning = false;
                    case LIST -> samantha.printTaskList(parts);
                    case MARK -> samantha.markDone(samantha.parseID(parts));
                    case UNMARK -> samantha.markNotDone(samantha.parseID(parts));
                    case TODO -> {
                        String taskName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                        samantha.addToDo(taskName);
                    }
                    case DEADLINE -> {
                        String content = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                        String[] segments = content.split("\\s*/by\\s*", 2);
                        if (segments.length < 2) {
                            throw new SamanthaException("You forgot to include /by for this deadline.");
                        }
                        samantha.addDeadline(segments[0].trim(), segments[1].trim());
                    }
                    case EVENT -> {
                        String content = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                        String[] segments = content.split("/from|/to");
                        if (segments.length < 3) {
                            throw new SamanthaException("You forgot to include /from and /to for this event.");
                        }
                        samantha.addEvent(segments[0].trim(), segments[1].trim(), segments[2].trim());
                    }
                    case DELETE -> samantha.delete(samantha.parseID(parts));
                }
            } catch (SamanthaException e) {
                samantha.ui.showResponse(e.getMessage());
            }
        }

        samantha.ui.showGoodbye();
    }
}
