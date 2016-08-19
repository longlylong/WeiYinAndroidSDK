package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;


/**
 * 订单列表
 * Created by King on 2015/7/2 0002.
 */
public class OrderBean extends BaseResultBean {

    @Expose
    public OrderListBean.Order order;


}
