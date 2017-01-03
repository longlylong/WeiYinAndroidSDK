package com.weiyin.wysdk.http;

import com.weiyin.wysdk.WYSdk;

/**
 * 服务器常量
 * Created by King on 2015/4/22 0022.
 */
public class HttpConstant {

    /**
     * 请求成功
     */
    public static final String SUCCESS = "1000";
    /**
     * 签名错误
     */
    public static final String SIGN_ERROR = "1001";

    private static final String Online_Api_Url = "https://openapi.weiyin.cc/";//接口
    private static final String Test_Api_Url = "https://apitest.weiyin.cc/";//接口

    /**
     * 线上服务器
     */
    private static boolean ONLINE_SERVER = true;

    public static String Root_Api_Url = ONLINE_SERVER ? Online_Api_Url : Test_Api_Url;

    /**
     * 购物车地址
     */
    public static String getShowCartUrl() {
        return ONLINE_SERVER ? WYSdk.getInstance().getHost() + "/order/webviewcart" : "https://apptest.weiyin.cc/order/webviewcart";
    }

    /**
     * 订单地址
     */
    public static String getShowOrderUrl() {
        return ONLINE_SERVER ? WYSdk.getInstance().getHost() + "/order/webvieworder" : "https://apptest.weiyin.cc/order/webvieworder";
    }

    /**
     * 纸质画册地址
     */
    public static String getPaperUrl() {
        return ONLINE_SERVER ? WYSdk.getInstance().getHost() + "/home/bookshow" : "https://apptest.weiyin.cc/home/bookshow";
    }

    /**
     * 纸质画册地址
     */
    public static String getQuestionUrl() {
        return "http://app.weiyin.cc/home/linktowx";
    }
}
