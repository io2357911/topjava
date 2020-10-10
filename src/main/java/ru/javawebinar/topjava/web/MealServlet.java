package ru.javawebinar.topjava.web;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        if (appCtx != null) {
            appCtx.close();
        }
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (id == null) {
            LocalDate startDate = parseLocalDate(request, "startDate", LocalDate.MIN);
            LocalDate endDate = parseLocalDate(request, "endDate", LocalDate.MAX);

            LocalTime startTime = parseLocalTime(request, "startTime", LocalTime.MIN);
            LocalTime endTime = parseLocalTime(request, "endTime", LocalTime.MAX);

            List<MealTo> meals = controller.getFiltered(startDate, endDate, startTime, endTime);

            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);

        } else {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            if (meal.isNew()) {
                controller.create(meal);
            } else {
                controller.update(meal.getId(), meal);
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                request.setAttribute("meals", controller.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    static LocalDate parseLocalDate(HttpServletRequest request, String paramName, LocalDate defaultLocalDate) {
        String date = request.getParameter(paramName);
        return date == null || date.isEmpty() ? defaultLocalDate : LocalDate.parse(date);
    }

    static LocalTime parseLocalTime(HttpServletRequest request, String paramName, LocalTime defaultLocalTime) {
        String time = request.getParameter(paramName);
        return time == null || time.isEmpty() ? defaultLocalTime : LocalTime.parse(time);
    }
}
