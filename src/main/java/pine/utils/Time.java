package pine.utils;

/**
 * Time utility class.
 */
public class Time {
    public static long startTime = System.nanoTime();

    /**
     * @return Time since application launched to current time in seconds.
     */
    public static double time() { return (double) (System.nanoTime() - startTime) * 1E-9; }
}
