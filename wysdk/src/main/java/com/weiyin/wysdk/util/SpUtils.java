package com.weiyin.wysdk.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sp
 * Created by King on 2016/9/27 0027.
 */

public class SpUtils {

    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static boolean getTipsFlag(Context context) {
        return getSp(context).getBoolean("TipsFlag", true);
    }

    public static void saveTipsFlag(Context context, boolean flag) {
        getSp(context).edit().putBoolean("TipsFlag", flag).apply();
    }

    public static String getUniqueId(Context context) {
        return getSp(context).getString("UniqueId", "");
    }

    public static void saveUniqueId(Context context, String uuid) {
        getSp(context).edit().putString("UniqueId", uuid).apply();
    }
}
