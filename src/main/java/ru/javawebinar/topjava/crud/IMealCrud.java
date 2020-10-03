package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface IMealCrud {
    int add(Meal meal);

    void update(Meal meal);

    void delete(int mealId);

    List<Meal> getList();

    Meal get(int mealId);
}
