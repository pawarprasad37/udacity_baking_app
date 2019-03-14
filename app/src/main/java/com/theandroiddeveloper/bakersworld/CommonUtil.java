package com.theandroiddeveloper.bakersworld;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class CommonUtil {
    public static void showToast(final Context context, final String s, final int duration) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, s, duration)
                        .show();
            }
        });
    }
}
