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

    private static final String Online_Show_Url = "https://app.weiyin.cc/";//接口
    private static final String Test_Show_Url = "https://apptest.weiyin.cc/";//接口


    /**
     * 线上服务器
     */
    private static boolean ONLINE_SERVER = true;

    public static String Root_Api_Url = ONLINE_SERVER ? Online_Api_Url : Test_Api_Url;
    private static String Root_Show_Url = ONLINE_SERVER ? Online_Show_Url : Test_Show_Url;

    public static String getToken() {
        WYSdk sdk = WYSdk.getInstance();
        return "token=" + sdk.token + "&timestamp=" + sdk.timestamp +
                "&guid=" + sdk.guid + "&clientver=" + WYSdk.SDK_VERSION + "&client=" + sdk.getChannel() + "&";
    }

    /**
     * 购物车地址
     */
    public static String getShowCartUrl() {
        return Root_Show_Url + "order/webviewcart?" + getToken();
    }

    /**
     * 订单地址
     */
    public static String getShowOrderUrl() {
        return Root_Show_Url + "order/webvieworder?" + getToken();
    }

    /**
     * 我的作品地址
     */
    public static String getShowProductListUrl() {
        return Root_Show_Url + "book/webviewproduct?" + getToken();
    }

    /**
     * 纸质画册地址
     */
    public static String getPaperUrl() {
        return Root_Show_Url + "home/bookshow?" + getToken();
    }

    /**
     * 纸质画册地址
     */
    public static String getQuestionUrl() {
        return "https://app.weiyin.cc/home/linktowx?" + getToken();
    }
}
