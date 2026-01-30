public class UnmarkCommand extends Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AceException {
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }
        tasks.get(index).markNotDone();
        storage.save(tasks.getTasks());
        ui.showUnmarked(tasks.get(index));
    }
}
