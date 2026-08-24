import java.time.LocalDate;

/**
 * Represents the command that displays tasks, optionally filtered by date.
 */
public class ListCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a list command with an optional date filter.
     *
     * @param date date to filter by, or {@code null} to show all tasks
     */
    public ListCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * Displays the matching tasks.
     *
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler, which is not needed for listing
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String message = "Here are the tasks in your list:\n";
        for (int i = 1; i <= tasks.size(); i++) {
            Task task = tasks.get(i - 1);
            if (date == null || task.isOnDate(date)) {
                message += String.format("%d. %s\n", i, task);
            }
        }
        ui.showResponse(message.stripTrailing());
    }
}
