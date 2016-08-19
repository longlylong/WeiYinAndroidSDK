package com.weiyin.wysdk.model.request.odrer;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseRequestBean;

/**
 * 支付
 * Created by King on 2016/4/8 0008.
 */
public class RequestPayBean extends BaseRequestBean {

    @Expose
    public String orderSerial;

    @Expose
    public int paymentPattern;


}
