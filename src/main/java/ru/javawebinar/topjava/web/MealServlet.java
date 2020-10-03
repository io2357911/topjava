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
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final String MEALS_JSP = "meals.jsp";
    private static final String EDIT_MEALS_JSP = "editMeals.jsp";

    private static final String ACTION_ADD = "add";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_DELETE = "delete";

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

        if (action.equalsIgnoreCase(ACTION_ADD)) {
            Meal newMeal = new Meal(LocalDateTime.now(), "", 0);
            editMeal(request, response, action, newMeal);

        } else if (action.equalsIgnoreCase(ACTION_UPDATE)) {
            Meal meal = mealCrud.get(getMealId(request));
            editMeal(request, response, action, meal);

        } else if (action.equalsIgnoreCase(ACTION_DELETE)) {
            log.debug("Delete mealId=" + getMealId(request));
            mealCrud.delete(getMealId(request));
            response.sendRedirect("meals");

        } else {
            List<MealTo> mealsTo = filteredByStreams(mealCrud.getList(), LocalTime.of(0, 0),
                    LocalTime.of(23, 59), 2000);
            mealsTo.sort(Comparator.comparing(MealTo::getDateTime));

            listMealsTo(request, response, mealsTo);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(
                request.getParameter("dateTime"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );

        Meal meal = new Meal(
                getMealId(request),
                dateTime,
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );

        String action = getAction(request);

        if (action.equalsIgnoreCase(ACTION_ADD)) {
            log.debug("Add " + meal);
            mealCrud.add(meal);

        } else if (action.equalsIgnoreCase(ACTION_UPDATE)) {
            log.debug("Update " + meal);
            mealCrud.update(meal);
        }

        response.sendRedirect("meals");
    }

    private void editMeal(HttpServletRequest request, HttpServletResponse response, String action, Meal meal)
            throws ServletException, IOException {
        request.setAttribute("action", action);
        request.setAttribute("meal", meal);
        request.getRequestDispatcher(EDIT_MEALS_JSP).forward(request, response);
    }

    private void listMealsTo(HttpServletRequest request, HttpServletResponse response, List<MealTo> mealsTo)
            throws ServletException, IOException {
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher(MEALS_JSP).forward(request, response);
    }

    private String getAction(HttpServletRequest request) {
        String action = request.getParameter("action");
        return action != null ? action : "default";
    }

    private int getMealId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("mealId"));
    }
}
