package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

/**
 * 用户信息的返回
 * Created by King on 2016/3/28 0028.
 */
public class UserInfoBean extends BaseResultBean {

    @Expose
    public String identity;

    @Expose
    public String host;
    
    @Expose
    public String guid;

    @Expose
    public String token;

    @Expose
    public String timestamp;

    @Expose
    public int client;


}
