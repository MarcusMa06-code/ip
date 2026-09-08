package samantha.parser;

import java.time.LocalDate;
import java.util.Arrays;

import samantha.command.AddDeadlineCommand;
import samantha.command.AddEventCommand;
import samantha.command.AddTodoCommand;
import samantha.command.Command;
import samantha.command.DeleteCommand;
import samantha.command.ExitCommand;
import samantha.command.FindCommand;
import samantha.command.HelpCommand;
import samantha.command.ListCommand;
import samantha.command.MarkCommand;
import samantha.command.UnmarkCommand;
import samantha.exception.InputException;
import samantha.model.DateTimeValue;

/**
 * Interprets the command word and arguments entered by the user.
 */
public class Parser {
    private static final int COMMAND_WORD_COUNT = 1;
    private static final int MAXIMUM_ARGUMENT_PARTS = 2;
    private static final int COMMAND_ARGUMENT_INDEX = 1;

    /**
     * Creates a parser for Samantha commands.
     */
    public Parser() {
    }

    /**
     * Represents the commands supported by Samantha.
     */
    private enum CommandType {
        BYE, HELP, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, FIND
    }

    /**
     * Converts a complete user command into an executable command object.
     *
     * @param fullCommand complete command entered by the user
     * @return an executable command
     * @throws InputException if the command or its arguments are invalid
     */
    public static Command parse(String fullCommand) throws InputException {
        assert fullCommand != null : "A complete command must not be null";
        String[] parts = splitCommand(fullCommand);
        assert parts.length > 0 : "A split command must contain a command word";
        return switch (parseCommandType(parts[0])) {
            case BYE -> new ExitCommand();
            case HELP -> new HelpCommand();
            case LIST -> new ListCommand(parseListDate(parts));
            case TODO -> new AddTodoCommand(parseDescription(parts));
            case DEADLINE -> {
                String[] details = parseDeadlineDetails(parts);
                yield new AddDeadlineCommand(details[0], details[1]);
            }
            case EVENT -> {
                String[] details = parseEventDetails(parts);
                yield new AddEventCommand(details[0], details[1], details[2]);
            }
            case MARK -> new MarkCommand(parseTaskId(parts));
            case UNMARK -> new UnmarkCommand(parseTaskId(parts));
            case DELETE -> new DeleteCommand(parseTaskId(parts));
            case FIND -> new FindCommand(parseFindKeyword(parts));
        };
    }

    /**
     * Converts a command word into its supported command type.
     *
     * @param word command word to interpret
     * @return the matching command type
     * @throws InputException if the word does not name a supported command
     */
    private static CommandType parseCommandType(String word) throws InputException {
        try {
            return CommandType.valueOf(word.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InputException("It seems that you entered a wrong command.");
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
    public static String parseDescription(String... parts) {
        return String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
    }

    /**
     * Extracts and validates the keyword following a {@code find} command.
     *
     * @param parts words from the user command
     * @return the keyword to search for
     * @throws InputException if no keyword was supplied
     */
    public static String parseFindKeyword(String... parts) throws InputException {
        String keyword = parseDescription(parts).trim();
        if (keyword.isEmpty()) {
            throw new InputException("You forgot to mention the keyword to search for.");
        }
        return keyword;
    }

    /**
     * Extracts a deadline's description and its value after {@code /by}.
     *
     * @param parts words from a deadline command
     * @return a two-element array containing the description and deadline
     * @throws InputException if the command does not contain {@code /by}
     */
    public static String[] parseDeadlineDetails(String... parts) throws InputException {
        String[] segments = parseDescription(parts).split("\\s*/by\\s*", 2);
        if (segments.length < 2) {
            throw new InputException("You forgot to include /by for this deadline.");
        }
        assert segments.length == 2 : "A deadline must have exactly two segments";
        return new String[] {segments[0].trim(), segments[1].trim()};
    }

    /**
     * Extracts an event's description, start time, and end time.
     *
     * @param parts words from an event command
     * @return a three-element array containing the description, start time, and end time
     * @throws InputException if the command does not contain both {@code /from} and {@code /to}
     */
    public static String[] parseEventDetails(String... parts) throws InputException {
        String[] segments = parseDescription(parts).split("/from|/to");
        if (segments.length < 3) {
            throw new InputException("You forgot to include /from and /to for this event.");
        }
        assert segments.length == 3 : "An event must have exactly three segments";
        return new String[] {segments[0].trim(), segments[1].trim(), segments[2].trim()};
    }

    /**
     * Parses the optional date argument supplied to {@code list}.
     *
     * @param parts words from a list command
     * @return the requested date, or {@code null} when no date was given
     * @throws InputException if too many arguments were supplied or the date is invalid
     */
    public static LocalDate parseListDate(String... parts) throws InputException {
        if (parts.length > MAXIMUM_ARGUMENT_PARTS) {
            throw new InputException("You entered too many parameters for this operation");
        }
        if (parts.length == COMMAND_WORD_COUNT) {
            return null;
        }
        return DateTimeValue.parseDate(parts[COMMAND_ARGUMENT_INDEX]);
    }

    /**
     * Parses the one-based task ID supplied to a task-ID command.
     *
     * @param parts words from the user command
     * @return the parsed task ID
     * @throws InputException if the command has a missing, extra, or non-numeric ID
     */
    public static int parseTaskId(String... parts) throws InputException {
        if (parts.length > MAXIMUM_ARGUMENT_PARTS) {
            throw new InputException("You entered too many parameters for this operation");
        }
        if (parts.length == COMMAND_WORD_COUNT) {
            throw new InputException("You forgot to mention the id of the task");
        }
        try {
            return Integer.parseInt(parts[COMMAND_ARGUMENT_INDEX]);
        } catch (NumberFormatException e) {
            throw new InputException("You need to enter a number for the task id.");
        }
    }
}
