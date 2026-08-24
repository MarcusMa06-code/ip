/**
 * Base class for checked exceptions raised by Samantha.
 */
public class SamanthaException extends Exception {
    /**
     * Creates an application exception with a user-facing message.
     *
     * @param message explanation of the failure
     */
    public SamanthaException(String message) {
        super(message);
    }

    /**
     * Creates an application exception with a message and underlying cause.
     *
     * @param message explanation of the failure
     * @param cause lower-level failure that caused this exception
     */
    public SamanthaException(String message, Throwable cause) {
        super(message, cause);
    }
}
