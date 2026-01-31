package ace.task;

/**
 * Represents a generic task with a description and completion status.
 * This class serves as the base class for all specific task types
 * such as {@link Todo}, {@link Deadline}, and {@link Event}.
 */
public class Task {
    protected final String description;
    protected boolean isDone;

    /**
     * Creates a new task with the given description.
     * Newly created tasks are marked as not done by default.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is marked as completed.
     *
     * @return {@code true} if the task is done, {@code false} otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markNotDone() {
        isDone = false;
    }

    /**
     * Returns a visual indicator of the task's completion status.
     *
     * @return "X" if the task is done, otherwise a blank space.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the type icon of this task.
     * Subclasses should override this method to provide their own type icons.
     *
     * @return The task type icon.
     */
    public String getTypeIcon() {
        return "[T]";
    }

    /**
     * Returns the string representation of this task,
     * including its type, status, and description.
     *
     * @return A formatted string representing the task.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "]" + description;
    }
}
