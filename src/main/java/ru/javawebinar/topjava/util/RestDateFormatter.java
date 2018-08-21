package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class RestDateFormatter implements Formatter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String text, Locale locale) {

        LocalDate parse = LocalDate.parse(Optional.ofNullable(text).orElse(""));
        return parse;
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return FORMATTER.format(object);
    }
}
