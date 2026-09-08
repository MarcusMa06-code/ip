package samantha.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import samantha.exception.CorruptedTaskFileException;
import samantha.exception.InputException;
import samantha.exception.SamanthaException;
import samantha.exception.TaskFileReadException;
import samantha.exception.TaskFileWriteException;
import samantha.exception.TaskValidationException;
import samantha.model.Deadline;
import samantha.model.Event;
import samantha.model.Task;
import samantha.model.Todo;

/**
 * Handles persistence of Samantha's task list.
 */
public class Storage {
    private static final Path DEFAULT_FILE = Path.of("data", "samantha.txt");
    private final Path file;

    /**
     * Creates storage using Samantha's default data-file location.
     */
    public Storage() {
        this(DEFAULT_FILE);
    }

    /**
     * Creates storage backed by the supplied file.
     *
     * <p>This constructor is useful when a caller needs an isolated storage
     * location, such as for automated tests.</p>
     *
     * @param file location of the task data file
     */
    public Storage(Path file) {
        assert file != null : "Storage must have a data-file path";
        this.file = file;
    }

    /**
     * Loads all tasks from the data file.
     *
     * <p>A missing file represents a first run and therefore produces an empty
     * task list. Any existing malformed record is reported as a corrupted file.
     *
     * @return the tasks read from disk
     * @throws TaskFileReadException if the file cannot be read
     * @throws CorruptedTaskFileException if an existing record is malformed
     */
    public ArrayList<Task> load() throws TaskFileReadException, CorruptedTaskFileException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) {
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new TaskFileReadException(e);
        }
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            String[] parts = line.split("\\s*\\|\\s*", -1);
            try {
                tasks.add(parseTask(parts));
            } catch (SamanthaException | IllegalArgumentException e) {
                throw new CorruptedTaskFileException(lineNumber + 1, e);
            }
        }
        return tasks;
    }

    /**
     * Reconstructs one task from the fields in a saved data-file record.
     *
     * @param parts pipe-delimited record fields
     * @return the reconstructed task, with its saved completion state
     * @throws TaskValidationException if a task field violates the task model's rules
     * @throws InputException if a persisted date or time value is invalid
     * @throws IllegalArgumentException if the record structure is malformed
     */
    private Task parseTask(String[] parts) throws TaskValidationException, InputException {
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException("Malformed task record");
        }

        int status;
        try {
            status = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Malformed task status", e);
        }
        if (status != 0 && status != 1) {
            throw new IllegalArgumentException("Malformed task status");
        }

        Task task;
        switch (parts[0].trim()) {
            case "T" -> {
                requirePartCount(parts, 3);
                task = new Todo(requireText(parts[2]));
            }
            case "D" -> {
                requirePartCount(parts, 4);
                task = new Deadline(requireText(parts[2]), requireText(parts[3]));
            }
            case "E" -> {
                if (parts.length == 4) {
                    String schedule = requireText(parts[3]);
                    String[] times = schedule.split("\\s+to\\s+", 2);
                    if (times.length != 2) {
                        throw new IllegalArgumentException("Malformed event schedule");
                    }
                    task = new Event(requireText(parts[2]), requireText(times[0]), requireText(times[1]));
                } else if (parts.length == 5) {
                    task = new Event(requireText(parts[2]), requireText(parts[3]), requireText(parts[4]));
                } else {
                    throw new IllegalArgumentException("Wrong number of fields");
                }
            }
            default -> throw new IllegalArgumentException("Unknown task type");
        }

        if (status == 1) {
            task.markDone();
        }
        assert task != null : "A valid task record must reconstruct a task";
        return task;
    }

    /**
     * Checks that a saved record has the required number of fields.
     *
     * @param parts record fields to validate
     * @param expected required field count
     * @throws IllegalArgumentException if the field count differs from {@code expected}
     */
    private void requirePartCount(String[] parts, int expected) {
        if (parts.length != expected) {
            throw new IllegalArgumentException("Wrong number of fields");
        }
    }

    /**
     * Returns trimmed record text after ensuring that it is not blank.
     *
     * @param value raw record field
     * @return non-blank trimmed text
     * @throws IllegalArgumentException if the field is blank
     */
    private String requireText(String value) {
        String text = value.trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Task fields cannot be empty");
        }
        return text;
    }

    /**
     * Saves the current task list, creating the relative data directory when
     * necessary.
     *
     * @param tasks tasks to serialize
     * @throws TaskFileWriteException if the file cannot be created or written
     */
    public void save(List<Task> tasks) throws TaskFileWriteException {
        assert tasks != null : "Tasks to save must not be null";
        assert tasks.stream().noneMatch(task -> task == null)
                : "Tasks to save must not contain null tasks";
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (var writer = Files.newBufferedWriter(file)) {
                for (Task task : tasks) {
                    writer.write(task.toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new TaskFileWriteException(e);
        }
    }
}
