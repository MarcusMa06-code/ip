/**
 * Indicates that an event is missing its start time.
 */
public class MissingEventStartException extends TaskValidationException {
    /**
     * Creates an exception for an event without a start time.
     */
    public MissingEventStartException() {
        super("You need to specify a start time after /from.");
    }
}
