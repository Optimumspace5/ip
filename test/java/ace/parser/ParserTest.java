package ace.parser;

import ace.AceException;
import ace.command.AddCommand;
import ace.command.DeleteCommand;
import ace.command.ListCommand;
import ace.command.MarkCommand;
import ace.command.UnmarkCommand;
import ace.task.TaskList;
import ace.task.Todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    @Test
    public void parseDeleteIndex_validNumber_returnsZeroBasedIndex() throws AceException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));
        tasks.add(new Todo("b"));

        int idx = Parser.parseDeleteIndex("delete 2", tasks);
        assertEquals(1, idx);
    }

    @Test
    public void parseDeleteIndex_aliasValidNumber_returnsZeroBasedIndex() throws AceException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));
        tasks.add(new Todo("b"));

        int idx = Parser.parseDeleteIndex("rm 2", tasks);
        assertEquals(1, idx);
    }

    @Test
    public void parseMarkIndex_notANumber_throwsAceException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));

        assertThrows(AceException.class, () -> Parser.parseMarkIndex("mark x", tasks));
    }

    @Test
    public void parseUnmarkIndex_outOfRange_throwsAceException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));

        assertThrows(AceException.class, () -> Parser.parseUnmarkIndex("unmark 2", tasks));
    }

    @Test
    public void parse_blankInput_throwsAceExceptionWithHelpfulMessage() {
        TaskList tasks = new TaskList();
        AceException ex = assertThrows(AceException.class, () -> Parser.parse("   ", tasks));
        assertEquals("Please enter a command.", ex.getMessage());
    }

    @Test
    public void parseFind_withoutKeyword_throwsAceException() {
        TaskList tasks = new TaskList();

        assertThrows(AceException.class, () -> Parser.parse("find", tasks));
        assertThrows(AceException.class, () -> Parser.parse("find   ", tasks));
    }

    @Test
    public void parseBareAddCommands_missingFields_throwAceException() {
        TaskList tasks = new TaskList();

        assertThrows(AceException.class, () -> Parser.parse("todo", tasks));
        assertThrows(AceException.class, () -> Parser.parse("t", tasks));
        assertThrows(AceException.class, () -> Parser.parse("deadline", tasks));
        assertThrows(AceException.class, () -> Parser.parse("event", tasks));
    }

    @Test
    public void parseEvent_missingStartOrEnd_throwsAceException() {
        TaskList tasks = new TaskList();

        assertThrows(AceException.class, () -> Parser.parse("event project /from  /to tomorrow", tasks));
        assertThrows(AceException.class, () -> Parser.parse("event project /from today /to ", tasks));
    }

    @Test
    public void parse_listAlias_returnsListCommand() throws AceException {
        TaskList tasks = new TaskList();

        assertTrue(Parser.parse("ls", tasks) instanceof ListCommand);
    }

    @Test
    public void parse_todoAlias_returnsAddCommand() throws AceException {
        TaskList tasks = new TaskList();

        assertTrue(Parser.parse("t read book", tasks) instanceof AddCommand);
    }

    @Test
    public void parse_deleteAlias_returnsDeleteCommand() throws AceException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));

        assertTrue(Parser.parse("rm 1", tasks) instanceof DeleteCommand);
    }

    @Test
    public void parse_markAlias_returnsMarkCommand() throws AceException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));

        assertTrue(Parser.parse("mk 1", tasks) instanceof MarkCommand);
    }

    @Test
    public void parse_unmarkAlias_returnsUnmarkCommand() throws AceException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));

        assertTrue(Parser.parse("um 1", tasks) instanceof UnmarkCommand);
    }
}