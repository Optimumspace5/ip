package ace.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    // Due date for this deadline task.
    private final LocalDate by;
    // Shared output format used when displaying due dates to users.
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    // Returns the due date for filtering/sorting/storage logic.
    public LocalDate getBy() {
        return by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    // Includes formatted due date in user-facing task display.
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "]"
                + description + "(by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}
