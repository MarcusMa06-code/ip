package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.model.Task;

/**
 * Represents the command that marks a task as complete.
 */
public class MarkCommand extends Command {
    private final int taskId;

    /**
     * Creates a command for the given one-based task ID.
     *
     * @param taskId task to mark as complete
     */
    public MarkCommand(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Marks and saves the selected task.
     *
     * @param context current application state and persistence handlers
     * @return confirmation for the marked task
     * @throws InputException if the task ID does not exist
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public String execute(CommandContext context)
            throws InputException, TaskFileWriteException {
        Task task = getTask(context.getTasks(), taskId);
        task.markDone();
        saveTasks(context.getTasks(), context.getTaskStorage());
        return "Nice! I've marked this task as done:\n  " + task;
    }
}
