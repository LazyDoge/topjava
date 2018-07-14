package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})


@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100004, USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(MEAL3);
        assertThat(meal).isEqualToIgnoringGivenFields(MEAL3);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongUser() {
        service.get(100004, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongId() {
        service.get(4, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(100004, USER_ID);
        assertThat(service.getAll(USER_ID))
                .usingElementComparatorIgnoringFields()
                .isEqualTo(Arrays.asList(MEAL1, MEAL4, MEAL2));
    }

    @Test(expected = NotFoundException.class)
    public void deleteFromAnotherUser() throws Exception {
        service.delete(100003, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongID() throws Exception {
        service.delete(2, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        assertThat(service.getBetweenDates(DateTimeUtil.parseLocalDate("2018-07-06"),
                DateTimeUtil.parseLocalDate("2018-07-07"), USER_ID))
                .usingElementComparatorIgnoringFields()
                .isEqualTo(Collections.singletonList(MEAL3));
    }

    @Test
    public void getBetweenDateTimes() {
        assertThat(service.getBetweenDateTimes(LocalDateTime.parse("2018-07-07T13:22:30"),
                LocalDateTime.parse("2018-07-07T15:22:30"), USER_ID))
                .usingElementComparatorIgnoringFields()
                .isEqualTo(Arrays.asList(MEAL3));
    }

    @Test
    public void getAll() {
        Iterable<Meal> actual = service.getAll(ADMIN_ID);
        Iterable<Meal> expected = Arrays.asList(MEAL5, MEAL6);
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL3);
        updated.setCalories(500);
        service.update(updated, USER_ID);
        assertThat(service.get(100004, USER_ID)).isEqualToIgnoringGivenFields(updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateWrongId() {
        Meal updated = new Meal(MEAL3);
        updated.setCalories(500);
        updated.setId(4);
        service.update(updated, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateWrongUser() {
        Meal updated = new Meal(MEAL3);
        updated.setCalories(500);
        service.update(updated, ADMIN_ID);
    }

    //    @Test(expected = PSQLException.class)
    @Test(expected = DataAccessException.class)
    public void sameTimeMealCreate() throws Exception {
        service.create(new Meal(LocalDateTime.parse("2018-07-09T10:22:30"), "sameTimeTest1", 1000), USER_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "testCreate", 25);
        Meal created = service.create(newMeal, SecurityUtil.authUserId());
        newMeal.setId(created.getId());
//        newMeal.setCalories(50);

        Iterable<Meal> actual = service.getAll(USER_ID);
        Iterable<Meal> expected = Arrays.asList(newMeal, MEAL1, MEAL4, MEAL2, MEAL3);
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}