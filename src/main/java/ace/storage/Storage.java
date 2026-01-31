package ace.storage;

import ace.task.Deadline;
import ace.task.Event;
import ace.task.Task;
import ace.task.Todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Handles persistent storage of tasks to and from a local text file.
 * Responsible for encoding tasks into strings for saving, and decoding
 * saved strings back into {@link Task} objects when loading.
 */
public class Storage {
    private final Path path;

    /**
     * Creates a Storage object that reads from and writes to the given file path.
     *
     * @param filePath Relative or absolute path to the task storage file.
     */
    public Storage(String filePath) {
        this.path = Paths.get(filePath);
    }

    /**
     * Loads tasks from the storage file.
     * Each line in the file is decoded into a {@link Task} object.
     *
     * @return A list of tasks loaded from disk. Returns an empty list if the file
     * does not exist or cannot be read.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(path)) {
            return tasks;
        }

        try {
            ArrayList<String> lines = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
            for (String line : lines) {
                Task t = decodeTask(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load from " + path);
        }

        return tasks;
    }

    /**
     * Saves the given list of tasks to the storage file.
     * Tasks are encoded into strings before being written to disk.
     *
     * @param tasks The list of tasks to be saved.
     */
    public void save(ArrayList<Task> tasks) {
        ArrayList<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            String encoded = encodeTask(t);
            if (!encoded.isEmpty()) {
                lines.add(encoded);
            }
        }

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to save to " + path);
        }
    }

    /**
     * Decodes a single line from the storage file into a {@link Task}.
     * The line format is expected to follow the Duke encoding convention.
     *
     * @param line A single line read from the storage file.
     * @return The decoded Task, or {@code null} if the line format is invalid.
     */
    private Task decodeTask(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        String done = parts[1].trim();

        Task t;

        if (type.equals("T")) {
            t = new Todo(parts[2]);

        } else if (type.equals("D")) {
            if (parts.length < 4) return null;
            try {
                LocalDate by = LocalDate.parse(parts[3].trim());
                t = new Deadline(parts[2], by);
            } catch (DateTimeParseException e) {
                return null;
            }

        } else if (type.equals("E")) {
            if (parts.length < 5) return null;
            t = new Event(parts[2], parts[3], parts[4]);

        } else {
            return null;
        }

        if (done.equals("1")) {
            t.markDone();
        }
        return t;
    }

    /**
     * Encodes a {@link Task} into its string representation for storage.
     *
     * @param t The task to encode.
     * @return The encoded string representation of the task, or an empty string
     * if the task type is unsupported.
     */
    private String encodeTask(Task t) {
        String done = t.isDone() ? "1" : "0";

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.getDescription();

        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy();

        } else if (t instanceof Event) {
            Event e = (Event) t;
            return "E | " + done + " | " + e.getDescription()
                    + " | " + e.getFrom() + " | " + e.getTo();
        }

        return "";
    }
}
