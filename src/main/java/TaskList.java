import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Stores and provides operations on Samantha's tasks.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds all supplied tasks to this task list.
     *
     * @param tasks tasks to add
     */
    public void addAll(Collection<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Adds a task to the end of this task list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based position.
     *
     * @param index zero-based task position
     * @return the task at the given position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the task identified by a one-based user-facing ID.
     *
     * @param taskId one-based task ID
     * @return the matching task
     * @throws TaskNotFoundException if the ID is outside this list
     */
    public Task getTask(int taskId) throws TaskNotFoundException {
        if (taskId < 1 || taskId > tasks.size()) {
            throw new TaskNotFoundException();
        }
        return get(taskId - 1);
    }

    /**
     * Removes and returns the task at a zero-based position.
     *
     * @param index zero-based task position
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an immutable snapshot of the current tasks for persistence.
     *
     * @return snapshot of the tasks in this list
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
