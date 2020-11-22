package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractFormatter<T> implements Formatter<T> {

    protected final String pattern;

    private final Map<Locale, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();

    public AbstractFormatter(String pattern) {
        this.pattern = pattern;
    }

    protected DateTimeFormatter getFormatter(Locale locale) {
        return formatterMap.computeIfAbsent(locale, l -> DateTimeFormatter.ofPattern(pattern, l));
    }
}
