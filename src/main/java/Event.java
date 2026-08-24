public class Event extends Task{

    private String from;
    private String to;

    public Event(String name, String from, String to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getType() {
        return "[E]";
    }

    @Override
    public String toString() {
        if (to.isBlank()) {
            return String.format("%s (at: %s)", super.toString(), this.from);
        }
        return String.format("%s (from: %s to: %s)", super.toString(), this.from, this.to);
    }

    @Override
    public String toFileString() {
        String schedule = to.isBlank() ? from : from + " to " + to;
        return String.format("E | %d | %s | %s", getStatus() ? 1 : 0, getTaskName(), schedule);
    }
}
