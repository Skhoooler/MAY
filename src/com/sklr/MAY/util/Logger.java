package com.sklr.MAY.util;

import java.time.LocalDateTime;

/**
 * Logger is responsible for formatting all output to the console. The goal is to
 * have a uniform format for each type of message sent to the console.
 */
public class Logger {
    private static final String RESET = "\u001B[0m";
    private static final String ERROR = "\u001B[31m";
    private static final String WARN  = "\u001B[33m";
    private static final String POP   = "\u001B[32m";
    private static final String BLACK_TEXT  = "\u001B[30m";
    private static final String YELLOW_TEXT = "\u001B[33m";
    private static final String TITLE_BACKGROUND = "\u001B[42m";

    /**
     * Prints the title of the server to the console
     */
    public static void title() {
        System.out.println(time() + TITLE_BACKGROUND + BLACK_TEXT + "####################################################" + RESET);
        System.out.println(time() + TITLE_BACKGROUND + BLACK_TEXT + "#                INITIALIZING M.A.Y                #" + RESET);
        System.out.println(time() + TITLE_BACKGROUND + BLACK_TEXT + "####################################################" + RESET);
        pop(PropertyAccess.getInstance().getVersionedName());
    }

    /**
     * Used for regular messages to the console
     * @param s The logging message to be sent to the console.
     */
    public static void log(String s) {
        System.out.println(time() + "LOG: " + s + RESET);
    }

    /**
     * Used for warning messages
     * @param s The warning message to be sent to the console
     */
    public static void warn(String s) {
        System.out.println(time() + WARN + "WARN: " + s + RESET);
    }

    /**
     * For use with errors that the server can recover from.
     * @param s The error message to be sent to the console
     */
    public static void error(String s) {
        System.out.println(time() + ERROR + "ERROR: " + s + RESET);
    }

    /**
     * For when you need some text to be more easily picked out from the other stuff
     * @param s The message that needs to pop
     */
    public static void pop(String s) { System.out.println(time() + POP + s + RESET); }

    /**
     * For printing test names
     * @param s The title of the test
     */
    public static void test(String s) {
        System.out.println("\n" + YELLOW_TEXT + "####################################################" + RESET);
        System.out.println(s);
        System.out.println(YELLOW_TEXT + "####################################################" + RESET);
    }

    /**
     * Prints the subtest name
     * @param s The name of the subtest
     */
    public static void subtest(String s) {
        System.out.println("\n" + YELLOW_TEXT + "SUBTEST: " + s + RESET);
    }

    /**
     * @return Returns the date and time at the current moment
     */
    private static String time() {
        return Formatter.formatLoggerDateTime(LocalDateTime.now());
    }

}

// References
// https://www.geeksforgeeks.org/how-to-print-colored-text-in-java-console/