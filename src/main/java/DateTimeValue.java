import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the value after a deadline's {@code /by} marker.
 *
 * <p>Dates are stored as typed date and time values. A missing time represents
 * an all-day deadline.</p>
 */
public final class DateTimeValue {
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^(\\d{1,2})([/-])(\\d{1,2})\\2(\\d{4})(?:\\s+(\\d{4}))?$");
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    private final LocalDate date;
    private final Optional<LocalTime> time;

    private DateTimeValue(LocalDate date, Optional<LocalTime> time) {
        this.date = date;
        this.time = time;
    }

    /**
     * Parses a deadline value in {@code d/M/yyyy} or {@code d-M-yyyy} format,
     * with an optional {@code HHmm} time.
     *
     * @param text raw text after {@code /by}
     * @return a parsed deadline value
     * @throws InputException if the value is empty or does not follow the
     *         supported date format
     */
    public static DateTimeValue parse(String text) throws InputException {
        String value = text.trim();
        if (value.isEmpty()) {
            throw new InputException("You did not mention deadline after /by");
        }

        Matcher matcher = DATE_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw invalidFormat();
        }

        try {
            int day = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(3));
            int year = Integer.parseInt(matcher.group(4));
            LocalDate date = LocalDate.of(year, month, day);

            Optional<LocalTime> time = Optional.empty();
            if (matcher.group(5) != null) {
                String timeText = matcher.group(5);
                int hour = Integer.parseInt(timeText.substring(0, 2));
                int minute = Integer.parseInt(timeText.substring(2, 4));
                time = Optional.of(LocalTime.of(hour, minute));
            }
            return new DateTimeValue(date, time);
        } catch (DateTimeException | NumberFormatException e) {
            throw invalidFormat();
        }
    }

    public static LocalDate parseDate(String text) throws InputException {
        DateTimeValue value = parse(text);
        if (value.getTime().isPresent()) {
            throw invalidFormat();
        }
        return value.date;
    }

    private static InputException invalidFormat() {
        return new InputException(
                "The date and time must use d/M/yyyy or d-M-yyyy, optionally followed by HHmm.");
    }

    /**
     * Returns the value in a user-friendly display format.
     *
     * @return a formatted deadline
     */
    @Override
    public String toString() {
        String formattedDate = date.format(DISPLAY_DATE_FORMAT);
        return time.map(value -> formattedDate + ", "
                + value.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)))
                .orElse(formattedDate);
    }

    public String toStorageString() {
        String storedDate = date.format(STORAGE_DATE_FORMAT);
        return time.map(value -> storedDate + " "
                + value.format(DateTimeFormatter.ofPattern("HHmm")))
                .orElse(storedDate);
    }

    /**
     * Returns the parsed date.
     *
     * @return the parsed date
     */
    public Optional<LocalDate> getDate() {
        return Optional.of(date);
    }

    /**
     * Returns the parsed time, if one was supplied.
     *
     * @return the optional time
     */
    public Optional<LocalTime> getTime() {
        return time;
    }
}
