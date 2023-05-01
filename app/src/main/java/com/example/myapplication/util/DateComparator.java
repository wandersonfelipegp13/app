package com.example.myapplication.util;

import android.content.Context;

import com.example.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static String toString(Context context, Date date) {

        if (isYesterday(date)) {

            return context.getString(R.string.yesterday);

        } else {

            DateFormat df;

            if (isToday(date)) {

                df = new SimpleDateFormat("HH:mm", Locale.getDefault());

            } else {

                df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            }

            return df.format(date.getTime());

        }

    }

}
