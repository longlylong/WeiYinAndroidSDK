package com.weiyin.wysdk.basesdk.interfaces;

import android.app.Activity;

public interface WYPayOrderListener {

    void pay(Activity context, String orderId, float price, String randomStr);

}