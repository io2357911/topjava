package ru.javawebinar.topjava.util;

import java.time.LocalTime;
import java.util.Locale;

public final class LocalTimeFormatter extends AbstractFormatter<LocalTime> {

    public LocalTimeFormatter(String pattern) {
        super(pattern.isEmpty() ? "HH:mm" : pattern);
    }

    @Override
    public String print(LocalTime time, Locale locale) {
        return time == null ? "" : time.format(getFormatter(locale));
    }

    @Override
    public LocalTime parse(String formatted, Locale locale) {
        return formatted == null ? null : LocalTime.parse(formatted, getFormatter(locale));
    }
}
