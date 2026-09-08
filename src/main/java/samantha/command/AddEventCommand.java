package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.exception.TaskValidationException;
import samantha.model.Event;
import samantha.model.TaskList;
import samantha.storage.Storage;

/**
 * Represents the command that adds an event task.
 */
public class AddEventCommand extends Command {
    private final String taskName;
    private final String from;
    private final String to;

    /**
     * Creates a command for the given event details.
     *
     * @param taskName event description
     * @param from event start time
     * @param to event end time
     */
    public AddEventCommand(String taskName, String from, String to) {
        this.taskName = taskName;
        this.from = from;
        this.to = to;
    }

    /**
     * Adds and saves the event task.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the newly added task
     * @throws TaskValidationException if the event details are invalid
     * @throws InputException if an event date value is invalid
     * @throws TaskFileWriteException if saving fails
     */
    @Override
    public String execute(TaskList tasks, Storage storage)
            throws TaskValidationException, InputException, TaskFileWriteException {
        return addAndSaveTask(new Event(taskName, from, to), tasks, storage);
    }
}
