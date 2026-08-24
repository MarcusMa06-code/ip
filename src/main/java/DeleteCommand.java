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
     * @throws SamanthaException if the task ID is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SamanthaException {
        Task task = getTask(tasks, taskId);
        tasks.remove(taskId - 1);
        saveTasks(tasks, storage);
        ui.showResponse("Noted. I've removed this task:\n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", tasks.size()));
    }
}
