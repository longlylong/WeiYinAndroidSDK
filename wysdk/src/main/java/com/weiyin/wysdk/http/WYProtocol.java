package com.weiyin.wysdk.http;

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
import com.weiyin.wysdk.model.result.CouponActivatedBean;
import com.weiyin.wysdk.model.result.CouponBean;
import com.weiyin.wysdk.model.result.OrderBean;
import com.weiyin.wysdk.model.result.OrderListBean;
import com.weiyin.wysdk.model.result.PayBean;
import com.weiyin.wysdk.model.result.PrintBean;
import com.weiyin.wysdk.model.result.ShopCartListBean;
import com.weiyin.wysdk.model.result.UserInfoBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 协议接口
 * Created by King on 2016/3/28 0028.
 */
public interface WYProtocol {

    @POST("User/Info")
    Call<UserInfoBean> getUserInfo(@Body RequestUserInfoBean requestBean,
                                   @Header("Nonce") int nonce,
                                   @Header("Timestamp ") long timestamp,
                                   @Header("Authorization") String signature);

    @POST("Data/StructData")
    Call<PrintBean> postStructData(@Body RequestStructDataBean requestBean,
                                   @Header("Nonce") int nonce,
                                   @Header("Timestamp ") long timestamp,
                                   @Header("Authorization") String signature);

    @POST("Order/AddShoppingCar")
    Call<BaseResultBean> addShopCart(@Body RequestAddShopCartBean requestBean,
                                     @Header("Nonce") int nonce,
                                     @Header("Timestamp ") long timestamp,
                                     @Header("Authorization") String signature);

    @POST("Order/DeleteShoppingCar")
    Call<BaseResultBean> delShopCart(@Body RequestDelShopCartBean requestBean,
                                     @Header("Nonce") int nonce,
                                     @Header("Timestamp ") long timestamp,
                                     @Header("Authorization") String signature);

    @POST("Order/GetShoppingCar")
    Call<ShopCartListBean> getShopCart(@Body RequestShopCartListBean requestBean,
                                       @Header("Nonce") int nonce,
                                       @Header("Timestamp ") long timestamp,
                                       @Header("Authorization") String signature);

    @POST("Order/CreateOrder")
    Call<PayBean> createOrder(@Body RequestCreateOrderBean requestBean,
                              @Header("Nonce") int nonce,
                              @Header("Timestamp ") long timestamp,
                              @Header("Authorization") String signature);

    @POST("Order/GetOrders")
    Call<OrderListBean> getOrders(@Body RequestOrderListBean requestBean,
                                  @Header("Nonce") int nonce,
                                  @Header("Timestamp ") long timestamp,
                                  @Header("Authorization") String signature);

    @POST("Order/PayOrder")
    Call<PayBean> payOrder(@Body RequestPayBean requestBean,
                           @Header("Nonce") int nonce,
                           @Header("Timestamp ") long timestamp,
                           @Header("Authorization") String signature);

    @POST("Order/DeleteOrder")
    Call<BaseResultBean> deleteOrder(@Body RequestDelOrderBean requestBean,
                                     @Header("Nonce") int nonce,
                                     @Header("Timestamp ") long timestamp,
                                     @Header("Authorization") String signature);

    @POST("Order/GetTickets")
    Call<CouponBean> getTickets(@Body RequestCouponBean requestBean,
                                @Header("Nonce") int nonce,
                                @Header("Timestamp ") long timestamp,
                                @Header("Authorization") String signature);

    @POST("Order/ActivateTicket")
    Call<CouponActivatedBean> activateTicket(@Body RequestActivateCouponBean requestBean,
                                             @Header("Nonce") int nonce,
                                             @Header("Timestamp ") long timestamp,
                                             @Header("Authorization") String signature);


    @POST("Order/GetOrder")
    Call<OrderBean> getOrder(@Body RequestOrderBean requestBean,
                             @Header("Nonce") int nonce,
                             @Header("Timestamp ") long timestamp,
                             @Header("Authorization") String signature);
}
