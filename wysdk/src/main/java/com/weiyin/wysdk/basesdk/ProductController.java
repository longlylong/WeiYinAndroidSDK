package com.weiyin.wysdk.basesdk;

import com.weiyin.wysdk.model.BaseRequestBean;
import com.weiyin.wysdk.model.BaseResultBean;
import com.weiyin.wysdk.model.request.product.RequestDelProductBean;
import com.weiyin.wysdk.model.result.ProductListBean;

/**
 * 我的作品控制器
 * Created by King on 2017/5/4 0004.
 */

public class ProductController extends BaseSdk {
    private static class SingleHelper {
        public static ProductController instance = new ProductController();
    }

    private ProductController() {
    }

    public static ProductController getInstance() {
        return SingleHelper.instance;
    }

    public void getProductList(final WYListener<ProductListBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                final ProductListBean productList = mHttpStore.getProductList(new BaseRequestBean());
                handleResult(productList, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        callSuccess(controller, productList);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    public void delProduct(final String serial, final WYListener<BaseResultBean> listener) {
        final Controller controller = new Controller(listener);
        callStart(controller);

        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestDelProductBean delProductBean = new RequestDelProductBean();
                delProductBean.serial = serial;
                final BaseResultBean resultBean = mHttpStore.delProduct(delProductBean);
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
