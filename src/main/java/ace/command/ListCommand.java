package ace.command;

import ace.AceException;
import ace.storage.Storage;
import ace.task.TaskList;
import ace.ui.Ui;
import ace.AceException;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AceException {
        ui.showList(tasks);
    }
}
