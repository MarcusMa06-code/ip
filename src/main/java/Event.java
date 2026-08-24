public class Event extends Task {

    private final DateTimeValue from;
    private final DateTimeValue to;

    public Event(String name, String from, String to) throws SamanthaException {
        super(name);
        this.from = parseEventDateTime(from);
        this.to = parseEventDateTime(to);
    }

    private DateTimeValue parseEventDateTime(String text) throws SamanthaException {
        DateTimeValue dateTime = DateTimeValue.parse(text);
        if (dateTime.getTime().isEmpty()) {
            throw new SamanthaException("An event date and time must include a time in HHmm format.");
        }
        return dateTime;
    }

    @Override
    public String getType() {
        return "[E]";
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", super.toString(), this.from, this.to);
    }

    @Override
    public String toFileString() {
        String schedule = from.toStorageString() + " to " + to.toStorageString();
        return String.format("E | %d | %s | %s", getStatus() ? 1 : 0, getTaskName(), schedule);
    }
}
