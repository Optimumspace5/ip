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
}
