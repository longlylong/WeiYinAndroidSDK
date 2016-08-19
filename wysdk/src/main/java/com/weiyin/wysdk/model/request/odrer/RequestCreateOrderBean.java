package com.weiyin.wysdk.model.request.odrer;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseRequestBean;
import com.weiyin.wysdk.model.result.ShopCartListBean;

import java.util.List;


/**创建订单
 * Created by King on 2015/7/1 0001.
 */
public class RequestCreateOrderBean extends BaseRequestBean {

    @Expose
    public List<ShopCartListBean.Cart> cars;

    @Expose
    public String receiver;//收件人

    @Expose
    public String mobile;//收件人电话

    @Expose
    public String buyerMobile;//下单人电话（可空）

    /**
     * ZhifubaoApp = 1,
     * WeixinApp = 2
     */
    @Expose
    public int paymentPattern;//支付渠道

    @Expose
    public String buyerMark;//订单留言

    @Expose
    public String province;//省

    @Expose
    public String city;// 市

    @Expose
    public String area;//区

    @Expose
    public String address;//详细地址

    /**
     * 圆通快递 = 1,
     * 韵达快递 = 2,
     * 顺丰快递 = 3,
     * 申通快递 = 4,
     * EMS = 5,
     * 中通快递 = 6,
     * 其它 = 7,
     * 顺风空运 = 8
     */
    @Expose
    public int logistics;//快递类型

    @Expose
    public String ticket;//微印券
}
