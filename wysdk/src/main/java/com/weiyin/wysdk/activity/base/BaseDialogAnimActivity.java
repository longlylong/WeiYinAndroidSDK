package com.weiyin.wysdk.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.weiyin.wysdk.R;
import com.weiyin.wysdk.util.ScreenUtil;


/**
 * 底进底出的对话框效果
 * Created by King on 2015/5/29 0029.
 */
public class BaseDialogAnimActivity extends AppCompatActivity {

    protected Activity mContext;

    protected static Intent getIntent(Context context, Class<? extends BaseDialogAnimActivity> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public static void anim(Activity context) {
        context.overridePendingTransition(R.anim.wy_popup_enter, R.anim.wy_popup_exit);
    }

    protected View getView(int layoutId) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowWidth = displayMetrics.widthPixels;
        View view = View.inflate(mContext, layoutId, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(windowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ScreenUtil.fixScreen(this, view);
        view.setLayoutParams(params);
        return view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.wy_popup_enter, R.anim.wy_popup_exit);
    }

}
