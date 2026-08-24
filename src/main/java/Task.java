import java.time.LocalDate;

/**
 * Common state and behavior shared by all task types.
 */
public abstract class Task {
    private boolean isDone;
    private final String taskName;

    /**
     * Creates a task after checking its required description.
     *
     * @param name task description
     * @param taskType user-facing task type name
     * @throws EmptyTaskDescriptionException if the description is blank
     */
    public Task(String name, String taskType) throws EmptyTaskDescriptionException {
        if (name.isBlank()) {
            throw new EmptyTaskDescriptionException(taskType);
        }
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

    public abstract boolean isOnDate(LocalDate date);

    /**
     * Returns the task in the format used by {@link Storage}.
     *
     * @return one serialized task record
     */
    public abstract String toFileString();

    @Override
    public String toString() {
        String flag = isDone ? "[X] " : "[ ] ";
        return getType() + flag + this.taskName;
    }

}
