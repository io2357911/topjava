package ru.javawebinar.topjava.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

import java.util.Locale;

public class Util {
    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }

    public static String localize(String message, MessageSource messageSource) {
        return localize(message, messageSource, LocaleContextHolder.getLocale());
    }

    public static String localize(String message, MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(message, null, locale);
    }
}