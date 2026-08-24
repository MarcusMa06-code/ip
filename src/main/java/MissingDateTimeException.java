/**
 * Indicates that a date or date-time value was not supplied.
 */
public class MissingDateTimeException extends SamanthaException {
    /**
     * Creates an exception for a missing deadline value.
     */
    public MissingDateTimeException() {
        super("You did not mention deadline after /by");
    }
}
