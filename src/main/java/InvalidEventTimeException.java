/**
 * Indicates that an event time does not include an HHmm time component.
 */
public class InvalidEventTimeException extends TaskValidationException {
    /**
     * Creates an exception for an event date without a time.
     */
    public InvalidEventTimeException() {
        super("An event date and time must include a time in HHmm format.");
    }
}
