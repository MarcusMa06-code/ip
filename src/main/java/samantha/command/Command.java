package samantha.command;

import samantha.exception.InputException;
import samantha.exception.TaskFileWriteException;
import samantha.exception.TaskValidationException;
import samantha.model.Task;
import samantha.model.TaskList;
import samantha.storage.Storage;
import samantha.ui.Ui;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /** The task added by an add command, when this command is undoable. */
    private Task addedTask;

    /**
     * Creates a command.
     */
    public Command() {
    }

    /**
     * Performs this command and returns its user-facing response.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return response for the command
     * @throws TaskValidationException if task data is invalid
     * @throws InputException if a task value has invalid input format
     * @throws TaskFileWriteException if the task list cannot be saved
     */
    public abstract String execute(TaskList tasks, Storage storage)
            throws TaskValidationException, InputException, TaskFileWriteException;

    /**
     * Performs this command and displays its response through the console UI.
     *
     * @param tasks current task list
     * @param ui console interaction handler
     * @param storage task persistence handler
     * @throws TaskValidationException if task data is invalid
     * @throws InputException if a task value has invalid input format
     * @throws TaskFileWriteException if the task list cannot be saved
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws TaskValidationException, InputException, TaskFileWriteException {
        String response = execute(tasks, storage);
        if (!response.isEmpty()) {
            ui.showResponse(response);
        }
    }

    /**
     * Returns whether executing this command should end the application.
     *
     * @return {@code true} only for an exit command
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Returns whether this command can be undone.
     *
     * @return {@code true} when this command changes the task list
     */
    public boolean isUndoable() {
        return false;
    }

    /**
     * Returns whether this command requests undoing the latest command.
     *
     * @return {@code true} only for the undo command
     */
    public boolean isUndo() {
        return false;
    }

    /**
     * Reverses this command.
     *
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the undone command
     * @throws InputException if the command cannot be undone
     * @throws TaskFileWriteException if the task list cannot be saved
     */
    public String undo(TaskList tasks, Storage storage)
            throws InputException, TaskFileWriteException {
        if (addedTask == null) {
            throw new InputException("This command cannot be undone.");
        }
        tasks.removeTask(addedTask);
        saveTasks(tasks, storage);
        return "I've undone the last command.";
    }

    /**
     * Saves the current task list through the storage component.
     *
     * @param tasks task list to save
     * @param storage task persistence handler
     * @throws TaskFileWriteException if the task list cannot be saved
     */
    protected void saveTasks(TaskList tasks, Storage storage) throws TaskFileWriteException {
        storage.save(tasks.asList());
    }

    /**
     * Adds a task, persists the updated list, and returns its confirmation.
     *
     * @param task task to add
     * @param tasks current task list
     * @param storage task persistence handler
     * @return confirmation for the added task
     * @throws TaskFileWriteException if the task list cannot be saved
     */
    protected String addAndSaveTask(Task task, TaskList tasks, Storage storage)
            throws TaskFileWriteException {
        tasks.add(task);
        saveTasks(tasks, storage);
        addedTask = task;
        return getTaskAddedResponse(task, tasks.size());
    }

    /**
     * Returns the confirmation after a new task is added.
     *
     * @param task newly added task
     * @param taskCount number of tasks after the addition
     * @return confirmation for the added task
     */
    protected String getTaskAddedResponse(Task task, int taskCount) {
        return "Got it. I've added this task: \n  "
                + task + "\n"
                + String.format("Now you have %d tasks in the list.", taskCount);
    }

    /**
     * Returns the task for a one-based task ID after validating its range.
     *
     * @param tasks current task list
     * @param taskId one-based task ID
     * @return the matching task
     * @throws InputException if the task ID is outside the task list
     */
    protected Task getTask(TaskList tasks, int taskId) throws InputException {
        return tasks.getTask(taskId);
    }

}
