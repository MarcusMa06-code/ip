package samantha.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import samantha.exception.TaskValidationException;

class TodoTest {
    @Test
    void constructor_blankDescription_taskValidationExceptionThrown() {
        assertThrows(TaskValidationException.class, () -> new Todo("   "));
    }

    @Test
    void todo_newAndMarked_statusAndRepresentationsUpdated() throws TaskValidationException {
        Todo todo = new Todo("read book");

        assertFalse(todo.getStatus());
        assertEquals("[T]", todo.getType());
        assertEquals("[T][ ] read book", todo.toString());
        assertEquals("T | 0 | read book", todo.toFileString());
        assertFalse(todo.isOnDate(LocalDate.of(2024, 1, 1)));

        todo.markDone();
        assertTrue(todo.getStatus());
        assertEquals("[T][X] read book", todo.toString());
        assertEquals("T | 1 | read book", todo.toFileString());

        todo.markNotDone();
        assertFalse(todo.getStatus());
    }
}
