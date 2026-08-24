/**
 * Indicates that a date or date-time value has an unsupported format.
 */
public class InvalidDateTimeException extends InputException {
    /**
     * Creates an exception for an incorrectly formatted date or date-time.
     */
    public InvalidDateTimeException() {
        super("The date and time must use d/M/yyyy or d-M-yyyy, optionally followed by HHmm.");
    }
}
