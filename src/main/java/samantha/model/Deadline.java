package samantha.model;

import java.time.LocalDate;

import samantha.exception.InputException;
import samantha.exception.TaskValidationException;

/** A task that must be completed by a specified date or deadline value. */
public class Deadline extends Task {

    private final DateTimeValue deadline;

    /**
     * Creates a deadline task by parsing the supplied deadline value.
     *
     * @param name task description
     * @param deadline raw value after {@code /by}
     * @throws TaskValidationException if the task description is blank
     * @throws InputException if the deadline value is invalid
     */
    public Deadline(String name, String deadline)
            throws TaskValidationException, InputException {
        super(name, "deadline");
        this.deadline = DateTimeValue.parse(deadline);
    }

    @Override
    public String getType() {
        return "[D]";
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        return deadline.getDate().orElseThrow().equals(date);
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), this.deadline);
    }

    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s", getStatus() ? 1 : 0, getTaskName(),
                deadline.toStorageString());
    }
}
