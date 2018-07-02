package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoMemoryImpl implements MealDao {
    private AtomicInteger counter = new AtomicInteger(0);

    private ConcurrentMap<Integer, Meal> repository = new ConcurrentHashMap<>();

    public MealDaoMemoryImpl() {
        int i = counter.incrementAndGet();
        Meal value = new Meal(LocalDateTime.now(), "123", 200);
        value.setId(i);
        repository.put(i, value);

        i = counter.incrementAndGet();
        Meal value1 = new Meal(LocalDateTime.now(), "123", 200);
        value1.setId(i);
        repository.put(i, value1);

        i = counter.incrementAndGet();
        Meal value2 = new Meal(LocalDateTime.now(), "123", 200);
        value2.setId(i);
        repository.put(i, value2);
    }

    @Override

    public void add(Meal meal) {
        int i = counter.incrementAndGet();
        meal.setId(i);
        repository.put(i, meal);

    }

    @Override
    public void update(Meal meal) {
        repository.put(meal.getId(), meal);

    }

    @Override
    public void remove(int id) {
        repository.remove(id);

    }

    @Override
    public Meal getByID(int ID) {
        return repository.get(ID);
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(repository.values());
    }

}
