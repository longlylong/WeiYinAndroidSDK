package com.weiyin.wysdk.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast lastToast;

    public static void show(Context context, int textResId) {
        show(context, context.getResources().getText(textResId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    private static void show(Context context, CharSequence text, int duration) {
        if (lastToast != null) {
            lastToast.cancel();
        }
        lastToast = Toast.makeText(context, text, duration);
        lastToast.show();
    }

}
