package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class LocalDateFormatter implements Formatter<LocalDate> {

    public LocalDateFormatter() {
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return date == null ? "" : date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public LocalDate parse(String formatted, Locale locale) {
        return formatted == null ? null : LocalDate.parse(formatted, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
