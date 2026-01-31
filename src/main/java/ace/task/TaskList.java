package ace.task;

import ace.AceException;

import java.util.ArrayList;

/**
 * Represents a collection of {@link Task} objects.
 * Provides methods to add, remove, retrieve, and query tasks
 * while enforcing valid task indices.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list backed by the given list of tasks.
     *
     * @param tasks The list of tasks to use.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to this task list.
     *
     * @param t The task to add.
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The index of the task to remove (0-based).
     * @return The removed task.
     * @throws AceException If the index is invalid.
     */
    public Task delete(int index) throws AceException {
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index The index of the task to retrieve (0-based).
     * @return The task at the given index.
     * @throws AceException If the index is invalid.
     */
    public Task get(int index) throws AceException {
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return The list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
