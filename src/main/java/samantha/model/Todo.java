package samantha.model;

import java.time.LocalDate;

import samantha.exception.TaskValidationException;

public class Todo extends Task{

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
        return String.format("T | %d | %s", getStatus() ? 1 : 0, getTaskName());
    }
}
