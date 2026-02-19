package ace.task;
/**
 * A task that spans a start and end time (or date/time text).
 */
public class Event extends Task {
     // Start time/date text as entered or parsed.
    private String from;
    // End time/date text as entered or parsed.
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    // Accessors used by command logic and persistence layer.
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }
    
     // Includes event time range in user-facing task display.
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + "(from: " + from + " to: " + to + ")";
    }
}
