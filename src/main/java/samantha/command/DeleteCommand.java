package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.model.Task;

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
     * @param context current application state and persistence handlers
     * @return confirmation for the removed task
     * @throws InputException if the task ID does not exist
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public String execute(CommandContext context)
            throws InputException, TaskFileWriteException {
        Task task = context.getTasks().removeTask(taskId);
        saveTasks(context.getTasks(), context.getTaskStorage());
        return "Noted. I've removed this task:\n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", context.getTasks().size());
    }
}
