package com.weiyin.wysdk.model.request.odrer;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseRequestBean;

/**
 * 请求某个订单的的接口
 */
public class RequestOrderBean extends BaseRequestBean {

    @Expose
    public String orderSerial;

    @Expose
    public String randomKey;
}
