/**
 * Indicates that an event is missing its end time.
 */
public class MissingEventEndException extends TaskValidationException {
    /**
     * Creates an exception for an event without an end time.
     */
    public MissingEventEndException() {
        super("You need to specify an end time after /to.");
    }
}
