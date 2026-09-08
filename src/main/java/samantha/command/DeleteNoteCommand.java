package samantha.command;

import samantha.exception.InputException;
import samantha.exception.NoteFileWriteException;
import samantha.model.Note;

/**
 * Represents the command that removes a note.
 */
public class DeleteNoteCommand extends Command {
    private final int noteId;

    /**
     * Creates a command for the given one-based note ID.
     *
     * @param noteId note to remove
     */
    public DeleteNoteCommand(int noteId) {
        this.noteId = noteId;
    }

    /**
     * Removes and saves the selected note.
     *
     * @param context current application state and persistence handlers
     * @return confirmation for the removed note
     * @throws InputException if the note ID does not exist
     * @throws NoteFileWriteException if saving fails
     */
    @Override
    public String execute(CommandContext context) throws InputException, NoteFileWriteException {
        Note note = context.getNotes().removeNote(noteId);
        saveNotes(context.getNotes(), context.getNoteStorage());
        return "Noted. I've removed this note:\n  " + note + "\n"
                + String.format("Now you have %d notes in the list.", context.getNotes().size());
    }
}
