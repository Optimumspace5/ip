package ace.command;

import ace.storage.Storage;
import ace.task.Task;
import ace.task.TaskList;
import ace.ui.Ui;

import java.util.ArrayList;
/**
 * Finds tasks whose descriptions match the provided keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ArrayList<Task> matches = tasks.find(keyword);
        ui.showFindResults(matches);
    }
}