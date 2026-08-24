import java.io.IOException;

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

    /**
     * Runs the application until the user enters the exit command.
     */
    public void run() {
        String banner = " ____    _    __  __    _    _   _ _____ _   _    _    \n"
                + "/ ___|  / \\  |  \\/  |  / \\  | \\ | |_   _| | | |  / \\   \n"
                + "\\___ \\ / _ \\ | |\\/| | / _ \\ |  \\| | | | | |_| | / _ \\  \n"
                + " ___) / ___ \\| |  | |/ ___ \\| |\\  | | | |  _  |/ ___ \\ \n"
                + "|____/_/   \\_\\_|  |_/_/   \\_\\_| \\_| |_| |_| |_/_/   \\_\\\n";

        try {
            loadTasks();
        } catch (SamanthaException e) {
            ui.showCorruptedFileWarning();
        } catch (IOException e) {
            ui.showFileReadErrorWarning();
        }

        ui.showWelcome(banner);

        boolean isRunning = true;
        while (isRunning) {
            try {
                String[] parts = Parser.splitCommand(ui.readCommand());
                switch (Parser.parseCommand(parts[0])) {
                    case BYE -> {
                        Command command = new ExitCommand();
                        command.execute(tasks, ui, storage);
                        isRunning = !command.isExit();
                    }
                    case LIST -> new ListCommand(Parser.parseListDate(parts)).execute(tasks, ui, storage);
                    case MARK -> markDone(Parser.parseTaskId(parts));
                    case UNMARK -> markNotDone(Parser.parseTaskId(parts));
                    case TODO -> {
                        addToDo(Parser.parseDescription(parts));
                    }
                    case DEADLINE -> {
                        String[] details = Parser.parseDeadlineDetails(parts);
                        addDeadline(details[0], details[1]);
                    }
                    case EVENT -> {
                        String[] details = Parser.parseEventDetails(parts);
                        addEvent(details[0], details[1], details[2]);
                    }
                    case DELETE -> delete(Parser.parseTaskId(parts));
                }
            } catch (SamanthaException e) {
                ui.showResponse(e.getMessage());
            }
        }

        ui.showGoodbye();
    }

    /**
     * Starts Samantha using the default storage location.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Samantha().run();
    }
}
