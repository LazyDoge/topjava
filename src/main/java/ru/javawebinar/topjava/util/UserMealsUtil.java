package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


public class UserMealsUtil {
    private static Map<LocalDate, Integer> recursionDateCaloriesPerDayMap = new HashMap<>();

    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );


        List<UserMealWithExceed> filteredWithExceededRecursion = getFilteredWithExceededByRecursion(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        Collections.reverse(filteredWithExceededRecursion);
        System.out.println(filteredWithExceededRecursion);


//        .toLocalDate();
//        .toLocalTime();
    }


    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateCaloriesPerDayMap = new HashMap<>();
        List<UserMeal> subResult = mealList.stream().peek(x -> dateCaloriesPerDayMap.merge(x.getDateTime().toLocalDate(), x.getCalories(), Integer::sum))
                .filter(z -> TimeUtil.isBetween(z.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
        return subResult.stream().map((y) -> new UserMealWithExceed(y, (caloriesPerDay < dateCaloriesPerDayMap.get(y.getDateTime().toLocalDate()))))
                .collect(Collectors.toList());
    }



    public static List<UserMealWithExceed> getFilteredWithExceededByRecursion(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return getFilteredWithExceededByIteratorOfMealList(mealList.iterator(), startTime, endTime, caloriesPerDay);


    }

    public static List<UserMealWithExceed> getFilteredWithExceededByIteratorOfMealList(Iterator<UserMeal> iterator, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (iterator.hasNext()) {
            UserMeal userMeal = iterator.next();
            recursionDateCaloriesPerDayMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
            List<UserMealWithExceed> filteredWithExceededResultList = getFilteredWithExceededByIteratorOfMealList(iterator, startTime, endTime, caloriesPerDay);
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredWithExceededResultList.add(new UserMealWithExceed(userMeal, (caloriesPerDay < recursionDateCaloriesPerDayMap.get(userMeal.getDateTime().toLocalDate()))));
            }
            return filteredWithExceededResultList;
        } else return new ArrayList<>();
    }
}
