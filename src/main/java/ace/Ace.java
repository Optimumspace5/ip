package ace;

import java.util.ArrayList;
import ace.command.Command;
import ace.AceException;
import ace.parser.Parser;
import ace.storage.Storage;
import ace.task.TaskList;
import ace.ui.Ui;
import ace.task.Task;

/**
 * Start point of the Ace task management application.
 * Loads saved tasks, reads user commands, and delegates execution to commands.
 */
public class Ace {

    /**
     * Starts the Ace application loop.
     * @param args
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("./data/duke.txt");

        TaskList tasks = new TaskList();
        ArrayList<Task> loadedTasks = storage.load();
        for (Task t : loadedTasks) {
            tasks.add(t);
        }

        ui.showWelcome();

        while (true) {
            try {
                String userInput = ui.readCommand();

                Command command = Parser.parse(userInput, tasks);
                command.execute(tasks, ui, storage);

                if (command.isExit()) {
                    break;
                }

            } catch (AceException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}