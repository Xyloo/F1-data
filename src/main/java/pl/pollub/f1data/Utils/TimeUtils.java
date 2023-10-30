package pl.pollub.f1data.Utils;

/**
 * Utility class for time formatting used in lap times.
 */
public class TimeUtils {
    /**
     * Converts total milliseconds to lap time format.
     * @param totalMilliseconds total milliseconds
     * @return milliseconds in lap time format
     */
    public static String convertToLapTimeFormat(long totalMilliseconds) {
        long minutes = totalMilliseconds / (60 * 1000);
        long seconds = (totalMilliseconds % (60 * 1000)) / 1000;
        long milliseconds = totalMilliseconds % 1000;
        return String.format("%d:%02d.%03d", minutes, seconds, milliseconds);
    }

}
