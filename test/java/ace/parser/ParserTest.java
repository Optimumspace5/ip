package ace.parser;

import ace.AceException;
import ace.task.TaskList;
import ace.task.Todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertThrows(AceException.class, () -> Parser.parse("deadline", tasks));
        assertThrows(AceException.class, () -> Parser.parse("event", tasks));
    }

    @Test
    public void parseEvent_missingStartOrEnd_throwsAceException() {
        TaskList tasks = new TaskList();

        assertThrows(AceException.class, () -> Parser.parse("event project /from  /to tomorrow", tasks));
        assertThrows(AceException.class, () -> Parser.parse("event project /from today /to ", tasks));
    }
}

