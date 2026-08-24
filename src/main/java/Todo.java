public class Todo extends Task{

    public Todo(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "[T]";
    }

    @Override
    public String toFileString() {
        return String.format("T | %d | %s", getStatus() ? 1 : 0, getTaskName());
    }
}
