package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

import java.io.Serializable;

/**
 * 优惠卷
 * Created by King on 2015/8/7 0007.
 */
public class CouponActivatedBean extends BaseResultBean implements Serializable {

    @Expose
    public CouponBean.Ticket ticket;


}
