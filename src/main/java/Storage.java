import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of Samantha's task list.
 */
public class Storage {
    private static final Path FILE = Path.of("data", "samantha.txt");

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
        if (!Files.exists(FILE)) {
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(FILE);
        } catch (IOException e) {
            throw new TaskFileReadException(e);
        }
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            String[] parts = line.split("\\s*\\|\\s*", -1);
            try {
                tasks.add(parseTask(parts));
            } catch (SamanthaException | NumberFormatException e) {
                throw new CorruptedTaskFileException(lineNumber + 1, e);
            }
        }
        return tasks;
    }

    private Task parseTask(String[] parts) throws SamanthaException {
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new MalformedTaskRecordException("Malformed task record");
        }

        int status;
        try {
            status = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new MalformedTaskRecordException("Malformed task status", e);
        }
        if (status != 0 && status != 1) {
            throw new MalformedTaskRecordException("Malformed task status");
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
                    throw new MalformedTaskRecordException("Malformed event schedule");
                }
                task = new Event(requireText(parts[2]), requireText(times[0]), requireText(times[1]));
            } else if (parts.length == 5) {
                task = new Event(requireText(parts[2]), requireText(parts[3]), requireText(parts[4]));
            } else {
                throw new MalformedTaskRecordException("Wrong number of fields");
            }
        }
        default -> throw new MalformedTaskRecordException("Unknown task type");
        }

        if (status == 1) {
            task.markDone();
        }
        return task;
    }

    private void requirePartCount(String[] parts, int expected) throws SamanthaException {
        if (parts.length != expected) {
            throw new MalformedTaskRecordException("Wrong number of fields");
        }
    }

    private String requireText(String value) throws SamanthaException {
        String text = value.trim();
        if (text.isEmpty()) {
            throw new MalformedTaskRecordException("Task fields cannot be empty");
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
        try {
            Path parent = FILE.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (var writer = Files.newBufferedWriter(FILE)) {
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
