package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userID) throws NotFoundException {
        return checkNotFoundWithId(repository.save(meal, userID), userID);
    }

    @Override
    public void delete(int id, int userID) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userID), userID);

    }

    @Override
    public Meal get(int id, int userID) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userID), userID);
    }

    @Override
    public void update(Meal meal, int userID) throws NotFoundException {
        checkNotFoundWithId(repository.save(meal, userID), userID);
    }

    @Override
    public List<Meal> getAll(int userID) {
        return repository.getAll(userID);
    }

    @Override
    public List<Meal> getAll(int userID, LocalDate start, LocalDate end) {
        return repository.getAll(userID, start, end);
    }
}