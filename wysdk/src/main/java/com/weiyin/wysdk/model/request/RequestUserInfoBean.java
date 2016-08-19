package com.weiyin.wysdk.model.request;

import com.google.gson.annotations.Expose;

/**
 * 请求memberId的
 * Created by King on 2016/3/28 0028.
 */
public class RequestUserInfoBean {
    /**
     * 合作方用户唯一标识
     */
    @Expose
    public String openId;

    /**
     * 合作方用户昵称
     */
    @Expose
    public String name;

    /**
     * 合作方用户头像
     */
    @Expose
    public String headImg;
}
