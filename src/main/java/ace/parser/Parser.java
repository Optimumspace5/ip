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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // C-FriendlierSyntax aliases
    private static final String ALIAS_LIST = "ls";
    private static final String ALIAS_TODO = "t";
    private static final String ALIAS_DELETE = "rm";
    private static final String ALIAS_MARK = "mk";
    private static final String ALIAS_UNMARK = "um";

    private static final String ERR_UNKNOWN = "I don't know what that means.";
    private static final String ERR_INVALID_TASK_NO = "Invalid task number.";
    private static final String ERR_FIND_EMPTY = "The search keyword cannot be empty.";
    private static final String ERR_DEADLINE_FORMAT = "Invalid deadline format. Use: deadline <desc> /by <time>";
    private static final String ERR_EVENT_FORMAT = "Invalid event format. Use: event <desc> /from <start> /to <end>";
    private static final String ERR_TODO_EMPTY = "The description of a todo cannot be empty.";
    private static final String ERR_EMPTY_INPUT = "Please enter a command.";

    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.+?)\\s+/by\\s+(.+)$");
    private static final Pattern EVENT_PATTERN = Pattern.compile("^(.+?)\\s+/from\\s+(.+?)\\s+/to\\s+(.+)$");

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
        if (input.isEmpty()) {
            throw new AceException(ERR_EMPTY_INPUT);
        }

        if (input.equals(CMD_BYE)) {
            return new ExitCommand();
        }

        if (input.equals(CMD_LIST) || input.equals(ALIAS_LIST)) {
            return new ListCommand();
        }

        if (input.startsWith(CMD_FIND + " ") || input.equals(CMD_FIND)) {
            return parseFindCommand(input);
        }

        if (input.startsWith(CMD_TODO + " ")
                || input.startsWith(ALIAS_TODO + " ")
                || input.startsWith(CMD_DEADLINE + " ")
                || input.startsWith(CMD_EVENT + " ")) {
            Task taskToAdd = parseAddCommand(input);
            return new AddCommand(taskToAdd);
        }

        if (input.startsWith(CMD_DELETE + " ") || input.startsWith(ALIAS_DELETE + " ")) {
            int index = parseDeleteIndex(input, tasks);
            return new DeleteCommand(index);
        }

        if (input.startsWith(CMD_MARK + " ") || input.startsWith(ALIAS_MARK + " ")) {
            int index = parseMarkIndex(input, tasks);
            return new MarkCommand(index);
        }

        if (input.startsWith(CMD_UNMARK + " ") || input.startsWith(ALIAS_UNMARK + " ")) {
            int index = parseUnmarkIndex(input, tasks);
            return new UnmarkCommand(index);
        }

        validateBareAddCommand(input);

        throw new AceException(ERR_UNKNOWN);
    }

    /**
     * Parses an add-task command (todo/deadline/event) into a {@link Task}.
     *
     * @param input Trimmed user input starting with "todo ", "t ", "deadline ", or "event ".
     * @return The task represented by the input.
     * @throws AceException If the add command format is invalid.
     */
    private static Task parseAddCommand(String input) throws AceException {
        if (input.startsWith(CMD_TODO + " ") || input.startsWith(ALIAS_TODO + " ")) {
            String desc = input.startsWith(CMD_TODO + " ")
                    ? input.substring((CMD_TODO + " ").length()).trim()
                    : input.substring((ALIAS_TODO + " ").length()).trim();
            if (desc.isEmpty()) {
                throw new AceException(ERR_TODO_EMPTY);
            }
            return new Todo(desc);
        }

        if (input.startsWith(CMD_DEADLINE + " ")) {
            String content = input.substring((CMD_DEADLINE + " ").length()).trim();
            Matcher matcher = DEADLINE_PATTERN.matcher(content);
            if (!matcher.matches()) {
                throw new AceException(ERR_DEADLINE_FORMAT);
            }

            String description = matcher.group(1).trim();
            String byRaw = matcher.group(2).trim();
            if (description.isEmpty() || byRaw.isEmpty()) {
                throw new AceException(ERR_DEADLINE_FORMAT);
            }

            LocalDate by;
            try {
                by = LocalDate.parse(byRaw);
            } catch (DateTimeParseException e) {
                throw new AceException("Invalid date format. Use: yyyy-MM-dd");
            }

            return new Deadline(description, by);
        }

        if (input.startsWith(CMD_EVENT + " ")) {
            String content = input.substring((CMD_EVENT + " ").length()).trim();
            Matcher matcher = EVENT_PATTERN.matcher(content);
            if (!matcher.matches()) {
                throw new AceException(ERR_EVENT_FORMAT);
            }

            String description = matcher.group(1).trim();
            String from = matcher.group(2).trim();
            String to = matcher.group(3).trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new AceException(ERR_EVENT_FORMAT);
            }

            return new Event(description, from, to);
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
        if (input.equals(CMD_TODO) || input.equals(ALIAS_TODO)) {
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
     */
    public static int parseDeleteIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, tasks, CMD_DELETE, ALIAS_DELETE);
    }

    /**
     * Extracts and validates the target index for a mark command.
     */
    public static int parseMarkIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, tasks, CMD_MARK, ALIAS_MARK);
    }

    /**
     * Extracts and validates the target index for an unmark command.
     */
    public static int parseUnmarkIndex(String input, TaskList tasks) throws AceException {
        return parseIndex(input, tasks, CMD_UNMARK, ALIAS_UNMARK);
    }

    /**
     * Shared helper for parsing and validating indices for index-based commands.
     */
    private static int parseIndex(String input, TaskList tasks, String... commandWords) throws AceException {
        assert tasks != null : "tasks should not be null";
        assert commandWords != null && commandWords.length > 0 : "commandWords must not be empty";

        String numberPart = null;
        for (String commandWord : commandWords) {
            assert commandWord != null && !commandWord.isBlank() : "commandWord must not be blank";
            String prefix = commandWord + " ";
            if (input.startsWith(prefix)) {
                numberPart = input.substring(prefix.length()).trim();
                break;
            }
        }

        if (numberPart == null) {
            throw new AceException(ERR_UNKNOWN);
        }

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