import java.time.LocalDate;

public class Event extends Task {

    private final DateTimeValue from;
    private final DateTimeValue to;

    public Event(String name, String from, String to) throws SamanthaException {
        super(name, "event");
        if (from.isBlank()) {
            throw new MissingEventStartException();
        }
        if (to.isBlank()) {
            throw new MissingEventEndException();
        }
        this.from = parseEventDateTime(from);
        this.to = parseEventDateTime(to);
    }

    private DateTimeValue parseEventDateTime(String text) throws SamanthaException {
        DateTimeValue dateTime = DateTimeValue.parse(text);
        if (dateTime.getTime().isEmpty()) {
            throw new InvalidEventTimeException();
        }
        return dateTime;
    }

    @Override
    public String getType() {
        return "[E]";
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        LocalDate fromDate = from.getDate().orElseThrow();
        LocalDate toDate = to.getDate().orElseThrow();
        return !date.isBefore(fromDate) && !date.isAfter(toDate);
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
