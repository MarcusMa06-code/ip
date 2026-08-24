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
                    case MARK -> new MarkCommand(Parser.parseTaskId(parts)).execute(tasks, ui, storage);
                    case UNMARK -> new UnmarkCommand(Parser.parseTaskId(parts)).execute(tasks, ui, storage);
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
                    case DELETE -> new DeleteCommand(Parser.parseTaskId(parts)).execute(tasks, ui, storage);
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
