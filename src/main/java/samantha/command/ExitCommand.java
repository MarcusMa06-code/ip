package samantha.command;

import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that ends Samantha's command loop.
 */
public class ExitCommand extends Command {

    /**
     * Creates a command that ends the application loop.
     */
    public ExitCommand() {
    }

    /**
     * Performs no task operation because the application loop handles shutdown.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return an empty response because the application handles farewell output
     */
    @Override
    public String execute(TaskList tasks, Storage storage) {
        // The application loop ends after checking isExit().
        return "";
    }

    /**
     * Indicates that the application should exit.
     *
     * @return {@code true}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
