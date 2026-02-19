package ace.command;

import ace.AceException;
import ace.storage.Storage;
import ace.task.TaskList;
import ace.ui.Ui;
/**
 * Terminates the application loop after showing a farewell message.
 */
public class ExitCommand extends Command {

    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
    }

    public boolean isExit() {
        return true;
    }
}
