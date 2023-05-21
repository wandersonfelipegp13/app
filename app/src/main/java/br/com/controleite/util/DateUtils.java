package br.com.controleite.util;

import android.content.Context;

import br.com.controleite.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class DateUtils {

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

    public static String dateToString(Context context, Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (isYesterday(date)) {

            return context.getString(R.string.yesterday);

        } else if (isToday(date)) {

            return timeToString(calendar);

        } else {

            return dateToString(calendar);

        }

    }

    public static String dateToString(Calendar calendar) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return df.format(calendar.getTime());
    }

    public static String timeToString(Calendar calendar) {
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return df.format(calendar.getTime());
    }

    public static void correctDatePickerDate(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
    }

}
