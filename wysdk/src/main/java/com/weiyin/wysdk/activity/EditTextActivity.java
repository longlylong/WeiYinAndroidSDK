package com.weiyin.wysdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.weiyin.wysdk.R;
import com.weiyin.wysdk.activity.base.BaseWeiYinActivity;
import com.weiyin.wysdk.util.ActionUtil;
import com.weiyin.wysdk.view.ActionBarView;

/**
 * 编辑文本的面页
 * Created by King on 2016/6/22 0022.
 */
public class EditTextActivity extends BaseWeiYinActivity {

    public static final String EDIT_TEXT = "EDIT_TEXT";

    private String mText;
    private ActionBarView mActionBarView;
    private EditText mEditText;

    public static void launchForResult(Activity context, String text,int requestCode) {
        Intent intent = getIntent(context, EditTextActivity.class);
        intent.putExtra(EDIT_TEXT, text);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void initIntentData() {
        mText = getIntent().getStringExtra(EDIT_TEXT);
    }

    @Override
    public int getContentViewId() {
        return R.layout.wy_activity_edit_text;
    }

    @Override
    public void initUI() {
        mEditText = (EditText) findViewById(R.id.wy_edit_text_et);
        mActionBarView = (ActionBarView) findViewById(R.id.wy_edit_text_action_bar);
        mActionBarView.setTitle("修改文本");

        mEditText.setText(mText);
        mEditText.setSelection(mEditText.getText().length());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mActionBarView.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mActionBarView.setRightIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionUtil.hideSoftInput(mContext, mEditText);

                String text = mEditText.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra(EDIT_TEXT, text);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
