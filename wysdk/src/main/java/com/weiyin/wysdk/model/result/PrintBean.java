package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

/**
 * 添加完数据后要吊起排版页的
 * Created by King on 2016/3/31 0031.
 */
public class PrintBean extends BaseResultBean {

    @Expose
    public String url;

    @Expose
    public int unionId;
}
