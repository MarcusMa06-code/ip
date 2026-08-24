import java.time.LocalDate;
import java.util.Arrays;

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
     * Splits a raw user command into words.
     *
     * @param input raw command entered by the user
     * @return the command words
     */
    public static String[] splitCommand(String input) {
        return input.split(" ");
    }

    /**
     * Extracts the description following a command word.
     *
     * @param parts words from the user command
     * @return the description, which may be empty
     */
    public static String parseDescription(String[] parts) {
        return String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
    }

    /**
     * Extracts a deadline's description and its value after {@code /by}.
     *
     * @param parts words from a deadline command
     * @return a two-element array containing the description and deadline
     * @throws SamanthaException if the command does not contain {@code /by}
     */
    public static String[] parseDeadlineDetails(String[] parts) throws SamanthaException {
        String[] segments = parseDescription(parts).split("\\s*/by\\s*", 2);
        if (segments.length < 2) {
            throw new SamanthaException("You forgot to include /by for this deadline.");
        }
        return new String[] {segments[0].trim(), segments[1].trim()};
    }

    /**
     * Extracts an event's description, start time, and end time.
     *
     * @param parts words from an event command
     * @return a three-element array containing the description, start time, and end time
     * @throws SamanthaException if the command does not contain both {@code /from} and {@code /to}
     */
    public static String[] parseEventDetails(String[] parts) throws SamanthaException {
        String[] segments = parseDescription(parts).split("/from|/to");
        if (segments.length < 3) {
            throw new SamanthaException("You forgot to include /from and /to for this event.");
        }
        return new String[] {segments[0].trim(), segments[1].trim(), segments[2].trim()};
    }

    /**
     * Parses the optional date argument supplied to {@code list}.
     *
     * @param parts words from a list command
     * @return the requested date, or {@code null} when no date was given
     * @throws SamanthaException if too many arguments were supplied or the date is invalid
     */
    public static LocalDate parseListDate(String[] parts) throws SamanthaException {
        if (parts.length > 2) {
            throw new SamanthaException("You entered too many parameters for this operation");
        }
        return parts.length == 2 ? DateTimeValue.parseDate(parts[1]) : null;
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
