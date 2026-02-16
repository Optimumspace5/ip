package ace.parser;

import ace.AceException;
import ace.command.AddCommand;
import ace.command.Command;
import ace.command.DeleteCommand;
import ace.command.ExitCommand;
import ace.command.FindCommand;
import ace.command.ListCommand;
import ace.command.MarkCommand;
import ace.command.UnmarkCommand;
import ace.task.Deadline;
import ace.task.Event;
import ace.task.Task;
import ace.task.TaskList;
import ace.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input strings into executable {@link ace.command.Command} objects.
 * Responsible for validating command formats and converting user-provided data into
 * domain objects (e.g., tasks and indices).
 */
public class Parser {

    private static final String CMD_BYE = "bye";
    private static final String CMD_LIST = "list";
    private static final String CMD_FIND = "find";
    private static final String CMD_TODO = "todo";
    private static final String CMD_DEADLINE = "deadline";
    private static final String CMD_EVENT = "event";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_MARK = "mark";
    private static final String CMD_UNMARK = "unmark";

    private static final String ERR_UNKNOWN = "I don't know what that means.";
    private static final String ERR_INVALID_TASK_NO = "Invalid task number.";
    private static final String ERR_FIND_EMPTY = "The search keyword cannot be empty.";
    private static final String ERR_DEADLINE_FORMAT = "Invalid deadline format. Use: deadline <desc> /by <time>";
    private static final String ERR_EVENT_FORMAT = "Invalid event format. Use: event <desc> /from <start> /to <end>";
    private static final String ERR_TODO_EMPTY = "The description of a todo cannot be empty.";

    /**
     * Parses a user input line and returns the corresponding {@link Command}.
     *
     * @param userInput Raw user input.
     * @param tasks     Current task list (used for validating index-based commands).
     * @return The parsed command to be executed.
     * @throws AceException If the input is invalid or cannot be parsed into a known command.
     */
    public static Command parse(String userInput, TaskList tasks) throws AceException {
        assert userInput != null : "userInput should not be null";
        assert tasks != null : "tasks should not be null";

        String input = userInput.trim();

        if (input.equals(CMD_BYE)) {
            return new ExitCommand();
        }

        if (input.equals(CMD_LIST)) {
            return new ListCommand();
        }

        if (input.startsWith(CMD_FIND + " ") || input.equals(CMD_FIND)) {
            return parseFindCommand(input);
        }

        if (input.startsWith(CMD_TODO + " ")
                || input.startsWith(CMD_DEADLINE + " ")
                || input.startsWith(CMD_EVENT + " ")) {
            Task taskToAdd = parseAddCommand(input);
            return new AddCommand(taskToAdd);
        }

        if (input.startsWith(CMD_DELETE + " ")) {
            int index = parseDeleteIndex(input, tasks);
            return new DeleteCommand(index);
        }

        if (input.startsWith(CMD_MARK + " ")) {
            int index = parseMarkIndex(input, tasks);
            return new MarkCommand(index);
        }

        if (input.startsWith(CMD_UNMARK + " ")) {
            int index = parseUnmarkIndex(input, tasks);
            return new UnmarkCommand(index);
        }

        validateBareAddCommand(input);

        throw new AceException(ERR_UNKNOWN);
    }

    /**
     * Parses an add-task command (todo/deadline/event) into a {@link Task}.
     *
     * @param input Trimmed user input starting with "todo ", "deadline ", or "event ".
     * @return The task represented by the input.
     * @throws AceException If the add command format is invalid.
     */
    private static Task parseAddCommand(String input) throws AceException {
        if (input.startsWith(CMD_TODO + " ")) {
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new AceException(ERR_TODO_EMPTY);
            }
            return new Todo(desc);
        }

        if (input.startsWith(CMD_DEADLINE + " ")) {
            String[] parts = input.substring(9).split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty()) {
                throw new AceException(ERR_DEADLINE_FORMAT);
            }

            LocalDate by;
            try {
                by = LocalDate.parse(parts[1].trim());
            } catch (DateTimeParseException e) {
                throw new AceException("Invalid date format. Use: yyyy-MM-dd");
            }

            return new Deadline(parts[0].trim(), by);
        }

        if (input.startsWith(CMD_EVENT + " ")) {
            String[] parts = input.substring(6).split(" /from | /to ");
            if (parts.length < 3 || parts[0].trim().isEmpty()) {
                throw new AceException(ERR_EVENT_FORMAT);
            }
            return new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }

        throw new AceException(ERR_UNKNOWN);
    }

    private static Command parseFindCommand(String input) throws AceException {
        if (input.equals(CMD_FIND)) {
            throw new AceException(ERR_FIND_EMPTY);
        }

        String keyword = input.substring((CMD_FIND + " ").length()).trim();
        if (keyword.isEmpty()) {
            throw new AceException(ERR_FIND_EMPTY);
        }
        return new FindCommand(keyword);
    }

    private static void validateBareAddCommand(String input) throws AceException {
        if (input.equals(CMD_TODO)) {
            throw new AceException(ERR_TODO_EMPTY);
        }
        if (input.equals(CMD_DEADLINE)) {
            throw new AceException(ERR_DEADLINE_FORMAT);
        }
        if (input.equals(CMD_EVENT)) {
            throw new AceException(ERR_EVENT_FORMAT);
        }
    }

    /**
     * Extracts and validates the target index for a delete command.
     *
     * @param input User input starting with "delete ".
     * @param tasks Current task list for bounds checking.
     * @return Zero-based index of the task to delete.
     * @throws AceException If the index is missing, not a number, or out of range.
     */
    public static int parseDeleteIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, CMD_DELETE, tasks);
    }

    /**
     * Extracts and validates the target index for a mark command.
     *
     * @param input User input starting with "mark ".
     * @param tasks Current task list for bounds checking.
     * @return Zero-based index of the task to mark as done.
     * @throws AceException If the index is missing, not a number, or out of range.
     */
    public static int parseMarkIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, CMD_MARK, tasks);
    }

    /**
     * Extracts and validates the target index for an unmark command.
     *
     * @param input User input starting with "unmark ".
     * @param tasks Current task list for bounds checking.
     * @return Zero-based index of the task to mark as not done.
     * @throws AceException If the index is missing, not a number, or out of range.
     */
    public static int parseUnmarkIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, CMD_UNMARK, tasks);
    }

    /**
     * Shared helper for parsing and validating indices for index-based commands.
     *
     * @param input       User input line.
     * @param commandWord The command word (e.g., "delete", "mark", "unmark").
     * @param tasks       Current task list for bounds checking.
     * @return Zero-based validated index.
     * @throws AceException If the command format is wrong, the index is invalid, or out of range.
     */
    private static int parseIndex(String input, String commandWord, TaskList tasks) throws AceException {
        assert commandWord != null && !commandWord.isBlank() : "commandWord must not be blank";
        assert tasks != null : "tasks should not be null";

        String prefix = commandWord + " ";
        if (!input.startsWith(prefix)) {
            throw new AceException(ERR_UNKNOWN);
        }

        String numberPart = input.substring(prefix.length()).trim();

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            throw new AceException(ERR_INVALID_TASK_NO);
        }

        int index = taskNumber - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new AceException(ERR_INVALID_TASK_NO);
        }

        return index;
    }
}