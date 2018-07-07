package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
//            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userNameA", "email@mail.ru", "password", Role.ROLE_ADMIN));
            adminUserController.create(new User(null, "userNameB", "email2@mail.ru", "password", Role.ROLE_ADMIN));
            adminUserController.create(new User(null, "userName1", "email2@mail.ru", "password", Role.ROLE_ADMIN));
//            System.out.println(adminUserController.getAll());
            System.out.println();
            System.out.println();
            System.out.println();

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
            mealRestController.create(new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
            mealRestController.create(new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
            mealRestController.create(new Meal(1, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
            mealRestController.create(new Meal(2, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
            mealRestController.create(new Meal(2, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));

            System.out.println(mealRestController.getAll());

        }
    }
}
