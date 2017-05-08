package com.weiyin.wysdk.http;


import android.util.Log;

import com.weiyin.wysdk.WYSdk;
import com.weiyin.wysdk.model.BaseRequestBean;
import com.weiyin.wysdk.model.BaseResultBean;
import com.weiyin.wysdk.model.request.RequestStructDataBean;
import com.weiyin.wysdk.model.request.RequestUserInfoBean;
import com.weiyin.wysdk.model.request.odrer.RequestActivateCouponBean;
import com.weiyin.wysdk.model.request.odrer.RequestAddShopCartBean;
import com.weiyin.wysdk.model.request.odrer.RequestCouponBean;
import com.weiyin.wysdk.model.request.odrer.RequestCreateOrderBean;
import com.weiyin.wysdk.model.request.odrer.RequestDelOrderBean;
import com.weiyin.wysdk.model.request.odrer.RequestDelShopCartBean;
import com.weiyin.wysdk.model.request.odrer.RequestOrderBean;
import com.weiyin.wysdk.model.request.odrer.RequestOrderListBean;
import com.weiyin.wysdk.model.request.odrer.RequestPayBean;
import com.weiyin.wysdk.model.request.odrer.RequestShopCartListBean;
import com.weiyin.wysdk.model.request.product.RequestDelProductBean;
import com.weiyin.wysdk.model.result.CouponActivatedBean;
import com.weiyin.wysdk.model.result.CouponBean;
import com.weiyin.wysdk.model.result.OrderBean;
import com.weiyin.wysdk.model.result.OrderListBean;
import com.weiyin.wysdk.model.result.PayBean;
import com.weiyin.wysdk.model.result.PrintBean;
import com.weiyin.wysdk.model.result.ProductListBean;
import com.weiyin.wysdk.model.result.ShopCartListBean;
import com.weiyin.wysdk.model.result.UserInfoBean;
import com.weiyin.wysdk.util.HmacSHA1Signature;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpStore {

    private WYProtocol mWYProtocol;

    public HttpStore() {
        mWYProtocol = HttpProtocolFactory.getProtocol(HttpConstant.Root_Api_Url, WYProtocol.class);
    }

    public String getSyn(String url) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
        Call call = client.newCall(builder.build());
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    private int getRandom() {
        return new Random(System.currentTimeMillis()).nextInt(899999) + 100000;
    }

    private String getSignature(int nonce, long timestamp, String url) {
        String data = "POST" + "\n" + nonce + "\n" + timestamp + "\n" + url;
        String sign = new HmacSHA1Signature().signature(WYSdk.getInstance().getAccessSecret(), data).replaceAll("\\n", "");
        return WYSdk.getInstance().getAccessKey() + ":" + sign;
    }

    private <T extends BaseResultBean> T getResult(retrofit2.Call<T> call) throws IOException {
        retrofit2.Response<T> response = call.execute();
        if (response.isSuccessful()) {
            Log.d("112233", "Successful--" + call.request().url().toString());
            return response.body();
        } else {
            Log.d("112233", response.errorBody().string());
            return null;
        }
    }

    public OrderBean getOrder(RequestOrderBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/GetOrder");
            return getResult(mWYProtocol.getOrder(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除作品
     */
    public BaseResultBean delProduct(RequestDelProductBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Book/DeleteProduct");
            return getResult(mWYProtocol.delProduct(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取作品列表
     */
    public ProductListBean getProductList(BaseRequestBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Book/GetProducts");
            return getResult(mWYProtocol.getProductList(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 激活微印券
     */
    public CouponActivatedBean activateTicket(RequestActivateCouponBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/ActivateTicket");
            return getResult(mWYProtocol.activateTicket(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取微印券
     */
    public CouponBean getTickets(RequestCouponBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/GetTickets");
            return getResult(mWYProtocol.getTickets(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除订单
     */
    public BaseResultBean delOrder(RequestDelOrderBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/DeleteOrder");
            return getResult(mWYProtocol.deleteOrder(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 支付订单 并返回支付
     */
    public PayBean payOrder(RequestPayBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/PayOrder");
            return getResult(mWYProtocol.payOrder(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取订单列表
     */
    public OrderListBean getOrders(RequestOrderListBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/GetOrders");
            return getResult(mWYProtocol.getOrders(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建订单 并返回支付
     */
    public PayBean createOrder(RequestCreateOrderBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/CreateOrder");
            return getResult(mWYProtocol.createOrder(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取购物车列表
     */
    public ShopCartListBean getShopCart(RequestShopCartListBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/GetShoppingCar");
            return getResult(mWYProtocol.getShopCart(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增加增加购物车列表
     */
    public BaseResultBean addShopCart(RequestAddShopCartBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/AddShoppingCar");
            return getResult(mWYProtocol.addShopCart(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增加删除
     */
    public BaseResultBean delShopCart(RequestDelShopCartBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Order/DeleteShoppingCar");
            return getResult(mWYProtocol.delShopCart(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取用户的id
     */
    public UserInfoBean getUserInfo(RequestUserInfoBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "User/Info");
            return getResult(mWYProtocol.getUserInfo(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交数据
     */
    public PrintBean postStructData(RequestStructDataBean bean) {
        try {
            int num = getRandom();
            long time = getTimestamp();
            String authorization = getSignature(num, time, "Data/StructData");
            return getResult(mWYProtocol.postStructData(bean, num, time, authorization));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}