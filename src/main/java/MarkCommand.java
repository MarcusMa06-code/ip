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
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws SamanthaException if the task ID is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SamanthaException {
        Task task = getTask(tasks, taskId);
        task.markDone();
        saveTasks(tasks, storage);
        ui.showResponse("Nice! I've marked this task as done:\n  " + task);
    }
}
