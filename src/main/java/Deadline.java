public class Deadline extends Task{

    private String deadline;

    public Deadline(String name, String deadline) {
        super(name);
        this.deadline = deadline;
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
        return String.format("D | %d | %s | %s", getStatus() ? 1 : 0, getTaskName(), deadline);
    }
}
