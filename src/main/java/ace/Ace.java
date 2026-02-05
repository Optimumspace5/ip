package ace;

import java.util.ArrayList;

import ace.command.Command;
import ace.parser.Parser;
import ace.storage.Storage;
import ace.task.Task;
import ace.task.TaskList;
import ace.ui.Ui;

/**
 * Start point of the Ace task management application.
 * Loads saved tasks, reads user commands, and delegates execution to commands.
 */
public class Ace {

    private static final String DEFAULT_FILE_PATH = "./data/duke.txt";

    private final TaskList tasks;
    private final Storage storage;

    /**
     * Constructs an Ace instance for use by GUI/other callers.
     * Loads tasks from storage into memory.
     */
    public Ace() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Constructs an Ace instance with a custom save file path.
     * Loads tasks from storage into memory.
     *
     * @param filePath file path to load/save tasks
     */
    public Ace(String filePath) {
        this.storage = new Storage(filePath);
        this.tasks = new TaskList();

        ArrayList<Task> loadedTasks = storage.load();
        for (Task t : loadedTasks) {
            tasks.add(t);
        }
    }

    /**
     * Temporary GUI-facing method for JavaFX Part 3.
     * IMPORTANT: This does NOT yet execute real commands (because current commands
     * print via Ui). It just returns a placeholder string so GUI wiring works.
     *
     * @param input user input
     * @return bot response text
     */
    public String getResponse(String input) {
        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        return "Ace heard: " + trimmed;
    }

    /**
     * CLI entry point (kept working).
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Ace ace = new Ace();

        ui.showWelcome();

        while (true) {
            try {
                String userInput = ui.readCommand();

                Command command = Parser.parse(userInput, ace.tasks);
                command.execute(ace.tasks, ui, ace.storage);

                if (command.isExit()) {
                    break;
                }

            } catch (AceException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}