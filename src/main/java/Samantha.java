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
                        new AddTodoCommand(Parser.parseDescription(parts)).execute(tasks, ui, storage);
                    }
                    case DEADLINE -> {
                        String[] details = Parser.parseDeadlineDetails(parts);
                        new AddDeadlineCommand(details[0], details[1]).execute(tasks, ui, storage);
                    }
                    case EVENT -> {
                        String[] details = Parser.parseEventDetails(parts);
                        new AddEventCommand(details[0], details[1], details[2]).execute(tasks, ui, storage);
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
