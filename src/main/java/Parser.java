import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {
    public static Task parseAddCommand(String userInput) throws AceException {
        if (userInput.startsWith("todo ")) {
            String desc = userInput.substring(5).trim();
            if (desc.isEmpty()) {
                throw new AceException("The description of a todo cannot be empty.");
            }
            return new Todo(desc);
        }

        if (userInput.startsWith("deadline ")) {
            String[] parts = userInput.substring(9).split(" /by ", 2);
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

        if (userInput.startsWith("event ")) {
            String[] parts = userInput.substring(6).split(" /from | /to ");
            if (parts.length < 3 || parts[0].trim().isEmpty()) {
                throw new AceException("Invalid event format. Use: event <desc> /from <start> /to <end>");
            }

            return new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }

        return null; // not an add command
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

        int index = taskNumber - 1; // 0-based
        if (index < 0 || index >= tasks.size()) {
            throw new AceException("Invalid task number.");
        }

        return index;
    }
}

