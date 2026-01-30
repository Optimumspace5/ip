import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public static Command parse(String userInput, TaskList tasks) throws AceException {
        String input = userInput.trim();

        if (input.equals("bye")) {
            return new ExitCommand();
        }

        if (input.equals("list")) {
            return new ListCommand();
        }

        if (input.startsWith("todo ")
                || input.startsWith("deadline ")
                || input.startsWith("event ")) {
            Task taskToAdd = parseAddCommand(input);
            return new AddCommand(taskToAdd);
        }

        if (input.startsWith("delete ")) {
            int index = parseDeleteIndex(input, tasks);
            return new DeleteCommand(index);
        }

        if (input.startsWith("mark ")) {
            int index = parseMarkIndex(input, tasks);
            return new MarkCommand(index);
        }

        if (input.startsWith("unmark ")) {
            int index = parseUnmarkIndex(input, tasks);
            return new UnmarkCommand(index);
        }

        // handles bare "todo", "deadline", "event" and anything unknown
        if (input.equals("todo")) {
            throw new AceException("The description of a todo cannot be empty.");
        }
        if (input.equals("deadline")) {
            throw new AceException("Invalid deadline format. Use: deadline <desc> /by <time>");
        }
        if (input.equals("event")) {
            throw new AceException("Invalid event format. Use: event <desc> /from <start> /to <end>");
        }

        throw new AceException("I don't know what that means.");
    }

    private static Task parseAddCommand(String input) throws AceException {
        if (input.startsWith("todo ")) {
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new AceException("The description of a todo cannot be empty.");
            }
            return new Todo(desc);
        }

        if (input.startsWith("deadline ")) {
            String[] parts = input.substring(9).split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty()) {
                throw new AceException("Invalid deadline format. Use: deadline <desc> /by <time>");
            }

            LocalDate by;
            try {
                by = LocalDate.parse(parts[1].trim());
            } catch (DateTimeParseException e) {
                throw new AceException("Invalid date format. Use: yyyy-MM-dd");
            }

            return new Deadline(parts[0].trim(), by);
        }

        if (input.startsWith("event ")) {
            String[] parts = input.substring(6).split(" /from | /to ");
            if (parts.length < 3 || parts[0].trim().isEmpty()) {
                throw new AceException("Invalid event format. Use: event <desc> /from <start> /to <end>");
            }
            return new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }

        throw new AceException("I don't know what that means.");
    }

    public static int parseDeleteIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, "delete", tasks);
    }

    public static int parseMarkIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, "mark", tasks);
    }

    public static int parseUnmarkIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, "unmark", tasks);
    }

    private static int parseIndex(String input, String commandWord, TaskList tasks) throws AceException {
        String prefix = commandWord + " ";
        if (!input.startsWith(prefix)) {
            throw new AceException("I don't know what that means.");
        }

        String numberPart = input.substring(prefix.length()).trim();

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            throw new AceException("Invalid task number.");
        }

        int index = taskNumber - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }

        return index;
    }
}