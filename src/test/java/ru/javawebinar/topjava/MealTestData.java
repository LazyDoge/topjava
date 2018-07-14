package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    private static int currentNum = START_SEQ;
    public static final int USER_ID = currentNum++;
    public static final int ADMIN_ID = currentNum++;

    public static final Meal MEAL1 = new Meal(currentNum++, LocalDateTime.parse("2018-07-09T10:22:30"), "Прием1", 1000);
    public static final Meal MEAL2 = new Meal(currentNum++, LocalDateTime.parse("2018-07-08T17:22:30"), "Прием2", 1000);
    public static final Meal MEAL3 = new Meal(currentNum++, LocalDateTime.parse("2018-07-07T14:22:30"), "Прием3", 1000);
    public static final Meal MEAL4 = new Meal(currentNum++, LocalDateTime.parse("2018-07-09T03:22:30"), "Прием4", 1000);
    public static final Meal MEAL5 = new Meal(currentNum++, LocalDateTime.parse("2018-07-08T01:22:30"), "Прием5", 1000);
    public static final Meal MEAL6 = new Meal(currentNum++, LocalDateTime.parse("2018-07-07T02:22:30"), "Прием6", 1000);



}
