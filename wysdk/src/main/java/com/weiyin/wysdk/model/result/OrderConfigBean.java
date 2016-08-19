package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

/**
 * 在订单面页的 48小时发货
 * Created by King on 2015/7/3 0003.
 */
public class OrderConfigBean extends BaseResultBean {

    /**
     * 发货时间  兼容1.6以前的文案
     */
    @Expose
    @Deprecated
    public String deliverytime;

    /**
     * 购物车 等待付款 文案
     */
    @Expose
    public String shoppingcardesc; //"现在支付，预计48小时内（MM月dd日)发货",  //购物车页支付按钮下的提示方案
}
