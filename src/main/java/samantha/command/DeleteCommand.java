package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;
import samantha.ui.Ui;

/**
 * Represents the command that removes a task.
 */
public class DeleteCommand extends Command {
    private final int taskId;

    /**
     * Creates a command for the given one-based task ID.
     *
     * @param taskId task to remove
     */
    public DeleteCommand(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Removes and saves the selected task.
     *
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws InputException if the task ID does not exist
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws InputException, TaskFileWriteException {
        Task task = getTask(tasks, taskId);
        tasks.remove(taskId - 1);
        saveTasks(tasks, storage);
        ui.showResponse("Noted. I've removed this task:\n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", tasks.size()));
    }
}
