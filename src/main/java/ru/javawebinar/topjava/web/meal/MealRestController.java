package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private boolean timeFilterNeed = false;
    private boolean dateFilterNeed = false;
    private LocalTime startTime = LocalTime.MIN;
    private LocalTime endTime = LocalTime.MAX;
    private LocalDate startDate = LocalDate.MIN;
    private LocalDate endDate = LocalDate.MAX;

    private static final Logger log = getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        List<Meal> result = Collections.emptyList();
        if (dateFilterNeed) {
            result = service.getAll(authUserId(), startDate, endDate);
        } else result = service.getAll(authUserId());
        return timeFilterNeed ? MealsUtil.getFilteredWithExceeded(result,
                authUserCaloriesPerDay(), startTime, endTime) :
                MealsUtil.getWithExceeded(result, authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }


    public boolean isTimeFilterNeed() {
        return timeFilterNeed;
    }

    public void setTimeFilterNeed(boolean timeFilterNeed) {
        this.timeFilterNeed = timeFilterNeed;
    }

    public boolean isDateFilterNeed() {
        return dateFilterNeed;
    }

    public void setDateFilterNeed(boolean dateFilterNeed) {
        this.dateFilterNeed = dateFilterNeed;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}