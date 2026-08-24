/**
 * Represents the command that adds a deadline task.
 */
public class AddDeadlineCommand extends Command {
    private final String taskName;
    private final String deadline;

    /**
     * Creates a command for the given deadline details.
     *
     * @param taskName deadline description
     * @param deadline deadline value
     */
    public AddDeadlineCommand(String taskName, String deadline) {
        this.taskName = taskName;
        this.deadline = deadline;
    }

    /**
     * Adds and saves the deadline task.
     *
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws SamanthaException if the details are invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SamanthaException {
        Task task = new Deadline(taskName, deadline);
        tasks.add(task);
        saveTasks(tasks, storage);
        showTaskAdded(task, tasks.size(), ui);
    }
}
