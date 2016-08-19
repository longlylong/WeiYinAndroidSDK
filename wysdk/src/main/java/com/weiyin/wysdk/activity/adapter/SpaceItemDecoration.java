package com.weiyin.wysdk.activity.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private static final int dp_1 = 2;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.right = dp_1;
            outRect.top = dp_1;
        }
    }
}