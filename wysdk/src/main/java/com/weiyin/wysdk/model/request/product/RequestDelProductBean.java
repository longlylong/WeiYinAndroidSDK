package com.weiyin.wysdk.model.request.product;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseRequestBean;

/**
 * 删除作品
 * Created by Administrator on 2016/11/21.
 */

public class RequestDelProductBean extends BaseRequestBean {

    @Expose
    public String serial;
}
