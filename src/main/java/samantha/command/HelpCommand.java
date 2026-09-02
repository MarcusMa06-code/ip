package samantha.command;

import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that displays Samantha's supported commands.
 */
public class HelpCommand extends Command {

    /**
     * Returns a concise guide to Samantha's command syntax.
     *
     * @param tasks current task list, which is not needed to show help
     * @param storage task persistence handler, which is not needed to show help
     * @return command usage guide
     */
    @Override
    public String execute(TaskList tasks, Storage storage) {
        return "Here's how to use Samantha:\n"
                + "  help\n"
                + "    Show this guide.\n"
                + "  todo DESCRIPTION\n"
                + "    Add a task.\n"
                + "  deadline DESCRIPTION /by DATE [TIME]\n"
                + "    Add a task due on DATE, optionally at TIME.\n"
                + "  event DESCRIPTION /from DATE TIME /to DATE TIME\n"
                + "    Add an event.\n"
                + "  list [DATE]\n"
                + "    List every task, or only tasks on DATE.\n"
                + "  find KEYWORD\n"
                + "    Find tasks whose descriptions contain KEYWORD.\n"
                + "  mark N | unmark N | delete N\n"
                + "    Update or remove task number N.\n"
                + "  bye\n"
                + "    Close Samantha.\n"
                + "\n"
                + "Use dates as d/M/yyyy or d-M-yyyy. Use times as HHmm.";
    }
}
