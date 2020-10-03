package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryMealCrud implements IMealCrud {
    private int counter = 0;
    private Map<Integer, Meal> mealMap = new HashMap<>();

    @Override
    public int add(Meal meal) {
        synchronized (mealMap) {
            Meal newUser = new Meal(counter, meal.getDateTime(), meal.getDescription(), meal.getCalories());
            mealMap.put(counter, newUser);
            counter++;
            return counter;
        }
    }

    @Override
    public void update(Meal meal) {
        synchronized (mealMap) {
            int id = meal.getId();
            Meal newMeal = new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories());
            mealMap.put(id, newMeal);
        }
    }

    @Override
    public void delete(int mealId) {
        synchronized (mealMap) {
            mealMap.remove(mealId);
        }
    }

    @Override
    public List<Meal> getList() {
        synchronized (mealMap) {
            return mealMap.values().stream().collect(Collectors.toList());
        }
    }

    @Override
    public Meal get(int mealId) {
        synchronized (mealMap) {
            return mealMap.get(mealId);
        }
    }
}
