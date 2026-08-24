/**
 * Indicates that a command received more arguments than it accepts.
 */
public class TooManyArgumentsException extends InputException {
    /**
     * Creates an exception for an over-specified command.
     */
    public TooManyArgumentsException() {
        super("You entered too many parameters for this operation");
    }
}
