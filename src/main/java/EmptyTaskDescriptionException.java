/**
 * Indicates that a task was created without a description.
 */
public class EmptyTaskDescriptionException extends TaskValidationException {
    /**
     * Creates an exception for an empty description of the given task type.
     *
     * @param taskType task type whose description is missing
     */
    public EmptyTaskDescriptionException(String taskType) {
        super("The description of a " + taskType + " cannot be empty.");
    }
}
