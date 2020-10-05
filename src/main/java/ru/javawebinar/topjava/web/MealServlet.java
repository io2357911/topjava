package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.crud.ICrud;
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

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final String mealsJsp = "meals.jsp";
    private static final String mealJsp = "meal.jsp";

    private static final String actionAdd = "add";
    private static final String actionUpdate = "update";
    private static final String actionDelete = "delete";

    private static final int maxCaloriesPerDay = 2000;

    private static final Logger log = getLogger(MealServlet.class);

    private final ICrud<Meal> mealCrud;

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
        String action = getAction(request);

        switch (action.toLowerCase()) {
            case actionAdd:
                Meal newMeal = new Meal(LocalDateTime.now().withSecond(0).withNano(0), "", 0);
                log.debug("Adding {}", newMeal);
                editMeal(request, response, newMeal);
                break;
            case actionUpdate:
                Meal meal = mealCrud.get(getMealId(request));
                log.debug("Updating {}", meal);
                editMeal(request, response, meal);
                break;
            case actionDelete:
                log.debug("Delete mealId={}", getMealId(request));
                mealCrud.delete(getMealId(request));
                response.sendRedirect("meals");
                break;
            default:
                List<MealTo> mealsTo = filteredByStreams(mealCrud.getList(), LocalTime.MIN,
                        LocalTime.MAX, maxCaloriesPerDay);
                listMealsTo(request, response, mealsTo);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(
                getMealId(request),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );

        String action = getAction(request);

        switch (action.toLowerCase()) {
            case actionAdd:
                log.debug("Add {}", meal);
                mealCrud.add(meal);
                break;
            case actionUpdate:
                log.debug("Update {}", meal);
                mealCrud.update(meal);
                break;
            default:
                log.debug("Unknown action '{}' in doPost", action);
        }

        response.sendRedirect("meals");
    }

    private void editMeal(HttpServletRequest request, HttpServletResponse response, Meal meal)
            throws ServletException, IOException {
        request.setAttribute("meal", meal);
        request.getRequestDispatcher(mealJsp).forward(request, response);
    }

    private void listMealsTo(HttpServletRequest request, HttpServletResponse response, List<MealTo> mealsTo)
            throws ServletException, IOException {
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher(mealsJsp).forward(request, response);
    }

    private String getAction(HttpServletRequest request) {
        String action = request.getParameter("action");
        return action != null ? action : "default";
    }

    private int getMealId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("mealId"));
    }
}
