package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal, int userID);

    boolean delete(int id, int userID);

    Meal get(int id, int userID);

    List<Meal> getAll(int userID);

    List<Meal> getAll(int userID, LocalDate start, LocalDate end);
}
