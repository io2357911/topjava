package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class LocalTimeFormatter implements Formatter<LocalTime> {

    public LocalTimeFormatter() {
    }

    @Override
    public String print(LocalTime time, Locale locale) {
        return time == null ? "" : time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public LocalTime parse(String formatted, Locale locale) {
        return formatted == null ? null : LocalTime.parse(formatted, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
