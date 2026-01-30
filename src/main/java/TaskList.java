import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task delete(int index) throws AceException {
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }
        return tasks.remove(index);
    }

    public Task get(int index) throws AceException {
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
