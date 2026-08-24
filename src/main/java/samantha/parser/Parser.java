package samantha.parser;

import java.time.LocalDate;
import java.util.Arrays;

import samantha.command.AddDeadlineCommand;
import samantha.command.AddEventCommand;
import samantha.command.AddTodoCommand;
import samantha.command.Command;
import samantha.command.DeleteCommand;
import samantha.command.ExitCommand;
import samantha.command.ListCommand;
import samantha.command.MarkCommand;
import samantha.command.UnmarkCommand;
import samantha.exception.InputException;
import samantha.model.DateTimeValue;

/**
 * Interprets the command word and arguments entered by the user.
 */
public class Parser {

    /**
     * Represents the commands supported by Samantha.
     */
    private enum CommandType {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE
    }

    /**
     * Converts a complete user command into an executable command object.
     *
     * @param fullCommand complete command entered by the user
     * @return an executable command
     * @throws InputException if the command or its arguments are invalid
     */
    public static Command parse(String fullCommand) throws InputException {
        String[] parts = splitCommand(fullCommand);
        return switch (parseCommandType(parts[0])) {
        case BYE -> new ExitCommand();
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
        };
    }

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
    public static String parseDescription(String[] parts) {
        return String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
    }

    /**
     * Extracts a deadline's description and its value after {@code /by}.
     *
     * @param parts words from a deadline command
     * @return a two-element array containing the description and deadline
     * @throws InputException if the command does not contain {@code /by}
     */
    public static String[] parseDeadlineDetails(String[] parts) throws InputException {
        String[] segments = parseDescription(parts).split("\\s*/by\\s*", 2);
        if (segments.length < 2) {
            throw new InputException("You forgot to include /by for this deadline.");
        }
        return new String[] {segments[0].trim(), segments[1].trim()};
    }

    /**
     * Extracts an event's description, start time, and end time.
     *
     * @param parts words from an event command
     * @return a three-element array containing the description, start time, and end time
     * @throws InputException if the command does not contain both {@code /from} and {@code /to}
     */
    public static String[] parseEventDetails(String[] parts) throws InputException {
        String[] segments = parseDescription(parts).split("/from|/to");
        if (segments.length < 3) {
            throw new InputException("You forgot to include /from and /to for this event.");
        }
        return new String[] {segments[0].trim(), segments[1].trim(), segments[2].trim()};
    }

    /**
     * Parses the optional date argument supplied to {@code list}.
     *
     * @param parts words from a list command
     * @return the requested date, or {@code null} when no date was given
     * @throws InputException if too many arguments were supplied or the date is invalid
     */
    public static LocalDate parseListDate(String[] parts) throws InputException {
        if (parts.length > 2) {
            throw new InputException("You entered too many parameters for this operation");
        }
        return parts.length == 2 ? DateTimeValue.parseDate(parts[1]) : null;
    }

    /**
     * Parses the one-based task ID supplied to a task-ID command.
     *
     * @param parts words from the user command
     * @return the parsed task ID
     * @throws InputException if the command has a missing, extra, or non-numeric ID
     */
    public static int parseTaskId(String[] parts) throws InputException {
        if (parts.length > 2) {
            throw new InputException("You entered too many parameters for this operation");
        } else if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new InputException("You need to enter a number for the task id.");
            }
        } else {
            throw new InputException("You forgot to mention the id of the task");
        }
    }
}
