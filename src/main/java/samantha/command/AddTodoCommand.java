package samantha.command;

import samantha.exception.TaskFileWriteException;
import samantha.exception.TaskValidationException;
import samantha.model.TaskList;
import samantha.model.Todo;
import samantha.storage.Storage;

/**
 * Represents the command that adds a todo task.
 */
public class AddTodoCommand extends Command {
    private final String taskName;

    /**
     * Creates a command for the given todo description.
     *
     * @param taskName todo description
     */
    public AddTodoCommand(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Indicates that adding this task can be undone.
     *
     * @return {@code true}
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Adds and saves the todo task.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the newly added task
     * @throws TaskValidationException if the description is empty
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public String execute(TaskList tasks, Storage storage)
            throws TaskValidationException, TaskFileWriteException {
        return addAndSaveTask(new Todo(taskName), tasks, storage);
    }
}
