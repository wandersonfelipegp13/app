package com.example.myapplication.util;

import java.util.Calendar;
import java.util.Date;

public abstract class DateComparator {

    public static boolean isSameDay(Date firstDate, Date secondDate) {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(firstDate);

        Calendar c2 = Calendar.getInstance();
        c1.setTime(secondDate);

        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);

    }

    public static boolean isToday(Date date) {

        return isSameDay(new Date(), date);

    }

    public static boolean isYesterday(Date date) {

        Calendar today = Calendar.getInstance();

        Calendar comparedDate = Calendar.getInstance();
        comparedDate.setTime(date);

        return today.get(Calendar.DAY_OF_YEAR) == (comparedDate.get(Calendar.DAY_OF_YEAR) + 1)
                && today.get(Calendar.YEAR) == comparedDate.get(Calendar.YEAR);

    }

}
