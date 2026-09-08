package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that marks a task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final int taskId;
    private boolean wasDone;

    /**
     * Creates a command for the given one-based task ID.
     *
     * @param taskId task to mark as incomplete
     */
    public UnmarkCommand(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Indicates that unmarking this task can be undone.
     *
     * @return {@code true}
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Marks the selected task as incomplete and saves it.
     *
     * @param context current application state and persistence handlers
     * @return confirmation for the unmarked task
     * @throws InputException if the task ID does not exist
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public String execute(CommandContext context)
            throws InputException, TaskFileWriteException {
        Task task = getTask(context.getTasks(), taskId);
        wasDone = task.isDone();
        task.markNotDone();
        saveTasks(context.getTasks(), context.getTaskStorage());
        return "OK, I've marked this task as not done yet:\n  " + task;
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
