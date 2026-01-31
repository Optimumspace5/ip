package ace.storage;

import ace.task.Deadline;
import ace.task.Event;
import ace.task.Task;
import ace.task.Todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void saveThenLoad_roundTrip_preservesTasks() {
        Path file = tempDir.resolve("duke.txt");
        Storage storage = new Storage(file.toString());

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("return book", LocalDate.parse("2025-12-01")));
        tasks.add(new Event("exam", "2pm", "4pm"));

        storage.save(tasks);

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());

        // Compare user-visible output (stable for your app)
        assertEquals(tasks.get(0).toString(), loaded.get(0).toString());
        assertEquals(tasks.get(1).toString(), loaded.get(1).toString());
        assertEquals(tasks.get(2).toString(), loaded.get(2).toString());
    }
}
