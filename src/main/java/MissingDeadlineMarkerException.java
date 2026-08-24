/**
 * Indicates that a deadline command does not contain its {@code /by} marker.
 */
public class MissingDeadlineMarkerException extends SamanthaException {
    /**
     * Creates an exception for a deadline without a {@code /by} marker.
     */
    public MissingDeadlineMarkerException() {
        super("You forgot to include /by for this deadline.");
    }
}
