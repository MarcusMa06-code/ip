package samantha.command;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        String matchingTasks = IntStream.range(0, tasks.size())
                .filter(index -> date == null || tasks.get(index).isOnDate(date))
                .mapToObj(index -> String.format("%d. %s", index + 1, tasks.get(index)))
                .collect(Collectors.joining("\n"));
        String message = "Here are the tasks in your list:\n" + matchingTasks;
        return message.stripTrailing();
    }
}
