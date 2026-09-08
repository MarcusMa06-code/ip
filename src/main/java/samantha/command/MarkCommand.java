package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that marks a task as complete.
 */
public class MarkCommand extends Command {
    private final int taskId;
    private boolean wasDone;

    /**
     * Creates a command for the given one-based task ID.
     *
     * @param taskId task to mark as complete
     */
    public MarkCommand(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Indicates that marking this task can be undone.
     *
     * @return {@code true}
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Marks and saves the selected task.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the marked task
     * @throws InputException if the task ID does not exist
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public String execute(TaskList tasks, Storage storage)
            throws InputException, TaskFileWriteException {
        Task task = getTask(tasks, taskId);
        wasDone = task.isDone();
        task.markDone();
        saveTasks(tasks, storage);
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Restores the task's completion state from before this command.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the undone command
     * @throws InputException if the task no longer exists
     * @throws TaskFileWriteException if the task list cannot be saved
     */
    @Override
    public String undo(TaskList tasks, Storage storage)
            throws InputException, TaskFileWriteException {
        Task task = getTask(tasks, taskId);
        if (wasDone) {
            task.markDone();
        } else {
            task.markNotDone();
        }
        saveTasks(tasks, storage);
        return "I've undone the last command.";
    }
}
