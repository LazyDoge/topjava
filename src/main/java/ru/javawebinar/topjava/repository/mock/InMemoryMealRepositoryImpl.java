package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(o -> this.save(o, o.getUserID()));
    }

    @Override
    public Meal save(Meal meal, int userID) {
        if (checkWrongUserID(meal, userID)) return null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userID) {
        if (checkWrongUserID(repository.get(id), userID)) return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userID) {
        Meal meal = repository.get(id);
        if (checkWrongUserID(meal, userID)) meal = null;
        return meal;
    }

    @Override
    public List<Meal> getAll(int userID) {
        return getAll(userID, meal -> true);
    }

    @Override
    public List<Meal> getAll(int userID, LocalDate start, LocalDate end) {
        return getAll(userID, meal -> DateTimeUtil.isBetweenDate(meal.getDate(), start, end));
    }

    public List<Meal> getAll(int userID, Predicate<Meal> filter) {
        return repository.values().stream().filter(meal -> meal.getUserID() == userID)
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private boolean checkWrongUserID(Meal meal, int userID) {
        if (meal == null) return true;
        return userID != meal.getUserID();
    }
}

