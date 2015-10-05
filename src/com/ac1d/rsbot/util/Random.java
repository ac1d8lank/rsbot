package com.ac1d.rsbot.util;

/**
 * Utility class
 */
public class Random {
    private static final java.util.Random RAND = new java.util.Random(System.currentTimeMillis());

    /**
     * @return a random integer between min and max, inclusive
     */
    public static int get(int min, int max) {
        return min + nextInt(max - min);
    }

    /**
     * @return a number between 0 and max, inclusive
     */
    public static int get(int max) {
        return nextInt(max);
    }

    /**
     * @return true at an odds of 1 : ratio
     */
    public static boolean oneIn(int ratio) {
        return nextInt(ratio) == 0;
    }

    /**
     * @return true {@code percent}% of the time
     */
    public static boolean percent(int percent) {
        return nextDouble() <= ((double)1/percent);
    }

    /**
     * Paranoid wrap {@link java.util.Random#nextInt} in case client peeks at last number for detection
     */
    private static int nextInt(int n) {
        RAND.nextInt();
        final int value = RAND.nextInt(n);
        RAND.nextInt();
        return value;
    }

    /**
     * Paranoid wrap for {@link java.util.Random#nextDouble} in case client peeks at last number for detection
     */
    private static double nextDouble() {
        RAND.nextDouble();
        final double value = RAND.nextDouble();
        RAND.nextDouble();
        return value;
    }
}
