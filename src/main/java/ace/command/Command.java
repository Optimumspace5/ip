package ace.command;


import ace.AceException;
import ace.storage.Storage;
import ace.task.TaskList;
import ace.ui.Ui;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws AceException;

    public boolean isExit() {
        return false;
    }
}

