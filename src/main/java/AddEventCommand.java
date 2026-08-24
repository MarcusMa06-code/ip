/**
 * Represents the command that adds an event task.
 */
public class AddEventCommand extends Command {
    private final String taskName;
    private final String from;
    private final String to;

    /**
     * Creates a command for the given event details.
     *
     * @param taskName event description
     * @param from event start time
     * @param to event end time
     */
    public AddEventCommand(String taskName, String from, String to) {
        this.taskName = taskName;
        this.from = from;
        this.to = to;
    }

    /**
     * Adds and saves the event task.
     *
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws SamanthaException if the details are invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SamanthaException {
        Task task = new Event(taskName, from, to);
        tasks.add(task);
        saveTasks(tasks, storage);
        showTaskAdded(task, tasks.size(), ui);
    }
}
