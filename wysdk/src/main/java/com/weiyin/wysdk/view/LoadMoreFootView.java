package com.weiyin.wysdk.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiyin.wysdk.R;

/**
 * 加载更多的尾巴
 * Created by King on 2016/8/1 0001.
 */
public class LoadMoreFootView extends LinearLayout {

    private TextView mLoadMoreText;

    public LoadMoreFootView(Context context) {
        super(context);
        View.inflate(context, R.layout.wy_load_more, this);
        init();
    }

    private void init() {
        mLoadMoreText = (TextView) findViewById(R.id.load_more_text);
    }

    public void loading() {
        mLoadMoreText.setText("加载中...");
    }

    public void loadFinnish() {
        mLoadMoreText.setText("点击加载更多");
    }
}
