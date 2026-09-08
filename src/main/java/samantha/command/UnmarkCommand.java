package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.model.Task;

/**
 * Represents the command that marks a task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final int taskId;

    /**
     * Creates a command for the given one-based task ID.
     *
     * @param taskId task to mark as incomplete
     */
    public UnmarkCommand(int taskId) {
        this.taskId = taskId;
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
        task.markNotDone();
        saveTasks(context.getTasks(), context.getTaskStorage());
        return "OK, I've marked this task as not done yet:\n  " + task;
    }
}
