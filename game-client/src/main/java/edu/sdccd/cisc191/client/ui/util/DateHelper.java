package edu.sdccd.cisc191.client.ui.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    private static final String INSTANT_FORMAT = "hh:mm:ss a";

    public static String formatInstant(Instant instant) {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern(INSTANT_FORMAT);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

        return zonedDateTime.format(formatter);
    }
}
