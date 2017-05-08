package com.weiyin.wysdk.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.weiyin.wysdk.R;
import com.weiyin.wysdk.WYSdk;
import com.weiyin.wysdk.activity.adapter.SelectDataAdapter;
import com.weiyin.wysdk.activity.adapter.SelectDataSection;
import com.weiyin.wysdk.activity.adapter.SpaceItemDecoration;
import com.weiyin.wysdk.activity.base.BaseWeiYinActivity;
import com.weiyin.wysdk.baseadapter.BaseQuickAdapter;
import com.weiyin.wysdk.basesdk.AlbumHelper;
import com.weiyin.wysdk.basesdk.WYListener;
import com.weiyin.wysdk.model.request.RequestStructDataBean;
import com.weiyin.wysdk.util.SpUtils;
import com.weiyin.wysdk.util.StatusBarUtil;
import com.weiyin.wysdk.util.thread.AsyncExecutor;
import com.weiyin.wysdk.view.ActionBarView;
import com.weiyin.wysdk.view.LoadMoreFootView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 选择数据的面页
 * Created by King on 2016/6/17 0017.
 */
public class SelectDataActivity extends BaseWeiYinActivity {

    private RecyclerView mRecyclerView;
    private SelectDataAdapter mAdapter;
    private ActionBarView mActionBarView;
    private ProgressDialog mProgressDialog;
    private SelectDataSection mLastLongPressSection;
    private StaggeredGridLayoutManager mLayoutManager;

    private View mTipsLayout;
    private View mTipsCancel;

    private int mLastLongPressPos;
    private GestureDetector gestureDetector;
    private boolean isLoadingMore;

    private int mBookType = WYSdk.BookType_Big;
    private int mMakeType = WYSdk.MakeType_Simple;

    private LoadMoreFootView mLoadMoreFootView;

    private static final int REQUEST_EDIT = 1111;
    private static final int REQUEST_SELECT_OPT = 2222;

    private static final String BookType = "BookType";
    private static final String MakeType = "MakeType";

    public static void launch(Context context, int bookType, int makeType) {
        Intent intent = getIntent(context, SelectDataActivity.class);
        intent.putExtra(BookType, bookType);
        intent.putExtra(MakeType, makeType);
        context.startActivity(intent);
    }

    @Override
    public void initIntentData() {
        mBookType = getIntent().getIntExtra(BookType, WYSdk.BookType_Big);
        mMakeType = getIntent().getIntExtra(MakeType, WYSdk.MakeType_Simple);
    }

    @Override
    public int getContentViewId() {
        return R.layout.wy_activity_select_data;
    }

    @Override
    public void initUI() {
        WYSdk.getInstance().setSelectDataPage(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("加载中...");

        mTipsLayout = findViewById(R.id.wy_select_data_tips_layout);
        mTipsCancel = findViewById(R.id.wy_select_data_tips_cancel);

        mActionBarView = (ActionBarView) findViewById(R.id.wy_select_data_action_bar);
        mActionBarView.setLeftIconRes(R.mipmap.wy_actionbar_cancel);
        mActionBarView.setTitle("选择照片");

        mRecyclerView = (RecyclerView) findViewById(R.id.wy_select_data_recycler_view);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setStatusBarColor(this, R.color.wy_bg_white);
            StatusBarUtil.StatusBarLightMode(this);
        }

        if (SpUtils.getTipsFlag(mContext)) {
            mTipsLayout.setVisibility(View.VISIBLE);
            SpUtils.saveTipsFlag(mContext, false);
        } else {
            mTipsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        mAdapter = new SelectDataAdapter(this, getData());

        mLoadMoreFootView = new LoadMoreFootView(this);
        mAdapter.addFooterView(mLoadMoreFootView);
        if (WYSdk.getInstance().isLoadMore()) {
            mLoadMoreFootView.setTextViewVisible(View.VISIBLE);
        } else {
            mLoadMoreFootView.setTextViewVisible(View.INVISIBLE);
        }

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        if (mLoadMoreFootView != null) {
            mLoadMoreFootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WYSdk.getInstance().getLoadMoreListener() != null) {
                        WYSdk.getInstance().getLoadMoreListener().loadMore();
                    }
                    mProgressDialog.show();
                    mLoadMoreFootView.loading();
                    isLoadingMore = true;
                }
            });
        }

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                if (WYSdk.getInstance().isLoadMore() &&
                        getLastVisiblePosition() >= mAdapter.getItemCount() - 1 && distanceY > 100 && !isLoadingMore) {
                    if (WYSdk.getInstance().getLoadMoreListener() != null) {
                        WYSdk.getInstance().getLoadMoreListener().loadMore();
                    }
                    mProgressDialog.show();
                    isLoadingMore = true;
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SelectDataSection section = mAdapter.getData().get(position);
                section.block.isSelected = !section.block.isSelected;
                setTitleCount();
                mAdapter.notifyItemChanged(position);
            }
        });

        mAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                SelectDataSection section = mAdapter.getData().get(position);
                section.block.isSelected = true;
                mAdapter.notifyItemChanged(position);

                mLastLongPressSection = section;
                mLastLongPressPos = position;

                String text = "";
                if (section.block.blockType == RequestStructDataBean.TYPE_TEXT) {
                    text = section.block.text;
                } else if (section.block.blockType == RequestStructDataBean.TYPE_PHOTO) {
                    text = section.block.resource.desc;
                }
                EditTextActivity.launchForResult(mContext, text, REQUEST_EDIT);
                return true;
            }
        });

        mTipsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTipsLayout.setVisibility(View.GONE);
                SpUtils.saveTipsFlag(mContext, false);
            }
        });

        mActionBarView.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectOptActivity.launchForResult(mContext, REQUEST_SELECT_OPT);
            }
        });

        mActionBarView.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("正在生成画册...");
                mProgressDialog.show();

                AsyncExecutor.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        RequestStructDataBean structDataBean = WYSdk.getInstance().getStructDataBean();

                        int selectCount = 0;
                        for (RequestStructDataBean.Block block : structDataBean.structData.dataBlocks) {
                            if (block.isSelected) {
                                selectCount++;
                            }
                        }

                        if (AlbumHelper.checkPhotoCount(selectCount, mBookType, mMakeType)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int[] range = AlbumHelper.photoRange(mBookType, mMakeType);
                                    showToast("照片范围不符合哦,需" + range[0] + "-" + range[1]);
                                    mProgressDialog.setMessage("加载中...");
                                    mProgressDialog.dismiss();
                                }
                            });
                            return;
                        }

                        CopyOnWriteArrayList<RequestStructDataBean.Block> selectedArr = new CopyOnWriteArrayList<>();
                        int size = structDataBean.structData.dataBlocks.size();
                        for (int x = 0; x < size; x++) {
                            RequestStructDataBean.Block block = structDataBean.structData.dataBlocks.get(x);
                            if (block.blockType == RequestStructDataBean.TYPE_CHAPTER && x < size - 2) {
                                RequestStructDataBean.Block nextBlock = structDataBean.structData.dataBlocks.get(x + 1);
                                if (nextBlock.isSelected) {
                                    selectedArr.add(block);
                                }
                            }
                            if (block.isSelected && block.blockType != RequestStructDataBean.TYPE_CHAPTER) {
                                selectedArr.add(block);
                            }

                        }
                        structDataBean.structData.dataBlocks = selectedArr;
                        WYSdk.getInstance().requestPrint(SelectDataActivity.this, mBookType, mMakeType, false, new WYListener<Object>() {
                            @Override
                            public void onFail(String msg) {
                                mProgressDialog.dismiss();
                                showToast(msg);
                            }

                            @Override
                            public void onSuccess(Object result) {
                                mProgressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    public void setTitleCount() {
        int selectCount = 0;
        for (SelectDataSection section : mAdapter.getData()) {
            if (section.block != null && section.block.isSelected) {
                selectCount++;
            }
        }
        if (selectCount == 0) {
            mActionBarView.setTitle("选择照片");
        } else {
            mActionBarView.setTitle("已选择" + selectCount + "张");
        }
    }

    private List<SelectDataSection> getData() {
        RequestStructDataBean structDataBean = WYSdk.getInstance().getStructDataBean();
        List<SelectDataSection> dataSections = new ArrayList<>();

        for (int x = 0; x < structDataBean.structData.dataBlocks.size(); x++) {
            RequestStructDataBean.Block block = structDataBean.structData.dataBlocks.get(x);
            SelectDataSection section = new SelectDataSection();

            if (x == 0) {
                section.isHeader = true;
                if (block.blockType == RequestStructDataBean.TYPE_CHAPTER) {
                    section.header = block.chapter.title;
                    dataSections.add(section);
                } else {
                    section.header = SelectDataSection.DEFAULT_CHAPTER_NAME;
                    dataSections.add(section);

                    SelectDataSection section1 = new SelectDataSection();
                    section1.header = section.header;
                    section1.block = block;
                    dataSections.add(section1);
                }


            } else {
                if (block.blockType == RequestStructDataBean.TYPE_CHAPTER) {
                    section.isHeader = true;
                    section.header = block.chapter.title;
                } else {
                    section.header = dataSections.get(x - 1).header;
                    section.block = block;
                }
                dataSections.add(section);
            }
        }
        return dataSections;
    }

    private int getLastVisiblePosition() {
        int[] positions = mLayoutManager.findLastVisibleItemPositions(null);
        int lastPosition = positions[0];
        for (int p : positions) {
            lastPosition = p > lastPosition ? p : lastPosition;
        }
        return lastPosition;
    }

    public void addLoadMordData(List<RequestStructDataBean.Block> blockList) {
        isLoadingMore = false;
        mProgressDialog.dismiss();
        if (mLoadMoreFootView != null) {
            mLoadMoreFootView.loadFinnish();
        }
        if (blockList != null && blockList.size() != 0) {
            mAdapter.setNewData(getData());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT && data != null && mLastLongPressSection != null) {

                String text = data.getStringExtra(EditTextActivity.EDIT_TEXT);
                if (mLastLongPressSection.block.blockType == RequestStructDataBean.TYPE_TEXT) {
                    mLastLongPressSection.block.text = text;
                } else if (mLastLongPressSection.block.blockType == RequestStructDataBean.TYPE_PHOTO) {
                    mLastLongPressSection.block.resource.desc = text;
                }
                mAdapter.notifyItemChanged(mLastLongPressPos);
            }

            if (requestCode == REQUEST_SELECT_OPT) {
                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        WYSdk.getInstance().setSelectDataPage(null);
        WYSdk.getInstance().initStructData();
        super.onDestroy();
    }
}
