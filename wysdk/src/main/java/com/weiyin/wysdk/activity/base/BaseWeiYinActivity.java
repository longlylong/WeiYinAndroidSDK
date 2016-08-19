package com.weiyin.wysdk.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.weiyin.wysdk.util.ToastUtils;

/**
 * auth @long
 * Created by long on 14-12-28.
 */
public abstract class BaseWeiYinActivity extends AppCompatActivity {

    protected Activity mContext;

    public static void launch(Context context, Class<? extends BaseWeiYinActivity> clazz) {
        Intent intent = getIntent(context, clazz);
        context.startActivity(intent);
    }

    protected static Intent getIntent(Context context, Class<? extends BaseWeiYinActivity> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public abstract void initIntentData();

    public abstract int getContentViewId();

    public abstract void initUI();

    public abstract void initData();

    public abstract void initListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
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

    public void showToast(String title) {
        ToastUtils.show(this, title);
    }
}
