package com.ac1d.rsbot.util;

import org.powerbot.script.Random;

/**
 * Utility class
 */
public class RandomUtils {
    /**
     * @return true at an odds of 1 : ratio
     */
    public static boolean oneIn(int ratio) {
        return Random.nextInt(0, ratio) == 0;
    }

    /**
     * @return true {@code percent}% of the time
     */
    public static boolean percent(int percent) {
        return Random.nextDouble() <= ((double)1/percent);
    }
}
