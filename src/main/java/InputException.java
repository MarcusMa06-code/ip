/**
 * Base class for failures caused by invalid user-supplied command input.
 */
public abstract class InputException extends SamanthaException {
    /**
     * Creates an input exception with a user-facing message.
     *
     * @param message explanation of the invalid input
     */
    protected InputException(String message) {
        super(message);
    }
}
