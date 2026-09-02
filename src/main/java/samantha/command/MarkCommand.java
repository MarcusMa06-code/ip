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
        task.markDone();
        saveTasks(tasks, storage);
        return "Nice! I've marked this task as done:\n  " + task;
    }
}
