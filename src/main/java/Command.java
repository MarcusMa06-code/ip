import java.io.IOException;

/**
 * Represents an executable user command.
 */
public abstract class Command {

    /**
     * Performs this command using Samantha's application components.
     *
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws SamanthaException if this command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws SamanthaException;

    /**
     * Returns whether executing this command should end the application.
     *
     * @return {@code true} only for an exit command
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the current task list and converts storage failures to application errors.
     *
     * @param tasks task list to save
     * @param storage task persistence handler
     * @throws SamanthaException if the task list cannot be saved
     */
    protected void saveTasks(TaskList tasks, Storage storage) throws SamanthaException {
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            throw new SamanthaException("I couldn't save your tasks to disk.");
        }
    }

    /**
     * Displays the confirmation after a new task is added.
     *
     * @param task newly added task
     * @param taskCount number of tasks after the addition
     * @param ui console interaction handler
     */
    protected void showTaskAdded(Task task, int taskCount, Ui ui) {
        ui.showResponse("Got it. I've added this task: \n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", taskCount));
    }
}
