package ace.command;

import ace.task.Task;
import ace.task.TaskList;
import ace.ui.Ui;
import ace.storage.Storage;

/**
 * Represents a command that adds a task to the task list.
 */
public class AddCommand extends Command {
    /**
     * The task to be added to the task list.
     */
    private final Task task;

    /**
     * Constructs an AddCommand with the specified task.
     *
     * @param task The task to be added.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task to the task list, saves the updated list,
     * and displays a confirmation message to the user.
     *
     * @param tasks   The task list to add the task to.
     * @param ui      The UI used to display feedback to the user.
     * @param storage The storage used to persist the updated task list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAdded(task, tasks.size());
    }
}
