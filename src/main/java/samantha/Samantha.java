package samantha;

import java.util.ArrayList;
import java.util.List;

import samantha.command.Command;
import samantha.exception.CorruptedTaskFileException;
import samantha.exception.SamanthaException;
import samantha.exception.TaskFileReadException;
import samantha.model.TaskList;
import samantha.parser.Parser;
import samantha.storage.Storage;
import samantha.ui.Ui;

/**
 * Coordinates Samantha's command loop and its task, storage, and UI components.
 */
public class Samantha {
    private static final String GUI_GREETING = "Hello. I’m here. What would you like to do today?";
    private static final String CORRUPTED_FILE_WARNING =
            "Warning: The saved task file is corrupted. Starting with an empty task list.";
    private static final String FILE_READ_WARNING =
            "Warning: I couldn't read the saved tasks. Starting with an empty task list.";
    private static final String GOODBYE = "Bye. Let's talk next time!";

    private final TaskList tasks = new TaskList();
    private final Storage storage;
    private boolean isInitialized;
    private boolean isExitRequested;
    private String startupWarning = "";

    /**
     * Creates an empty Samantha instance using the default storage location.
     */
    public Samantha() {
        this(new Storage());
    }

    /**
     * Creates an application instance with the supplied collaborators.
     *
     * @param storage task persistence handler
     */
    Samantha(Storage storage) {
        assert storage != null : "Samantha must have a storage handler";
        this.storage = storage;
    }

    /**
     * Loads the persisted tasks into this application's task list.
     *
     * @throws TaskFileReadException if the task file cannot be read
     * @throws CorruptedTaskFileException if a persisted task record is malformed
     */
    private void loadTasks() throws TaskFileReadException, CorruptedTaskFileException {
        tasks.addAll(storage.load());
    }

    /**
     * Returns the assistant messages that should open a new conversation.
     *
     * @return greeting and any storage warning
     */
    public List<String> getInitialResponses() {
        initialize();
        List<String> messages = new ArrayList<>();
        messages.add(GUI_GREETING);
        if (!startupWarning.isEmpty()) {
            messages.add(startupWarning);
        }
        return List.copyOf(messages);
    }

    /**
     * Processes one user command and returns Samantha's response.
     *
     * @param input command entered by the user
     * @return command response, error message, or farewell
     */
    public String getResponse(String input) {
        initialize();
        assert isInitialized : "Samantha must be initialized before handling commands";
        try {
            Command command = Parser.parse(input);
            assert command != null : "Parsing a valid command must produce a command";
            String response = command.execute(tasks, storage);
            isExitRequested = command.isExit();
            return isExitRequested ? GOODBYE : response;
        } catch (SamanthaException e) {
            return e.getMessage();
        }
    }

    /**
     * Returns whether the most recently handled command requested application exit.
     *
     * @return {@code true} after a {@code bye} command
     */
    public boolean isExitRequested() {
        return isExitRequested;
    }

    /**
     * Loads saved tasks once and records any user-facing storage warning.
     */
    private void initialize() {
        if (isInitialized) {
            return;
        }

        try {
            loadTasks();
        } catch (CorruptedTaskFileException e) {
            startupWarning = CORRUPTED_FILE_WARNING;
        } catch (TaskFileReadException e) {
            startupWarning = FILE_READ_WARNING;
        }
        isInitialized = true;
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

        Ui ui = new Ui();
        initialize();
        if (!startupWarning.isEmpty()) {
            ui.showResponse(startupWarning);
        }

        ui.showWelcome(banner);

        while (!isExitRequested) {
            String response = getResponse(ui.readCommand());
            if (!isExitRequested) {
                ui.showResponse(response);
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
