package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface MealService {

    Meal create(Meal meal, int userID) throws NotFoundException;

    void delete(int id, int userID) throws NotFoundException;

    Meal get(int id, int userID) throws NotFoundException;

    void update(Meal meal, int userID) throws NotFoundException;

    List<Meal> getAll(int userID);

    List<Meal> getAll(int userID, LocalDate start, LocalDate end);

}