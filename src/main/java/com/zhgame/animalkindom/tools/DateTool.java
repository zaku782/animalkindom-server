package com.zhgame.animalkindom.tools;

import java.time.*;

public class DateTool {

    public static Long getNowMillis() {
        return System.currentTimeMillis();
    }

    public static String getNowString() {
        return LocalDate.now().toString();
    }

    public static String getNDaysAfter(int n) {
        return LocalDate.now().plusDays(n).toString();
    }

    public static boolean isBefore(String date1, String date2) {
        return LocalDate.parse(date1).isBefore(LocalDate.parse(date2));
    }

    public static int getHour() {
        return LocalTime.now().getHour();
    }

    public static void main(String[] args) {

    }
}
