package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取作品列表
 * Created by Administrator on 2016/11/21.
 */

public class ProductListBean extends BaseResultBean {

    @Expose
    public List<ProductBean> products;

    public static class ProductBean extends BaseResultBean implements Serializable {

        @Expose
        public int identity;

        @Expose
        public int bookId;

        @Expose
        public int bookType;

        @Expose
        public String frontImage;

        @Expose
        public boolean isForbId;

        @Expose
        public String name;

        @Expose
        public String serial;

        @Expose
        public int unionId;

        @Expose
        public long updateTime;

        @Expose
        public long structUpTime;

        @Expose
        public int pricePageCount;

        @Expose
        public List<ProductAttribute> productAttribute;

        @Expose
        public String typesetCode;

    }

    public static class ProductAttribute implements Serializable {
        @Expose
        public String Key;
        @Expose
        public String Value;
    }

}