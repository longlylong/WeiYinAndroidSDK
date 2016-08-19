package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

/**
 * 在订单面页支付的
 * Created by King on 2015/7/3 0003.
 */
public class PayBean extends BaseResultBean {

    @Expose
    public String charge;

    @Expose
    public boolean isPay;

    @Expose
    public String orderSerial;

    @Expose
    public float price;

    @Expose
    public String randomKey;
}
