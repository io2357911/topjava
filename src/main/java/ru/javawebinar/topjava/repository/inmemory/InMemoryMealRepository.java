package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(SecurityUtil.authUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> userRepo = repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userRepo.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return userRepo.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int mealId) {
        Map<Integer, Meal> userRepo = repository.get(userId);
        return userRepo != null && userRepo.remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {
        Map<Integer, Meal> userRepo = repository.get(userId);
        return userRepo == null ? null : userRepo.get(mealId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userRepo = repository.get(userId);
        return userRepo == null ? null : userRepo.values().stream()
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }
}
