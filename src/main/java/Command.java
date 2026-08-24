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
}
