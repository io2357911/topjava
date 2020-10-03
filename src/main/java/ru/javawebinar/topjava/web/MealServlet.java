package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.crud.IMealCrud;
import ru.javawebinar.topjava.crud.InMemoryMealCrud;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private IMealCrud mealCrud;

    public MealServlet() {
        mealCrud = new InMemoryMealCrud();

        mealCrud.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealCrud.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealCrud.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealCrud.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealCrud.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealCrud.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealCrud.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsTo = filteredByStreams(mealCrud.getList(), LocalTime.of(0, 0), LocalTime.of(23, 0), 2000);
        request.setAttribute("mealsTo", mealsTo);

        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
