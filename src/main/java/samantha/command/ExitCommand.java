package samantha.command;

import samantha.model.TaskList;
import samantha.storage.Storage;
import samantha.ui.Ui;

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
     * @param ui console interaction handler
     * @param storage task persistence handler
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // The application loop ends after checking isExit().
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
