package br.com.controleite.util;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;

import java.util.Calendar;

public class DateValidatorNoFuture implements CalendarConstraints.DateValidator {

    private final Calendar date = Calendar.getInstance();

    public static final Creator<DateValidatorNoFuture> CREATOR =
            new Creator<DateValidatorNoFuture>() {
                @Override
                public DateValidatorNoFuture createFromParcel(Parcel source) {
                    return new DateValidatorNoFuture();
                }

                @Override
                public DateValidatorNoFuture[] newArray(int size) {
                    return new DateValidatorNoFuture[size];
                }
            };

    @Override
    public boolean isValid(long date) {
        this.date.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int dayOfYear = this.date.get(Calendar.DAY_OF_YEAR);
        int year = this.date.get(Calendar.YEAR);

        if (year < today.get(Calendar.YEAR)) {
            return true;
        } else if (year == today.get(Calendar.YEAR)) {
            return (dayOfYear + 1) <= today.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
