package samantha.model;

import java.time.LocalDate;

import samantha.exception.TaskValidationException;

/**
 * A task without a date or time constraint.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the supplied description.
     *
     * @param name task description
     * @throws TaskValidationException if the description is blank
     */
    public Todo(String name) throws TaskValidationException {
        super(name, "todo");
    }

    @Override
    public String getType() {
        return "[T]";
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        return false;
    }

    @Override
    public String toFileString() {
        return String.format("T | %d | %s", isDone() ? 1 : 0, getTaskName());
    }
}
