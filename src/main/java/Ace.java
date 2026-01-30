import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Ace {
    public static void main(String[] args) {

        Ui ui = new Ui();
        ui.showWelcome();
        TaskList tasks = new TaskList();

        Storage storage = new Storage("./data/duke.txt");

        ArrayList<Task> loadedTasks = storage.load();
        for (Task t : loadedTasks) {
            tasks.add(t);
        }

        while (true) {
            try {
                String userInput = ui.readCommand();

                if (userInput.equals("bye")) {
                    ui.showBye();
                    break;
                }

                if (userInput.equals("todo")) {
                    throw new AceException("The description of a todo cannot be empty.");
                }

                if (userInput.equals("deadline")) {
                    throw new AceException("Invalid deadline format. Use: deadline <desc> /by <time>");
                }

                if (userInput.equals("event")) {
                    throw new AceException("Invalid event format. Use: event <desc> /from <start> /to <end>");
                }

                if (userInput.equals("list")) {
                    ui.showList(tasks);
                    continue;
                }

                if (userInput.startsWith("delete ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(7));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        Task removedTask = tasks.delete(taskIndex);
                        storage.save(tasks.getTasks());

                        ui.showDeleted(removedTask, tasks.size());

                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                Task newTask = Parser.parseAddCommand(userInput);
                if (newTask != null) {
                    tasks.add(newTask);
                    storage.save(tasks.getTasks());
                    ui.showAdded(newTask, tasks.size());
                    continue;
                }

                if (userInput.startsWith("mark ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(5));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        tasks.get(taskIndex).markDone();
                        storage.save(tasks.getTasks());
                        ui.showMarked(tasks.get(taskIndex));

                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                if (userInput.startsWith("unmark ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(7));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        tasks.get(taskIndex).markNotDone();
                        storage.save(tasks.getTasks());
                        ui.showUnmarked(tasks.get(taskIndex));
                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                throw new AceException("I don't know what that means.");

            } catch (AceException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}