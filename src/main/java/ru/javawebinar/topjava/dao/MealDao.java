package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    void add(Meal meal);

    void update(Meal meal);

    void remove(int ID);

    Meal getByID(int ID);

    List<Meal> getAllMeals();



}
