package ru.javawebinar.topjava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDao dao;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public MealServlet() {
        this.dao = new MealDaoMemoryImpl();
    }

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("meal get");
        String action = req.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            dao.remove(id);
        } else if (action != null && action.equalsIgnoreCase("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("meal", dao.getByID(id));
        }
        List<MealWithExceed> filteredWithExceeded = MealsUtil.getFilteredWithExceeded(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
        req.setAttribute("list", filteredWithExceeded);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("meal post");
        req.setCharacterEncoding("UTF-8");
        String dateTime = req.getParameter("dateTime");
        LocalDateTime time = LocalDateTime.parse(dateTime, formatter);
        Meal meal = new Meal(time, req.getParameter("description"), Integer.parseInt(req.getParameter("calories")));
        String id = req.getParameter("ID");
        if (id.isEmpty()||id==null) {
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            dao.update(meal);
        }
        req.setAttribute("list", MealsUtil.getFilteredWithExceeded(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }


}
