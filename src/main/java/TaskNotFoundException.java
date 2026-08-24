/**
 * Indicates that a requested one-based task ID is outside the task list.
 */
public class TaskNotFoundException extends SamanthaException {
    /**
     * Creates an exception for a task ID that does not exist.
     */
    public TaskNotFoundException() {
        super("You entered a task number that does not exist.");
    }
}
