import java.util.ArrayList;

public class Ace {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("./data/duke.txt");

        TaskList tasks = new TaskList();
        ArrayList<Task> loadedTasks = storage.load();
        for (Task t : loadedTasks) {
            tasks.add(t);
        }

        ui.showWelcome();

        while (true) {
            try {
                String userInput = ui.readCommand();

                Command command = Parser.parse(userInput, tasks);
                command.execute(tasks, ui, storage);

                if (command.isExit()) {
                    break;
                }

            } catch (AceException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}