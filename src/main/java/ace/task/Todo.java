package ace.task;

/**
 * A basic task with only a description and completion status.
 * No date/time range is associated with a Todo.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    // Type marker used in UI/storage to identify Todo tasks.
    public String getTypeIcon() {
        return "T";
    }
}
