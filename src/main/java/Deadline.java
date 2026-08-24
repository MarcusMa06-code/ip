/** A task that must be completed by a specified date or deadline value. */
public class Deadline extends Task {

    private final DateTimeValue deadline;

    /**
     * Creates a deadline task by parsing the supplied deadline value.
     *
     * @param name task description
     * @param deadline raw value after {@code /by}
     * @throws SamanthaException if the deadline value is invalid
     */
    public Deadline(String name, String deadline) throws SamanthaException {
        super(name);
        this.deadline = DateTimeValue.parse(deadline);
    }

    @Override
    public String getType() {
        return "[D]";
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
