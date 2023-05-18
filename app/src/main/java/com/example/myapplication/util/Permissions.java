package com.example.myapplication.util;

import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean validate(AppCompatActivity activity, int requestCode,
                                   String[] permissions) {
        List<String> list = new ArrayList<>();

        for(String permission : permissions) {
            boolean ok = ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_GRANTED;
            if(!ok) {
                list.add(permission);
            }
        }

        if(list.isEmpty()) {
            return true;
        }

        String[] newPermissions = new String[list.size()];
        list.toArray(newPermissions);
        ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
        return false;

    }

}
