package com.weiyin.wysdk.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ScreenUtil {

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        @SuppressWarnings("deprecation")
        int mResWidth = (int) (wm.getDefaultDisplay().getWidth());

        return mResWidth;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        @SuppressWarnings("deprecation")
        int mResHeight = (int) (wm.getDefaultDisplay().getHeight());
        return mResHeight;

    }

    /**
     * 得到状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Activity context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Rect frame = new Rect();
            context.getWindow().getDecorView()
                    .getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                pxValue, context.getResources().getDisplayMetrics());
    }

    public static List<Integer> getLocalImageWidthHeight(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        List<Integer> result = new ArrayList<>();
        result.add(newOpts.outWidth < 1 ? 240 : newOpts.outWidth);
        result.add(newOpts.outHeight < 1 ? 240 : newOpts.outHeight);
        return result;
    }

    public static void fixScreen(Context context, View view) {
        if (!isNormalScreen(context)) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    public static boolean isNormalScreen(Context context) {
        int screenHeight = getScreenHeight(context);
        return !"meizu".equalsIgnoreCase(Build.BRAND) &&
                (screenHeight == 1920 || screenHeight == 1280 || screenHeight > 1920
                        || screenHeight == 800 || screenHeight == 854 || screenHeight == 960);
    }
}
