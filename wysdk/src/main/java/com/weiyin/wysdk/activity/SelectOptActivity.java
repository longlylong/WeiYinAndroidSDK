package com.weiyin.wysdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.weiyin.wysdk.R;
import com.weiyin.wysdk.WYSdk;
import com.weiyin.wysdk.activity.base.BaseDialogAnimActivity;

/**
 * 选择功能的界面咯s
 * Created by King on 2016/7/25 0025.
 */
public class SelectOptActivity extends BaseDialogAnimActivity {

    public static void launchForResult(Activity context, int requestCode) {
        Intent intent = getIntent(context, SelectOptActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    private View mMyOrder;
    private View mProduct;
    private View mShopCart;
    private View mAbout;
    private View mExit;
    private View mAboutQ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView(R.layout.wy_activity_select_opt));

        initView();
        initListener();
    }

    private void initView() {
        mMyOrder = findViewById(R.id.select_opt_my_order);
        mProduct = findViewById(R.id.select_opt_product);
        mShopCart = findViewById(R.id.select_opt_shop_cart);
        mAbout = findViewById(R.id.select_opt_about);
        mAboutQ = findViewById(R.id.select_opt_about_q);
        mExit = findViewById(R.id.select_opt_exit);
    }

    private void initListener() {
        mProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WYSdk.getInstance().showProductList(mContext);
                setResult(RESULT_OK);
                finish();
            }
        });

        mMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WYSdk.getInstance().showOrderList(mContext);
                setResult(RESULT_OK);
                finish();
            }
        });

        mShopCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WYSdk.getInstance().showShopCart(mContext);
                setResult(RESULT_OK);
                finish();
            }
        });

        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WYSdk.getInstance().showPaper(mContext);
                setResult(RESULT_OK);
                finish();
            }
        });

        mAboutQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WYSdk.getInstance().showQuestion(mContext);
                setResult(RESULT_OK);
                finish();
            }
        });

        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
