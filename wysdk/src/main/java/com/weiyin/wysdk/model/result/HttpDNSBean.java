package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * http dns
 * Created by King on 2016/8/4 0004.
 */
public class HttpDNSBean {

    @Expose
    public String host;

    @Expose
    public List<String> ips;
    
    @Expose
    public int ttl;
}
