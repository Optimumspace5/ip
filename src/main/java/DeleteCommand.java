public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AceException {
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }
        Task removed = tasks.delete(index);
        storage.save(tasks.getTasks());
        ui.showDeleted(removed, tasks.size());
    }
}
