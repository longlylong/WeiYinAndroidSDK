package com.weiyin.wysdk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pingplusplus.android.PaymentActivity;
import com.weiyin.wysdk.R;
import com.weiyin.wysdk.WYSdk;
import com.weiyin.wysdk.activity.base.BaseWeiYinActivity;
import com.weiyin.wysdk.basesdk.OrderController;
import com.weiyin.wysdk.basesdk.WYListener;
import com.weiyin.wysdk.basesdk.interfaces.WYWebViewListener;
import com.weiyin.wysdk.exception.ClientException;
import com.weiyin.wysdk.http.HttpConstant;
import com.weiyin.wysdk.model.BaseResultBean;
import com.weiyin.wysdk.model.result.CouponActivatedBean;
import com.weiyin.wysdk.model.result.CouponBean;
import com.weiyin.wysdk.model.result.OrderListBean;
import com.weiyin.wysdk.model.result.PayBean;
import com.weiyin.wysdk.model.result.ShopCartListBean;
import com.weiyin.wysdk.util.ActionUtil;
import com.weiyin.wysdk.util.GsonUtils;
import com.weiyin.wysdk.util.thread.MainThreadExecutor;


/**
 * 这里是webview的共用类.
 *
 * @author long
 */
public class WYWebViewActivity extends BaseWeiYinActivity {

    public static final int REQUEST_CODE_PAYMENT = 10001;

    private WebView mWebView;
    private boolean isLandscape;
    private String mUrl;
    private ProgressDialog mLoadingDialog;

    public static void launch(Context context, String url, boolean isLandscape) {
        Intent intent = new Intent(context, WYWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isLandscape", isLandscape);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化传过来的intent
        initIntentData();
        //设置显示的内容
        setContentView(getContentViewId());
        //设置界面的
        initUI();
        //设置数据的
        initData();
        //设置监听的
        initListener();
    }

    public void initIntentData() {
        mUrl = getIntent().getStringExtra("url");
        isLandscape = getIntent().getBooleanExtra("isLandscape", false);
    }

    public int getContentViewId() {
        return R.layout.wy_activity_public_webview;
    }

    public void initUI() {
        if (isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        mWebView = (WebView) findViewById(R.id.wy_public_web_view);
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        if (isLandscape) {
            mLoadingDialog.setMessage("正在生成画册预览");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initData() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.addJavascriptInterface(new AndroidClient(), "app");

        webSettings.setDisplayZoomControls(false);

        mWebView.setWebChromeClient(new WebChromeClient() {
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                ActionUtil.openBrowser(mContext, url);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
                if (!isFinishing()) {
                    mLoadingDialog.setMessage("加载中");
                    mLoadingDialog.show();
                }
                if (mWebView != null) {
                    mWebView.setVisibility(View.INVISIBLE);
                }

                if (url.contains("/book/") || url.contains("/photo/") || url.contains("/calendar/")||url.contains("/card/")) {
                    orientation(0);
                } else {
                    orientation(1);
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (!isFinishing()) {
                    closeDialog(mLoadingDialog);
                }
                if (mWebView != null) {
                    mWebView.setVisibility(View.VISIBLE);
                }

                if (HttpConstant.getShowCartUrl().equals(url)) {
                    new AndroidClient().getShopCartList();
                } else if (HttpConstant.getShowOrderUrl().equals(url)) {
                    new AndroidClient().getOrders();
                }
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                if (!isFinishing()) {
                    closeDialog(mLoadingDialog);
                }
                if (mWebView != null) {
                    mWebView.setVisibility(View.VISIBLE);
                }
            }
        });

        mWebView.loadUrl(mUrl);

    }

    public void initListener() {
        WYSdk.getInstance().setWyWebViewListener(new WYWebViewListener() {
            @Override
            public void refreshOrder() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AndroidClient().getOrders();
                    }
                });
            }

            @Override
            public void payResult(String result) {
                loadJSFunc("showPayResult", result);
            }
        });
    }

    private void closeDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void orientation(int orientation) {
        if (orientation == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public class AndroidClient {
        @JavascriptInterface
        public void closeWebView() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    String url = mWebView.getUrl();
//                    if (!TextUtils.isEmpty(url) && url.contains("book")) {
                    finish();
//                    } else {
//                        if (mWebView.canGoBack()) {
//                            mWebView.goBack();
//                        } else {
//                            finish();
//                        }
//                    }
                }
            });
        }

        @JavascriptInterface
        public void getChannel() {
            loadJSFunc("showWebChannel", WYSdk.getInstance().getChannel() + "");
        }

        @JavascriptInterface
        public void loading() {
            MainThreadExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing() && !mLoadingDialog.isShowing()) {
                        mLoadingDialog.setMessage("加载中");
                        mLoadingDialog.show();
                    }
                }
            });
        }

        @JavascriptInterface
        public void copyToClipboard(String text) {
            ActionUtil.copyText(mContext, text);
        }

        @JavascriptInterface
        public void changeOrientation(int orientation) {
            orientation(orientation);
        }

        @JavascriptInterface
        public void delOrder(String orderSerial) {
            OrderController.getInstance().delOrder(orderSerial, new WYListener<BaseResultBean>() {
                @Override
                public void onStart() {
                    mLoadingDialog.setMessage("删除订单中");
                    mLoadingDialog.show();
                }

                @Override
                public void onSuccess(BaseResultBean result) {
                    closeDialog(mLoadingDialog);
                    loadJSFunc("delSuccess");
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                }
            });
        }

        @JavascriptInterface
        public void delShopCart(int cardId) {
            OrderController.getInstance().delShopCart(cardId, new WYListener<BaseResultBean>() {
                @Override
                public void onStart() {
                    mLoadingDialog.setMessage("删除购物车中");
                    mLoadingDialog.show();
                }

                @Override
                public void onSuccess(BaseResultBean result) {
                    closeDialog(mLoadingDialog);
                    loadJSFunc("delSuccess");
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                }
            });
        }

        @JavascriptInterface
        public void payOrder(String orderSerial, int paymentPattern) {
            OrderController.getInstance().payOrder(orderSerial, paymentPattern, new WYListener<PayBean>() {
                @Override
                public void onStart() {
                    mLoadingDialog.setMessage("获取中");
                    mLoadingDialog.show();
                }

                @Override
                public void onSuccess(PayBean result) {
                    closeDialog(mLoadingDialog);
                    if (WYSdk.getInstance().isMyAppPay()) {
                        if (WYSdk.getInstance().getWyPayOrderListener() != null) {
                            WYSdk.getInstance().getWyPayOrderListener().pay(mContext, result.orderSerial, result.price, result.randomKey);
                        }
                    } else {
                        pay(mContext, result.charge);
                    }
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                    showToast("支付订单异常! " + msg);
                }
            });
        }

        @JavascriptInterface
        public void activateCoupon(String code) {
            OrderController.getInstance().activateCoupon(code, new WYListener<CouponActivatedBean>() {
                @Override
                public void onStart() {
                    mLoadingDialog.setMessage("激活优惠券中");
                    mLoadingDialog.show();
                }

                @Override
                public void onSuccess(final CouponActivatedBean result) {
                    closeDialog(mLoadingDialog);
                    changeOrientation(1);
                    getCoupons();
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                }
            });
        }

        @JavascriptInterface
        public void getShopCartList() {
            OrderController.getInstance().getShopCartList(new WYListener<ShopCartListBean>() {
                @Override
                public void onSuccess(final ShopCartListBean result) {
                    closeDialog(mLoadingDialog);
                    try {
                        String json = GsonUtils.getInstance().parse(result);
                        loadJSFunc("showWebShopCart", json);
                    } catch (ClientException e) {
                        showToast("开打购物车解析异常! ");
                    }
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                }
            });
        }

        @JavascriptInterface
        public void getCoupons() {
            OrderController.getInstance().getCoupon(new WYListener<CouponBean>() {
                @Override
                public void onStart() {
                    mLoadingDialog.setMessage("获取优惠券中");
                    mLoadingDialog.show();
                }

                @Override
                public void onSuccess(final CouponBean result) {
                    closeDialog(mLoadingDialog);
                    changeOrientation(1);
                    try {
                        String json = GsonUtils.getInstance().parse(result);
                        loadJSFunc("showWebCoupons", json);
                    } catch (ClientException e) {
                        showToast("打开优惠券解析异常! ");
                    }
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                }
            });
        }

        @JavascriptInterface
        public void getOrders() {
            OrderController.getInstance().getOrderList(new WYListener<OrderListBean>() {
                @Override
                public void onStart() {
                    mLoadingDialog.setMessage("获取订单中");
                    mLoadingDialog.show();
                }

                @Override
                public void onSuccess(final OrderListBean result) {
                    closeDialog(mLoadingDialog);
                    changeOrientation(1);
                    try {
                        String json = GsonUtils.getInstance().parse(result);
                        loadJSFunc("showWebOrders", json);
                    } catch (ClientException e) {
                        showToast("打开订单解析异常! ");
                    }
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                }
            });
        }

        @JavascriptInterface
        public void createOrder(String receiver, String mobile, String buyerMobile, int paymentPattern, String buyerMark,
                                String province, String city, String area, String address, int logistics, String ticket, String shopCartListJson) {
            try {
                ShopCartListBean shopCartListBean = GsonUtils.getInstance().parse(ShopCartListBean.class, shopCartListJson);
                OrderController.getInstance().createOrder(receiver, mobile, buyerMobile, paymentPattern, buyerMark, province, city, area, address, logistics, ticket, shopCartListBean, new WYListener<PayBean>() {
                    @Override
                    public void onStart() {
                        mLoadingDialog.setMessage("创建订单中");
                        mLoadingDialog.show();
                    }

                    @Override
                    public void onSuccess(PayBean result) {
                        closeDialog(mLoadingDialog);
                        if (result.isPay) {
                            if (mWebView != null) {
                                //打开购物车地址
                                mWebView.loadUrl(HttpConstant.getShowOrderUrl());
                            }
                        } else {
                            if (WYSdk.getInstance().isMyAppPay()) {
                                if (WYSdk.getInstance().getWyPayOrderListener() != null) {
                                    WYSdk.getInstance().getWyPayOrderListener().pay(mContext, result.orderSerial, result.price, result.randomKey);
                                }
                            } else {
                                pay(mContext, result.charge);
                            }
                        }
                    }

                    @Override
                    public void onFail(String msg) {
                        closeDialog(mLoadingDialog);
                        showToast("创建订单异常! " + msg);
                    }
                });
            } catch (ClientException ignored) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("创建订单解析异常!请联系小印");
                    }
                });
            }
        }

        @JavascriptInterface
        public void addShopCart2(int bookId, int count, int workmanship) {
            OrderController.getInstance().addShopCart(bookId, count, workmanship, new WYListener<BaseResultBean>() {
                @Override
                public void onStart() {
                    mLoadingDialog.setMessage("加入购物车中");
                    mLoadingDialog.show();
                }

                @Override
                public void onSuccess(BaseResultBean result) {
                    if (mWebView != null) {
                        //打开购物车地址
                        mWebView.loadUrl(HttpConstant.getShowCartUrl());
                    }
                }

                @Override
                public void onFail(String msg) {
                    closeDialog(mLoadingDialog);
                }
            });
        }

        @JavascriptInterface
        public void saveAddress(String addressInfo) {
            getSP().edit().putString(WYSdk.getInstance().getIdentity(), addressInfo).apply();
        }

        @JavascriptInterface
        public void getAddress() {
            String addressInfo = getSP().getString(WYSdk.getInstance().getIdentity(), "");
            loadJSFunc("showWebAddress", addressInfo);
        }

        @JavascriptInterface
        public void getThemeColor() {
            loadJSFunc("showWebThemeColor", WYSdk.getInstance().getThemeColor());
        }

        @JavascriptInterface
        public void goUrl(final String url) {
            if (mWebView != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("shopcart".equals(url)) {
                            mWebView.loadUrl(HttpConstant.getShowCartUrl());
                        } else if ("paper".equals(url)) {
                            mWebView.loadUrl(HttpConstant.getPaperUrl());
                        } else if ("order".equals(url)) {
                            mWebView.loadUrl(HttpConstant.getShowOrderUrl());
                        } else {
                            mWebView.loadUrl(url);
                        }
                    }
                });
            }
        }

        private SharedPreferences getSP() {
            return getApplicationContext().getSharedPreferences("WYSdk", Context.MODE_PRIVATE);
        }
    }

    private void loadJSFunc(final String jsFuncName, final String param) {
        if (mWebView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + jsFuncName + "('" + param + "')");
                }
            });
        }
    }

    private void loadJSFunc(final String jsFuncName) {
        if (mWebView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + jsFuncName + "()");
                }
            });
        }
    }

    public static void pay(Activity activity, String charge) {
        Intent intent = new Intent();
        String packageName = activity.getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        activity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (REQUEST_CODE_PAYMENT == requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                final String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                loadJSFunc("showPayResult", result);
                //打开订单地址
                //mWebView.loadUrl(HttpConstant.getShowOrderUrl());

//              String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//              String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//              ToastUtils.show(mContext, result + errorMsg + extraMsg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        WYSdk.getInstance().setWyWebViewListener(null);
        super.onDestroy();
    }
}
