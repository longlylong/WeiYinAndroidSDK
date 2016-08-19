package com.weiyin.wysdk.model.request.odrer;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseRequestBean;

/**
 * 删除购物车
 * Created by King on 2016/4/8 0008.
 */
public class RequestDelShopCartBean extends BaseRequestBean {

    @Expose
    public int carId;

}
