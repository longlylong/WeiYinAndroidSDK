package com.weiyin.wysdk.activity.adapter;

import com.weiyin.wysdk.baseadapter.entity.SectionEntity;
import com.weiyin.wysdk.model.request.RequestStructDataBean;

/**
 * 选择数据的
 * Created by King on 2016/6/17 0017.
 */
public class SelectDataSection extends SectionEntity {

    public static final String DEFAULT_CHAPTER_NAME = "天盖地虎,啊盖啊盖";

    public RequestStructDataBean.Block block;

    public boolean isHeadSelectAll;
}
