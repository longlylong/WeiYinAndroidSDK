package com.weiyin.wysdk.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiyin.wysdk.R;


/**
 * actionbar
 * Created by King on 2015/6/18 0018.
 */
public class ActionBarView extends LinearLayout {

    private ImageView mLeftIcon;
    private View mRightIcon;
    private View mRightText;
    private TextView mTitle;

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.wy_view_actionbar, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        mLeftIcon = (ImageView) findViewById(R.id.action_bar_back_icon);
        mRightIcon = findViewById(R.id.action_bar_right_icon);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mRightText = findViewById(R.id.action_bar_right_text);
    }

    public void setLeftIconRes(int res) {
        mLeftIcon.setImageResource(res);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setLeftIconClickListener(@NonNull OnClickListener listener) {
        mLeftIcon.setOnClickListener(listener);
    }

    public void setRightIconClickListener(@NonNull OnClickListener listener) {
        mRightText.setVisibility(View.GONE);
        mRightIcon.setVisibility(View.VISIBLE);
        mRightIcon.setOnClickListener(listener);
    }

    public void setRightTextClickListener(@NonNull OnClickListener listener) {
        mRightIcon.setVisibility(View.GONE);
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setOnClickListener(listener);
    }
}
