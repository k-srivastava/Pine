package pine.utils;

public class Time {
    public static long startTime = System.nanoTime();

    public static double time() { return (double) (System.nanoTime() - startTime) * 1E-9; }
}
