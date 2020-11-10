package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {
    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }

    public static <T extends AbstractBaseEntity> void removeDuplicates(Collection<T> collection) {
        if (collection == null) {
            return;
        }
        Map<Integer, T> map = new LinkedHashMap<>();
        for (T entity : collection) {
            map.putIfAbsent(entity.getId(), entity);
        }
        collection.clear();
        collection.addAll(map.values());
    }
}