import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ace {
    public static void main(String[] args) {

        System.out.println("Hello, I'm Ace.");
        System.out.println("What can i do for you?");

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> list = new ArrayList<>();

        ArrayList<String> savedLines = loadFromFile();
        for (String line : savedLines) {
            String[] parts = line.split(" \\| "); // type, done, desc
            if (parts.length < 3) {
                continue;
            }

            String type = parts[0];
            String done = parts[1];

            Task t;

            if (type.equals("T")) {
                if (parts.length < 3) continue;
                t = new Todo(parts[2]);

            } else if (type.equals("D")) {
                if (parts.length < 4) continue;
                LocalDate byDate;
                try {
                    byDate = LocalDate.parse(parts[3].trim());
                } catch (DateTimeParseException e) {
                    continue;
                }
                t = new Deadline(parts[2], byDate);

            } else if (type.equals("E")) {
                if (parts.length < 5) continue;
                t = new Event(parts[2], parts[3], parts[4]);

            } else {
                continue;
            }

            if (done.equals("1")) {
                t.markDone();
            }
            list.add(t);
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
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println(i + 1 + ". " + list.get(i).toString());
                    }
                    continue;
                }
                if (userInput.startsWith("delete ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(7));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= list.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        Task removedTask = list.remove(taskIndex);
                        saveToFile(list);

                        System.out.println("Noted. I've removed this task:");
                        System.out.println(removedTask);
                        System.out.println("Now you have " + list.size() + " tasks in the list.");

                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                if (userInput.startsWith("todo ")) {
                    String description = userInput.substring(5);
                    list.add(new Todo(description));
                    saveToFile(list);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(list.get(list.size() - 1));
                    System.out.println("Now you have " + list.size() + " tasks in the list.");
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

                    list.add(new Deadline(description, byDate));
                    saveToFile(list);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(list.get(list.size() - 1));
                    System.out.println("Now you have " + list.size() + " tasks in the list.");
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

                    list.add(new Event(description, from, to));
                    saveToFile(list);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(list.get(list.size() - 1));
                    System.out.println("Now you have " + list.size() + " tasks in the list.");
                    continue;
                }

                if (userInput.startsWith("mark ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(5));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= list.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        list.get(taskIndex).markDone();
                        saveToFile(list);
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println(list.get(taskIndex));

                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                if (userInput.startsWith("unmark ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(7));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= list.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        list.get(taskIndex).markNotDone();
                        saveToFile(list);
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println(list.get(taskIndex));
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

    private static void saveToFile(ArrayList<Task> list) {
        try {
            Path p = Paths.get("./data/duke.txt");

            ArrayList<String> lines = new ArrayList<>();
            for (Task t : list) {
                String done = t.isDone() ? "1" : "0";

                if (t instanceof Todo) {
                    lines.add("T | " + done + " | " + t.getDescription());

                } else if (t instanceof Deadline) {
                    Deadline d = (Deadline) t;
                    lines.add("D | " + done + " | " + d.getDescription() + " | " + d.getBy());

                } else if (t instanceof Event) {
                    Event e = (Event) t;
                    lines.add("E | " + done + " | " + e.getDescription()
                            + " | " + e.getFrom() + " | " + e.getTo());
                }
            }

            Files.write(p, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to save to ./data/duke.txt");
        }
    }

    private static ArrayList<String> loadFromFile() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            Path p = Paths.get("./data/duke.txt");
            if (!Files.exists(p)) {
                return lines; // first run: no file
            }
            lines.addAll(Files.readAllLines(p, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Failed to load from ./data/duke.txt");
        }
        return lines;
    }
}
