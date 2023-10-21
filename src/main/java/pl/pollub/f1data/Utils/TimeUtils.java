package pl.pollub.f1data.Utils;

public class TimeUtils {
    public static String convertToLapTimeFormat(long totalMilliseconds) {
        long minutes = totalMilliseconds / (60 * 1000);
        long seconds = (totalMilliseconds % (60 * 1000)) / 1000;
        long milliseconds = totalMilliseconds % 1000;
        return String.format("%d:%02d.%03d", minutes, seconds, milliseconds);
    }

}
