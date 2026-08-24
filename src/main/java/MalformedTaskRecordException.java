/**
 * Indicates that one serialized task record does not follow the file format.
 */
public class MalformedTaskRecordException extends StorageException {
    /**
     * Creates an exception with an internal description of the malformed data.
     *
     * @param message internal description of the format problem
     */
    public MalformedTaskRecordException(String message) {
        super(message, null);
    }

    /**
     * Creates an exception with an internal description and underlying cause.
     *
     * @param message internal description of the format problem
     * @param cause lower-level parsing failure
     */
    public MalformedTaskRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
