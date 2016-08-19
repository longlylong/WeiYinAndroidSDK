package com.weiyin.wysdk.basesdk.interfaces;

/**
 * 支付状态改变的
 * Created by King on 2016/6/30 0030.
 */
public interface WYWebViewListener {

    void refreshOrder();

    void payResult(String result);

}
