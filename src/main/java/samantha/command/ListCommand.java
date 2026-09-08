package samantha.command;

import java.time.LocalDate;
import java.util.List;

import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;

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
     * @param storage task persistence handler, which is not needed for listing
     * @return matching task list
     */
    @Override
    public String execute(TaskList tasks, Storage storage) {
        StringBuilder message = new StringBuilder("Here are the tasks in your list:\n");
        List<Task> taskSnapshot = tasks.asList();
        for (int index = 0; index < taskSnapshot.size(); index++) {
            Task task = taskSnapshot.get(index);
            if (date == null || task.isOnDate(date)) {
                message.append(String.format("%d. %s\n", index + 1, task));
            }
        }
        return message.toString().stripTrailing();
    }
}
