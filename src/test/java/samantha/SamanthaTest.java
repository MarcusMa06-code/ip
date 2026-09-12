package samantha;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import samantha.storage.NoteStorage;
import samantha.storage.Storage;

/**
 * Tests Samantha's response-oriented API used by the JavaFX interface.
 */
class SamanthaTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getInitialResponses_missingTaskFile_returnsGreeting() {
        Samantha samantha = new Samantha(storage());

        assertEquals(List.of("Hello. I'm here. What would you like to do today?"),
                samantha.getInitialResponses());
    }

    @Test
    void getResponse_todoCommand_returnsConfirmationAndPersistsTask() throws Exception {
        Samantha samantha = new Samantha(storage());

        String response = samantha.getResponse("todo read book");

        assertEquals("I'll remember that:\n  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", response);
        assertEquals("T | 0 | read book", storage().load().get(0).toFileString());
    }

    @Test
    void getResponse_undoAfterTodo_removesTaskAndPersistsEmptyList() throws Exception {
        Samantha samantha = new Samantha(storage(), noteStorage());

        samantha.getResponse("todo read book");

        assertEquals("I've undone the last command.", samantha.getResponse("undo"));
        assertTrue(storage().load().isEmpty());
        assertEquals("There is nothing to undo.", samantha.getResponse("undo"));
    }

    @Test
    void getResponse_undoAfterMark_restoresPreviousCompletionState() throws Exception {
        Samantha samantha = new Samantha(storage(), noteStorage());

        samantha.getResponse("todo read book");
        samantha.getResponse("mark 1");

        assertEquals("I've undone the last command.", samantha.getResponse("undo"));
        assertEquals("T | 0 | read book", storage().load().get(0).toFileString());
    }

    @Test
    void getResponse_undoAfterDelete_restoresTaskOrder() throws Exception {
        Samantha samantha = new Samantha(storage(), noteStorage());

        samantha.getResponse("todo first");
        samantha.getResponse("todo second");
        samantha.getResponse("delete 1");

        assertEquals("I've undone the last command.", samantha.getResponse("undo"));
        assertEquals("first", storage().load().get(0).getTaskName());
        assertEquals("second", storage().load().get(1).getTaskName());
    }

    @Test
    void getResponse_undoAfterNoteEdit_restoresPreviousNoteContent() throws Exception {
        Samantha samantha = new Samantha(storage(), noteStorage());

        samantha.getResponse("note movie title: Inception");
        samantha.getResponse("edit-note 1 movie title: Interstellar");

        assertEquals("I've undone the last command.", samantha.getResponse("undo"));
        assertEquals("movie title: Inception", noteStorage().load().getFirst().getContent());
    }

    @Test
    void getResponse_undoAfterNoteAdd_removesNoteAndPersistsEmptyList() throws Exception {
        Samantha samantha = new Samantha(storage(), noteStorage());

        samantha.getResponse("note remember this");

        assertEquals("I've undone the last command.", samantha.getResponse("undo"));
        assertTrue(noteStorage().load().isEmpty());
    }

    @Test
    void getResponse_undoAfterNoteDelete_restoresNoteOrder() throws Exception {
        Samantha samantha = new Samantha(storage(), noteStorage());

        samantha.getResponse("note first");
        samantha.getResponse("note second");
        samantha.getResponse("delete-note 1");

        assertEquals("I've undone the last command.", samantha.getResponse("undo"));
        assertEquals("first", noteStorage().load().get(0).getContent());
        assertEquals("second", noteStorage().load().get(1).getContent());
    }

    @Test
    void getResponse_noteCommands_persistAndSearchNotes() throws Exception {
        Samantha samantha = new Samantha(storage(), noteStorage());

        assertEquals("I'll keep this note:\n"
                + "  movie title: Inception\n"
                + "Now you have 1 notes in the list.", samantha.getResponse("note movie title: Inception"));
        assertEquals("I've updated this note:\n  movie title: Interstellar",
                samantha.getResponse("edit-note 1 movie title: Interstellar"));
        assertEquals("Here are the matching tasks in your list:\n\n"
                + "Here are the matching notes in your list:\n"
                + "1. movie title: Interstellar", samantha.getResponse("find interstellar"));
        assertEquals("movie title: Interstellar", noteStorage().load().getFirst().getContent());
    }

    @Test
    void getResponse_invalidCommand_returnsErrorWithoutRequestingExit() {
        Samantha samantha = new Samantha(storage());

        assertEquals("I don't recognize that. Type help if you want the guide.",
                samantha.getResponse("unknown"));
        assertFalse(samantha.isExitRequested());
    }

    @Test
    void getResponse_bye_returnsFarewellAndRequestsExit() {
        Samantha samantha = new Samantha(storage());

        assertEquals("Bye. Let's talk next time!", samantha.getResponse("bye"));
        assertTrue(samantha.isExitRequested());
    }

    private Storage storage() {
        return new Storage(temporaryDirectory.resolve("samantha.txt"));
    }

    private NoteStorage noteStorage() {
        return new NoteStorage(temporaryDirectory.resolve("notes.txt"));
    }
}
