import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Ace {
    public static void main(String[] args) {

        System.out.println("Hello, I'm Ace.");
        System.out.println("What can i do for you?");

        Scanner scanner = new Scanner(System.in);
        TaskList tasks = new TaskList();

        Storage storage = new Storage("./data/duke.txt");

        ArrayList<Task> loadedTasks = storage.load();
        for (Task t : loadedTasks) {
            tasks.add(t);
        }

        while (true) {
            try {
                String userInput = scanner.nextLine();

                if (userInput.equals("bye")) {
                    System.out.println("Bye, Hope to see you again soon!");
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
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(i + 1 + ". " + tasks.get(i));
                    }
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

                        System.out.println("Noted. I've removed this task:");
                        System.out.println(removedTask);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");

                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                if (userInput.startsWith("todo ")) {
                    Task t = new Todo(userInput.substring(5));
                    tasks.add(t);
                    storage.save(tasks.getTasks());
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                if (userInput.startsWith("deadline ")) {
                    String[] parts = userInput.substring(9).split(" /by ", 2);
                    if (parts.length < 2) {
                        throw new AceException("Invalid deadline format. Use: deadline <desc> /by <time>");
                    }
                    String description = parts[0];
                    String byRaw = parts[1].trim();

                    LocalDate byDate;
                    try {
                        byDate = LocalDate.parse(byRaw);
                    } catch (DateTimeParseException e) {
                        throw new AceException("Invalid date format. Use: yyyy-MM-dd");
                    }

                    Task t = new Deadline(description, byDate);
                    tasks.add(t);
                    storage.save(tasks.getTasks());
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                if (userInput.startsWith("event ")) {
                    String[] parts = userInput.substring(6).split(" /from | /to ");
                    if (parts.length < 3) {
                        throw new AceException("Invalid event format. Use: event <desc> /from <start> /to <end>");
                    }
                    String description = parts[0];
                    String from = parts[1];
                    String to = parts[2];

                    Task t = new Event(description, from, to);
                    tasks.add(t);
                    storage.save(tasks.getTasks());
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println(tasks.get(taskIndex));

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
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println(tasks.get(taskIndex));
                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                throw new AceException("I don't know what that means.");

            } catch (AceException e) {
                System.out.println("Oops! " + e.getMessage());
            }
        }
    }
}