package com.weiyin.wysdk.model;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.WYSdk;

/**
 * 请求业务的基类bean 用来转json的
 */
public class BaseRequestBean {

    public BaseRequestBean() {
        this.identity = WYSdk.getInstance().getIdentity();
    }

    @Expose
    public String identity;//用户标识

}