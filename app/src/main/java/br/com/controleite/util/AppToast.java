package br.com.controleite.util;

import android.content.Context;
import android.widget.Toast;

public abstract class AppToast {

    public static void shorMsg(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void longMsg(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}

