package samantha.command;

import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that requests undoing the most recent undoable command.
 */
public class UndoCommand extends Command {

    /**
     * Performs no task operation because the application owns command history.
     *
     * @param tasks current task list, which is not needed here
     * @param storage task persistence handler, which is not needed here
     * @return an empty response because the application handles the undo
     */
    @Override
    public String execute(TaskList tasks, Storage storage) {
        return "";
    }

    /**
     * Indicates that this command requests an undo operation.
     *
     * @return {@code true}
     */
    @Override
    public boolean isUndo() {
        return true;
    }
}
