package com.example.myapplication.util;

import android.text.Editable;
import android.widget.TextView;

public abstract class InputValidator {

    public static boolean isValid(TextView textView) {

        if (textView.getText() == null)
            return false;

        return !textView.getText().toString().isEmpty();

    }

}
