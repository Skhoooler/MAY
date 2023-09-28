package com.sklr.MAY.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A singleton responsible for formatting anything that needs formatting (e.g. Dates)
 */
public class Formatter {
    private static final ZoneId TIME_ZONE = ZoneId.of(PropertyAccess.getInstance().getValue("TIME_ZONE"));

    private static final DateTimeFormatter HTTP_DATE_FORMAT = DateTimeFormatter
            .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            .withZone(TIME_ZONE);

    private static final DateTimeFormatter LOGGER_DATE_FORMAT = DateTimeFormatter
            .ofPattern("MM/dd/yyyy HH:mm:ss - ")
            .withZone(TIME_ZONE);


    public static String formatHTTPHeaders(Map<String, Object> headers) {
        StringBuilder str = new StringBuilder("Headers:\n");

        for (String key : headers.keySet()) {
            str.append("\t").append(key).append(": ").append(headers.get(key).toString());
        }

        str.append("\n\n");
        return str.toString();
    }

    public static String formatHTTPBody(List<String> body) {
        StringBuilder str = new StringBuilder();

        for (String s : body) {
            str.append(s);
        }

        return str.toString();
    }

    /**
     * Formats a LocalDateTime object in the format specified by RFC 2616
     *      e.g. Tue, 05 May 2023 16:00:00 GMT
     * This method applies the time zone set in the properties file
     *
     * @param date The LocalDateTime object to be formatted
     * @return A string of the same LocalDateTime object, but formatted correctly
     */
    public static String formatHTTPDateTime(LocalDateTime date) {
        return HTTP_DATE_FORMAT.format(date);
    }

    /**
     * Formats a LocalDateTime object for the Logger. This doesn't follow any specification, and the format is arbitrary
     * @param date The LocalDateTime object to be formatted
     * @return A string of the same LocalDateTime object, but formatted
     */
    public static String formatLoggerDateTime(LocalDateTime date) { return LOGGER_DATE_FORMAT.format(date); }

}
