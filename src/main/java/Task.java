public abstract class Task {
    private boolean isDone;
    private final String taskName; //cannot change task name once it's set

    public Task(String name) {
        this.taskName = name;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markNotDone() {
        this.isDone = false;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public boolean getStatus() {
        return isDone;
    }

    public abstract String getType();

    @Override
    public String toString() {
        String flag = isDone ? "[X] " : "[ ] ";
        return getType() + flag + this.taskName;
    }

}