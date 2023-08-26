package com.petstore.utils;

import java.util.Random;

/**
 * Utility class containing helpful methods for various tasks.
 */
public class Utils {

    private static final Random RANDOM = new Random();

    /**
     * Generates a random ID for an order within the range of 1 to 1000.
     *
     * @return A random long value representing the order ID.
     */
    public static long generateOrderId() {
        return RANDOM.nextInt(1000) + 1;
    }
}
