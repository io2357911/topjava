package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.util.Locale;

public final class LocalDateFormatter extends AbstractFormatter<LocalDate> {

    public LocalDateFormatter(String pattern) {
        super(pattern.isEmpty() ? "yyyy-MM-dd" : pattern);
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return date == null ? "" : date.format(getFormatter(locale));
    }

    @Override
    public LocalDate parse(String formatted, Locale locale) {
        return formatted == null ? null : LocalDate.parse(formatted, getFormatter(locale));
    }
}
