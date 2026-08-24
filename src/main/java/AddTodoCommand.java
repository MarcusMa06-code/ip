/**
 * Represents the command that adds a todo task.
 */
public class AddTodoCommand extends Command {
    private final String taskName;

    /**
     * Creates a command for the given todo description.
     *
     * @param taskName todo description
     */
    public AddTodoCommand(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Adds and saves the todo task.
     *
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws SamanthaException if the description is empty or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SamanthaException {
        Task task = new Todo(taskName);
        tasks.add(task);
        saveTasks(tasks, storage);
        showTaskAdded(task, tasks.size(), ui);
    }
}
