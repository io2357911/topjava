package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class AbstractFormatter<T> implements Formatter<T> {

    protected final String pattern;

    public AbstractFormatter(String pattern) {
        this.pattern = pattern;
    }

    protected DateTimeFormatter getFormatter(Locale locale) {
        return DateTimeFormatter.ofPattern(pattern, locale);
    }
}
