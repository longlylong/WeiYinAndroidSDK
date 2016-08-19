package com.weiyin.wysdk.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
//        if (context instanceof Activity) {
//            SnackBar snackBar = new SnackBar(context);
//            snackBar.applyStyle(com.more.client.android.ui.library.R.style.Material_Widget_SnackBar_Tablet_MultiLine);
//            snackBar.text(text);
//            snackBar.height(ScreenUtil.dp2px(context,48));
//            snackBar.verticalPadding(0);
//            snackBar.textSize(14);
//            snackBar.duration(1688);
//            snackBar.show((Activity) context);
//        } else {
        Toast.makeText(context, text, duration).show();
//        }
    }

    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
}
