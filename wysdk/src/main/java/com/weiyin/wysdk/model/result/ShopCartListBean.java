package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * 购物车的类
 * Created by King on 2015/7/2 0002.
 */
public class ShopCartListBean extends BaseResultBean implements Serializable {

    @Expose
    public List<Cart> cars;

    public static class Cart implements Serializable, Comparator<Cart> {

        @Expose
        public int bookId;
        @Expose
        public int count;
        @Expose
        public int carId;
        @Expose
        public String bookName;
        @Expose
        public int pricePageCount;
        @Expose
        public float price;

        @Expose
        public int volume;

        @Expose
        public String frontImage;//封面

        @Expose
        public int bookMakeType;//制作工艺

        @Override
        public int compare(Cart lhs, Cart rhs) {
            if (lhs == null || rhs == null) {
                return 1;
            }
            return rhs.carId - lhs.carId;
        }
    }


}
