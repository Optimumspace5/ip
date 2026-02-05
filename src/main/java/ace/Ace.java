package ace;

import java.util.ArrayList;

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
     * GUI-facing method.
     * Takes user input, executes the real command logic, and returns the response text for display in GUI.
     *
     * @param input user input
     * @return bot response text (possibly multi-line)
     */
    public String getResponse(String input) {
        Ui guiUi = new Ui(true); // GUI mode: buffer output instead of printing

        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        try {
            Command command = Parser.parse(trimmed, tasks);
            command.execute(tasks, guiUi, storage);
        } catch (AceException e) {
            guiUi.showError(e.getMessage());
        }

        return guiUi.consumeOutput().trim();
    }

    /**
     * CLI entry point (kept working).
     */
    public static void main(String[] args) {
        Ui ui = new Ui();      // CLI mode (prints)
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