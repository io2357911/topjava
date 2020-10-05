package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealCrud implements ICrud<Meal> {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    @Override
    public Meal add(Meal meal) {
        Meal newMeal = new Meal(counter.getAndIncrement(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        mealMap.put(newMeal.getId(), newMeal);
        return newMeal;
    }

    @Override
    public Meal update(Meal meal) {
        if (mealMap.containsKey(meal.getId())) {
            Meal newMeal = new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
            mealMap.put(meal.getId(), newMeal);
            return newMeal;
        } else {
            // It's better to throw MealNotFoundException(),
            // but currently we don't bother with it
            return null;
        }
    }

    @Override
    public void delete(int mealId) {
        mealMap.remove(mealId);
    }

    @Override
    public List<Meal> getList() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal get(int mealId) {
        return mealMap.get(mealId);
    }
}
