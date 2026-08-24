/**
 * Interprets the command word and arguments entered by the user.
 */
public class Parser {

    /**
     * Represents the commands supported by Samantha.
     */
    public enum Command {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE
    }

    /**
     * Converts a command word into a supported command.
     *
     * @param word command word entered by the user
     * @return the corresponding command
     * @throws SamanthaException if the word is not a supported command
     */
    public static Command parseCommand(String word) throws SamanthaException {
        try {
            return Command.valueOf(word.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SamanthaException("It seems that you entered a wrong command.");
        }
    }

    /**
     * Parses the one-based task ID supplied to a task-ID command.
     *
     * @param parts words from the user command
     * @return the parsed task ID
     * @throws SamanthaException if the command has a missing, extra, or non-numeric ID
     */
    public static int parseTaskId(String[] parts) throws SamanthaException {
        if (parts.length > 2) {
            throw new SamanthaException("You entered too many parameters for this operation");
        } else if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new SamanthaException("You need to enter a number for the task id.");
            }
        } else {
            throw new SamanthaException("You forgot to mention the id of the task");
        }
    }
}
