package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;



    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        User ref = entityManager.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(ref);
            entityManager.persist(meal);
        } else {
            int i = entityManager.createQuery("UPDATE Meal m SET m.description=?1, m.dateTime=?2, m.calories=?3 WHERE m.id=?5 AND m.user.id=?4")
                    .setParameter(1, meal.getDescription())
                    .setParameter(2, meal.getDateTime())
                    .setParameter(3, meal.getCalories())
                    .setParameter(4, userId)
                    .setParameter(5, meal.getId())
                    .executeUpdate();
//            entityManager.find(Meal.class, meal.getId());
//            entityManager
            if (i != 0) {
                meal.setUser(ref);
                return meal;
            } else return null;
        }

        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {

        return entityManager.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> resultList = entityManager.createQuery("SELECT m FROM Meal m WHERE m.id=?1 AND m.user.id=?2")
                .setParameter(1, id)
                .setParameter(2, userId)
                .getResultList();
        return DataAccessUtils.singleResult(resultList);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.GET_ALL, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
//        return null;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return entityManager.createQuery("SELECT m FROM Meal m WHERE m.user.id=?1 AND m.dateTime BETWEEN ?2 AND ?3 ORDER BY m.dateTime DESC")
                .setParameter(1, userId)
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .getResultList();

    }
}