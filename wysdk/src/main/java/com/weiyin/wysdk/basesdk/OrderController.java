package com.weiyin.wysdk.basesdk;

import com.weiyin.wysdk.http.HttpStore;
import com.weiyin.wysdk.model.BaseResultBean;
import com.weiyin.wysdk.model.request.odrer.RequestActivateCouponBean;
import com.weiyin.wysdk.model.request.odrer.RequestAddShopCartBean;
import com.weiyin.wysdk.model.request.odrer.RequestCouponBean;
import com.weiyin.wysdk.model.request.odrer.RequestCreateOrderBean;
import com.weiyin.wysdk.model.request.odrer.RequestDelOrderBean;
import com.weiyin.wysdk.model.request.odrer.RequestDelShopCartBean;
import com.weiyin.wysdk.model.request.odrer.RequestOrderListBean;
import com.weiyin.wysdk.model.request.odrer.RequestPayBean;
import com.weiyin.wysdk.model.request.odrer.RequestShopCartListBean;
import com.weiyin.wysdk.model.result.CouponActivatedBean;
import com.weiyin.wysdk.model.result.CouponBean;
import com.weiyin.wysdk.model.result.OrderListBean;
import com.weiyin.wysdk.model.result.PayBean;
import com.weiyin.wysdk.model.result.ShopCartListBean;

/**
 * sdk
 * Created by King on 2016/3/28 0028.
 */
public class OrderController extends BaseSdk {
    private HttpStore mHttpStore;

    private static class SingleHelper {
        public static OrderController instance = new OrderController();
    }

    private OrderController() {
        mHttpStore = new HttpStore();
    }

    public static OrderController getInstance() {
        return SingleHelper.instance;
    }

    public void getCoupon(final WYListener<CouponBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                final CouponBean resultBean = mHttpStore.getTickets(new RequestCouponBean());
                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void activateCoupon(final String couponCode, final WYListener<CouponActivatedBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestActivateCouponBean requestBean = new RequestActivateCouponBean();
                requestBean.code = couponCode;
                final CouponActivatedBean activatedBean = mHttpStore.activateTicket(requestBean);
                handleResult(activatedBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, activatedBean);
                    }

                    @Override
                    public void fail() {

                    }
                });

            }
        });
    }

    /**
     * @param paymentPattern ZhifubaoApp = 1, WeixinApp = 2
     */
    public void payOrder(final String orderSerial, final int paymentPattern, final WYListener<PayBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);
        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestPayBean requestBean = new RequestPayBean();
                requestBean.orderSerial = orderSerial;
                requestBean.paymentPattern = paymentPattern;
                final PayBean resultBean = mHttpStore.payOrder(requestBean);
                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void createOrder(final String receiver, final String mobile, final String buyerMobile, final int paymentPattern,
                            final String buyerMark, final String province, final String city, final String area, final String address, final int logistics,
                            final String ticket, final ShopCartListBean shopCartListBean, final WYListener<PayBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestCreateOrderBean requestBean = new RequestCreateOrderBean();
                requestBean.cars = shopCartListBean.cars;
                requestBean.receiver = receiver;
                requestBean.mobile = mobile;
                requestBean.buyerMobile = buyerMobile;
                requestBean.paymentPattern = paymentPattern;
                requestBean.buyerMark = buyerMark;
                requestBean.province = province;
                requestBean.city = city;
                requestBean.area = area;
                requestBean.address = address;
                requestBean.logistics = logistics;
                requestBean.ticket = ticket;
                final PayBean resultBean = mHttpStore.createOrder(requestBean);

                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void addShopCart(final int bookId, final int count, final int workmanship, WYListener<BaseResultBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestAddShopCartBean requestBean = new RequestAddShopCartBean();
                requestBean.bookid = bookId;
                requestBean.count = count;
                requestBean.bookMakeType = workmanship;

                final BaseResultBean resultBean = mHttpStore.addShopCart(requestBean);
                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void delShopCart(final int cartId, final WYListener<BaseResultBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestDelShopCartBean requestBean = new RequestDelShopCartBean();
                requestBean.carId = cartId;
                final BaseResultBean resultBean = mHttpStore.delShopCart(requestBean);

                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void delOrder(final String orderSerial, final WYListener<BaseResultBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestDelOrderBean requestBean = new RequestDelOrderBean();
                requestBean.orderSerial = orderSerial;
                final BaseResultBean resultBean = mHttpStore.delOrder(requestBean);

                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void getOrderList(final WYListener<OrderListBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);
        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestOrderListBean requestBean = new RequestOrderListBean();
                final OrderListBean resultBean = mHttpStore.getOrders(requestBean);

                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void getShopCartList(WYListener<ShopCartListBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                final BaseResultBean resultBean = mHttpStore.getShopCart(new RequestShopCartListBean());
                handleResult(resultBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, resultBean);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

}
