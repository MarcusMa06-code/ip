/**
 * Indicates that an event command does not contain both time markers.
 */
public class MissingEventMarkersException extends InputException {
    /**
     * Creates an exception for an event without {@code /from} and {@code /to}.
     */
    public MissingEventMarkersException() {
        super("You forgot to include /from and /to for this event.");
    }
}
