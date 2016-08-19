package com.weiyin.wysdk.model.request.odrer;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseRequestBean;

/**
 * 请求微印券的接口
 */
public class RequestActivateCouponBean extends BaseRequestBean {

    @Expose
    public String code;
}
