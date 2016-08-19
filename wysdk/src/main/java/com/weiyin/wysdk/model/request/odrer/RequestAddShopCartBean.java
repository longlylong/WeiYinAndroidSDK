package com.weiyin.wysdk.model.request.odrer;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseRequestBean;

public class RequestAddShopCartBean extends BaseRequestBean {

    @Expose
    public int bookid;

    @Expose
    public int count;

    @Expose
    public int bookMakeType;
}
