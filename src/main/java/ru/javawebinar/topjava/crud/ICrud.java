package ru.javawebinar.topjava.crud;

import java.util.List;

public interface ICrud<T> {
    int add(T object);

    void update(T object);

    void delete(int id);

    List<T> getList();

    T get(int id);
}
