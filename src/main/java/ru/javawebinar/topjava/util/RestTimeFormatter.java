package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RestTimeFormatter implements Formatter<LocalTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return FORMATTER.format(object);
    }
}
