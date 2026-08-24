/**
 * Indicates that a task ID could not be parsed as a number.
 */
public class InvalidTaskIdException extends InputException {
    /**
     * Creates an exception for a non-numeric task ID.
     */
    public InvalidTaskIdException() {
        super("You need to enter a number for the task id.");
    }
}
