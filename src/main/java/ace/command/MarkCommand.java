package ace.command;

import ace.task.TaskList;
import ace.ui.Ui;
import ace.storage.Storage;

import ace.AceException;

public class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AceException {
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }
        tasks.get(index).markDone();
        storage.save(tasks.getTasks());
        ui.showMarked(tasks.get(index));
    }
}
