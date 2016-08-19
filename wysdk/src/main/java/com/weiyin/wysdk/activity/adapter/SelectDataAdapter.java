package com.weiyin.wysdk.activity.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiyin.wysdk.R;
import com.weiyin.wysdk.WYSdk;
import com.weiyin.wysdk.baseadapter.BaseSectionQuickAdapter;
import com.weiyin.wysdk.baseadapter.BaseViewHolder;
import com.weiyin.wysdk.basesdk.interfaces.WYImageLoader;
import com.weiyin.wysdk.model.request.RequestStructDataBean;
import com.weiyin.wysdk.util.ScreenUtil;

import java.util.List;

/**
 * 选择数据的
 * Created by King on 2016/6/17 0017.
 */
public class SelectDataAdapter extends BaseSectionQuickAdapter<SelectDataSection> {

    private int wh;

    public SelectDataAdapter(Context context, List<SelectDataSection> data) {
        super(R.layout.wy_item_select_data, R.layout.wy_item_select_data_head, data);

        wh = ScreenUtil.getScreenWidth(context) / 3;
    }

    @Override
    protected void convertHead(final BaseViewHolder helper, final SelectDataSection item) {

        helper.setText(R.id.item_select_data_head_title_text, SelectDataSection.DEFAULT_CHAPTER_NAME.equals(item.header) ? "" : item.header);

        final TextView selectAll = helper.getView(R.id.item_select_data_head_select_text);

        selectAll.setVisibility(View.VISIBLE);
        if (item.isHeadSelectAll) {
            selectAll.setText("取消");
        } else {
            selectAll.setText("全选");
        }
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isHeadSelectAll = !item.isHeadSelectAll;
                for (int x = 0; x < mData.size(); x++) {
                    SelectDataSection section = getData().get(x);
                    if (!section.isHeader && item.header.equals(section.header)) {
                        section.block.isSelected = item.isHeadSelectAll;
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectDataSection section) {
        ViewGroup.LayoutParams layoutParams = helper.convertView.getLayoutParams();
        layoutParams.height = wh;
        layoutParams.width = wh;

        RequestStructDataBean.Block item = section.block;

        ImageView img = helper.getView(R.id.item_select_data_img);
        View hasTextIcon = helper.getView(R.id.item_select_data_image_text);

        TextView onlyText = helper.getView(R.id.item_select_data_only_text);

        View selectBg = helper.getView(R.id.item_select_data_selected_bg);

        if (item.isSelected) {
            selectBg.setVisibility(View.VISIBLE);
        } else {
            selectBg.setVisibility(View.GONE);
        }

        if (item.blockType == RequestStructDataBean.TYPE_TEXT) {
            img.setVisibility(View.GONE);
            hasTextIcon.setVisibility(View.GONE);
            onlyText.setVisibility(View.VISIBLE);
            onlyText.setText(item.text);

        } else if (item.blockType == RequestStructDataBean.TYPE_PHOTO) {

            if (showWarning(item.resource.width, item.resource.height)) {
                helper.setVisible(R.id.item_select_data_waring, true);
            } else {
                helper.setVisible(R.id.item_select_data_waring, false);
            }

            img.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(item.resource.desc)) {
                hasTextIcon.setVisibility(View.GONE);
            } else {
                hasTextIcon.setVisibility(View.VISIBLE);

            }
            onlyText.setVisibility(View.GONE);

            WYImageLoader wyImageLoader = WYSdk.getInstance().getWyImageLoader();
            if (wyImageLoader != null) {
                wyImageLoader.displayImage(img, item.resource.url, item.resource.lowPixelUrl, item.resource.width, item.resource.height);
            }
        }
    }

    private boolean showWarning(int width, int height) {
        float scale = Math.max(width * 1.0f, height) / Math.min(width, height);
        return width < 600 || height < 600
                || scale > 2;
    }
}