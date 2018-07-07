package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {


    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final int userID;

    public Meal(int userID, LocalDateTime dateTime, String description, int calories) {
        this(null, userID, dateTime, description, calories);
    }

    public Meal(Integer id, int userID, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.userID = userID;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getUserID() {
        return userID;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return "Meal{" +
                ", id=" + id +
                ", userID=" + userID +
//                "dateTime=" + dateTime +
//                ", description='" + description + '\'' +
//                ", calories=" + calories +
                '}';
    }
}
