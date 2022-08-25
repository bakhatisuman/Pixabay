package com.example.pixabaylibrary.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
