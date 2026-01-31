package ace.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDate by;
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    public LocalDate getBy() {
        return by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "]"
                + description + "(by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}
