package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);


        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = context.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                authUserId(), LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        boolean isNew = meal.isNew();
        log.info(isNew ? "Create {}" : "Update {}", meal);
        if (isNew) {
            controller.create(meal);
        } else {
            controller.update(meal, Integer.parseInt(id));
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        request.setAttribute("controll", controller);

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                String command = request.getParameter("command");
                if (command != null && command.equalsIgnoreCase("filterForm")) {

                    String startD = request.getParameter("startD");
                    String endD = request.getParameter("endD");
                    String startT = request.getParameter("startT");
                    String endT = request.getParameter("endT");
                    LocalDate startDate = (startD == null || startD.isEmpty()) ? LocalDate.MIN : LocalDate.parse(startD);
                    LocalDate endDate = (endD == null || endD.isEmpty()) ? LocalDate.MAX : LocalDate.parse(endD);
                    LocalTime startTime = (startT == null || startT.isEmpty()) ? LocalTime.MIN : LocalTime.parse(startT);
                    LocalTime endTime = (endT == null || endT.isEmpty()) ? LocalTime.MAX : LocalTime.parse(endT);

                    request.setAttribute("meals", controller.getAllFiltered(startDate, endDate, startTime, endTime));
                } else {

                    request.setAttribute("meals", controller.getAll());
                }

                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

}
