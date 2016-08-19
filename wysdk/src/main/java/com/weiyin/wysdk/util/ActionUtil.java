package com.weiyin.wysdk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Pattern;

public class ActionUtil {

    private static final Pattern PHONE_ALL = Pattern
            .compile("1[3-9][0-9]");

    public static void openBrowser(@NonNull Context context, String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } catch (Exception ignored) {
        }
    }

    /**
     * 打电话的了
     */
    public static void callPhone(Context mContext, String num) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + num));
        mContext.startActivity(intent);
    }

    /**
     * 发送短信
     */
    public static void sendSms(Context mContext, String num) {
        Uri uri = Uri.parse("smsto:" + num);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        // intent.putExtra("sms_body", "The SMS text");
        mContext.startActivity(intent);
    }

    public static void copyText(Context context, String text) {
        ClipboardManager mClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboard.setText(text);
    }

    /**
     * 判断双击的 默认555ms
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(555);
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick(int deltaTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < deltaTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断是否手机号码
     */
    public static boolean isPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
            return false;
        }
        String preNum = phoneNum.substring(0, 3);
        return PHONE_ALL.matcher(preNum).find();
    }

    public static void hideSoftInput(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
