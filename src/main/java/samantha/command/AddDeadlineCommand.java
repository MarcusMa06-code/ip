package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.exception.TaskValidationException;
import samantha.model.Deadline;
import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;
import samantha.ui.Ui;

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
     * @throws TaskValidationException if the description is invalid
     * @throws InputException if the deadline value is invalid
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws TaskValidationException, InputException, TaskFileWriteException {
        Task task = new Deadline(taskName, deadline);
        tasks.add(task);
        saveTasks(tasks, storage);
        showTaskAdded(task, tasks.size(), ui);
    }
}
