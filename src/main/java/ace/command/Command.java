package ace.command;


import ace.AceException;
import ace.storage.Storage;
import ace.task.TaskList;
import ace.ui.Ui;

/**
 * Represents a user command that can be executed by the application.
 * Each concrete command encapsulates a specific user action.
 */
public abstract class Command {
    /**
     * Executes the command.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The UI used to display messages to the user.
     * @param storage The storage used to persist task data.
     * @throws AceException If an error occurs during command execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws AceException;

    /**
     * Indicates whether this command signals the application to exit.
     *
     * @return {@code true} if the application should terminate, {@code false} otherwise.
     */
    public boolean isExit() {
        return false;
    }
}

