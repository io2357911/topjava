package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.LocalDateFormatter;
import ru.javawebinar.topjava.util.LocalTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static ru.javawebinar.topjava.util.ControllerUtil.createResponseEntity;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    private final LocalDateFormatter localDateFormatter = new LocalDateFormatter("yyyy-MM-dd");
    private final LocalTimeFormatter localTimeFormatter = new LocalTimeFormatter("HH:mm");

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        return createResponseEntity(REST_URL, super.create(meal));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @GetMapping("/filter")
//    public List<MealTo> getFiltered(
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
    public List<MealTo> getFiltered(HttpServletRequest request) {
        var locale = Locale.getDefault();
        var startDate = localDateFormatter.parse(request.getParameter("startDate"), locale);
        var endDate = localDateFormatter.parse(request.getParameter("endDate"), locale);
        var startTime = localTimeFormatter.parse(request.getParameter("startTime"), locale);
        var endTime = localTimeFormatter.parse(request.getParameter("endTime"), locale);
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}