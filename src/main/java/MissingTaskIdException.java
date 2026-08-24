/**
 * Indicates that a task command did not include a task ID.
 */
public class MissingTaskIdException extends SamanthaException {
    /**
     * Creates an exception for a missing task ID.
     */
    public MissingTaskIdException() {
        super("You forgot to mention the id of the task");
    }
}
