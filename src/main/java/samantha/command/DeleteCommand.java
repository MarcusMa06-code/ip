package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that removes a task.
 */
public class DeleteCommand extends Command {
    private final int taskId;
    private Task deletedTask;
    private int deletedTaskIndex;

    /**
     * Creates a command for the given one-based task ID.
     *
     * @param taskId task to remove
     */
    public DeleteCommand(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Indicates that deleting this task can be undone.
     *
     * @return {@code true}
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Removes and saves the selected task.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the removed task
     * @throws InputException if the task ID does not exist
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public String execute(TaskList tasks, Storage storage)
            throws InputException, TaskFileWriteException {
        Task task = getTask(tasks, taskId);
        deletedTask = tasks.removeTask(taskId);
        deletedTaskIndex = taskId - 1;
        saveTasks(tasks, storage);
        return "Noted. I've removed this task:\n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", tasks.size());
    }

    /**
     * Restores the task deleted by this command.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the undone command
     * @throws InputException if the deleted task has not been recorded
     * @throws TaskFileWriteException if the task list cannot be saved
     */
    @Override
    public String undo(TaskList tasks, Storage storage)
            throws InputException, TaskFileWriteException {
        if (deletedTask == null) {
            throw new InputException("This command cannot be undone.");
        }
        tasks.addTaskAt(deletedTask, deletedTaskIndex);
        saveTasks(tasks, storage);
        return "I've undone the last command.";
    }
}
