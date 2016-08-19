package com.weiyin.wysdk.model.result;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.model.BaseResultBean;

import java.util.Comparator;
import java.util.List;

/**
 * 订单列表
 * Created by King on 2015/7/2 0002.
 */
public class OrderListBean extends BaseResultBean {

    @Expose
    public List<Order> orders;

    public static class Order implements Comparator<Order> {

        @Expose
        public String orderSerial;//
        @Expose
        public String address;//
        @Expose
        public String receiver;//
        @Expose
        public String mobile;//
        @Expose
        public String buyerMobile;//
        /**
         * ZhifubaoApp = 1,
         * WeixinApp = 2
         */
        @Expose
        public int paymentPattern;//0,
        @Expose
        public int orderStatus;//0,
        @Expose
        public float totalPrice;//0.0,
        @Expose
        public int quantity;//0,
        @Expose
        public String buyerMark;//
        @Expose
        public String trackingNumber;//
        @Expose
        public long createTime;//"0001-01-01T00:00:00",
        @Expose
        public long payTime;//
        @Expose
        public long deliveryTime;//
        @Expose
        public long generateTime;//
        @Expose
        public long closeTime;//
        @Expose
        public String province;//
        @Expose
        public String city;//
        @Expose
        public String area;//
        @Expose
        public int logistics;//0,
        @Expose
        public String kuaiDiUrl;//
        @Expose
        public float discount;//0.0,
        @Expose
        public float fee;//0.0,
        @Expose
        public int displayType;// //默认订单=0, 历史订单 = 1
        @Expose
        public String desc;//"现在支付，预计48小时内（03月19日）发货", //订单状态描述，只有未支付跟正在制作状态才有
        @Expose
        public List<Detail> details;

        @Override
        public int compare(Order lhs, Order rhs) {
            if (lhs == null || rhs == null) {
                return 1;
            }
            return rhs.orderSerial.compareTo(lhs.orderSerial);
        }
    }

    public static class Detail {
        @Expose
        public int bookId;//6,
        @Expose
        public String bookName;//jingtianlei的画册",
        @Expose
        public int bookType;//
        @Expose
        public String serial;//W6",
        @Expose
        public int page;//16,
        @Expose
        public int count;//1,
        @Expose
        public int volume; //--一本有多少册
        @Expose
        public float price;//0.01--价格
        @Expose
        public String frontImage;//封面
        @Expose
        public int bookMakeType;//制作工艺
    }
}
