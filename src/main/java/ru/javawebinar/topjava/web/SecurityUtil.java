package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    public static void setAuthUserID(int authUserID) {
        SecurityUtil.authUserID = authUserID;
    }

    private static int authUserID = 1;

    public static int authUserId() {
        return authUserID;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static int getAuthUserID() {
        return authUserID;
    }


}