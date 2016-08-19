package com.weiyin.wysdk.model;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.http.HttpConstant;

/**
 * 请求结果
 * Created by King on 2016/3/28 0028.
 */
public class BaseResultBean {

    @Expose
    public String resultCode;

    @Expose
    public String errorMsg;

    public boolean ok() {
        return HttpConstant.SUCCESS.equals(resultCode);
    }
}
