package samantha;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

        assertEquals(List.of("Hello. I’m here. What would you like to do today?"),
                samantha.getInitialResponses());
    }

    @Test
    void getResponse_todoCommand_returnsConfirmationAndPersistsTask() throws Exception {
        Samantha samantha = new Samantha(storage());

        String response = samantha.getResponse("todo read book");

        assertEquals("Got it. I've added this task: \n  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", response);
        assertEquals("T | 0 | read book", storage().load().get(0).toFileString());
    }

    @Test
    void getResponse_invalidCommand_returnsErrorWithoutRequestingExit() {
        Samantha samantha = new Samantha(storage());

        assertEquals("It seems that you entered a wrong command.", samantha.getResponse("unknown"));
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
}
