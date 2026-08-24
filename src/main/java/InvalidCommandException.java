/**
 * Indicates that a command word is not supported by Samantha.
 */
public class InvalidCommandException extends InputException {
    /**
     * Creates an exception for an unsupported command word.
     */
    public InvalidCommandException() {
        super("It seems that you entered a wrong command.");
    }
}
