package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

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
        return DateTimeUtil.parseLocalDate(formatted);
    }
}
