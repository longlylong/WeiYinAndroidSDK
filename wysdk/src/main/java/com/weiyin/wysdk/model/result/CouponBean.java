package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠卷
 * Created by King on 2015/8/7 0007.
 */
public class CouponBean extends BaseResultBean implements Serializable {

    @Expose
    public List<Ticket> tickets;

    public static class Ticket implements Serializable {
        @Expose
        public String code;// --微印券号
        @Expose
        public long createTime;// 创建时间
        @Expose
        public long deadline;//到期时间
        @Expose
        public boolean isCheck; //--是否使用
        @Expose
        public long checkTime;//使用时间
        @Expose
        public String name;//名字
        @Expose
        public float leastPrice;//满多少可减
        @Expose
        public float cutPrice;//减多少元
        @Expose
        public float discount;//折扣 （折扣为0使用cutprice）
        @Expose
        public int useCountType;//1一次性的，2就多次
        @Expose
        public String mark;// --备注

    }


}
