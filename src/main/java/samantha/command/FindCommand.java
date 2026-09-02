package samantha.command;

import java.util.Locale;

import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that finds tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a find command for the supplied keyword.
     *
     * @param keyword keyword to search for in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword.toLowerCase(Locale.ROOT);
    }

    /**
     * Displays tasks whose descriptions contain the search keyword.
     *
     * @param tasks current task list
     * @param storage task persistence handler, which is not needed for searching
     * @return matching task list
     */
    @Override
    public String execute(TaskList tasks, Storage storage) {
        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 1; i <= tasks.size(); i++) {
            Task task = tasks.get(i - 1);
            if (containsKeyword(task)) {
                message.append(String.format("%d. %s\n", i, task));
            }
        }
        return message.toString().stripTrailing();
    }

    /**
     * Checks whether a task description contains this command's keyword.
     *
     * @param task task whose description should be checked
     * @return {@code true} if the description contains the keyword, ignoring case
     */
    private boolean containsKeyword(Task task) {
        return task.getTaskName().toLowerCase(Locale.ROOT).contains(keyword);
    }
}
