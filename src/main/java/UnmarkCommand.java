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
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws SamanthaException if the task ID is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SamanthaException {
        Task task = getTask(tasks, taskId);
        task.markNotDone();
        saveTasks(tasks, storage);
        ui.showResponse("OK, I've marked this task as not done yet:\n  " + task);
    }
}
