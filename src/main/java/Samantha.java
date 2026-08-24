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

        boolean isExit = false;
        while (!isExit) {
            try {
                Command command = Parser.parse(ui.readCommand());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (SamanthaException e) {
                ui.showError(e.getMessage());
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
