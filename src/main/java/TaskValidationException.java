/**
 * Base class for failures caused by invalid task data.
 */
public abstract class TaskValidationException extends SamanthaException {
    /**
     * Creates a task-validation exception with the supplied user-facing message.
     *
     * @param message explanation of the invalid task data
     */
    protected TaskValidationException(String message) {
        super(message);
    }
}
