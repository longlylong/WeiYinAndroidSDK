package com.weiyin.wysdk;

import android.content.Context;
import android.text.TextUtils;

import com.weiyin.wysdk.activity.SelectDataActivity;
import com.weiyin.wysdk.activity.WYWebViewActivity;
import com.weiyin.wysdk.basesdk.AlbumHelper;
import com.weiyin.wysdk.basesdk.BaseSdk;
import com.weiyin.wysdk.basesdk.Controller;
import com.weiyin.wysdk.basesdk.WYListener;
import com.weiyin.wysdk.basesdk.interfaces.WYImageLoader;
import com.weiyin.wysdk.basesdk.interfaces.WYLoadMoreListener;
import com.weiyin.wysdk.basesdk.interfaces.WYPayOrderListener;
import com.weiyin.wysdk.basesdk.interfaces.WYWebViewListener;
import com.weiyin.wysdk.http.HttpConstant;
import com.weiyin.wysdk.model.request.RequestStructDataBean;
import com.weiyin.wysdk.model.request.RequestUserInfoBean;
import com.weiyin.wysdk.model.result.PrintBean;
import com.weiyin.wysdk.model.result.UserInfoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * sdk
 * Created by King on 2016/3/28 0028.
 */
public class WYSdk extends BaseSdk {

    //支付状态
    public static final String PAY_SUCCESS = "success";//  payment succeed
    public static final String PAY_FAIL = "fail";// payment failed
    public static final String PAY_CANCEL = "cancel";// user canceld
    public static final String PAY_INVALID = "invalid";// payment plugin not installed

    public static final String SDK_VERSION = "1.6.0";//sdk版本

    //成品类型需要组合
    /**
     * 大书 卡片 照片冲印 台历 A4书
     */
    public static final int BookType_Big = 0;
    public static final int BookType_Card = 1;
    public static final int BookType_Photo = 3;
    public static final int BookType_Calendar = 4;
    public static final int BookType_A4 = 5;

    public static final int MakeType_Simple = 0;   //简胶
    public static final int MakeType_A4_D = 1;     //A4对裱
    public static final int MakeType_Jing = 2;     //精装
    public static final int MakeType_28P = 4;      //28P对裱影楼册
    public static final int MakeType_28P_B = 6;    //布纹
    public static final int MakeType_28P_M = 5;    //迷你
    public static final int MakeType_28P_M_B = 7;  //布纹迷你

    private RequestStructDataBean structDataBean;

    private WYImageLoader wyImageLoader;
    private WYWebViewListener wyWebViewListener;
    private WYPayOrderListener wyPayOrderListener;
    private WYLoadMoreListener wyLoadMoreListener;

    private SelectDataActivity mSelectDataActivity;

    private boolean isShowSelectDataPage = true;
    private boolean isMyAppPay = false;
    private boolean isLoadMore = false;

    private long lastLoginTime = 0;

    private int channel;
    private String themeColor = "f56971";
    private String accessKey, accessSecret, openId, identity, thirdName, thirdHeadImg;
    public String guid, token, timestamp;

    private WYSdk() {
        initStructData();
    }

    public static WYSdk getInstance() {
        return SingleHelper.instance;
    }

    public void initStructData() {
        structDataBean = new RequestStructDataBean();
        structDataBean.structData = new RequestStructDataBean.StructData();
        structDataBean.structData.dataBlocks = new CopyOnWriteArrayList<>();
    }

    /**
     * 初始化的方法
     *
     * @param accessKey    申请到的AppKey
     * @param accessSecret 申请到的AppSecret
     * @param openId       每个合作方的每个用户唯一标识 建议写法 前缀+唯一标识 如 WY_xxxxxxx
     */
    public void setSdk(Context context, String accessKey, String accessSecret, String openId) {
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
        this.openId = openId;
        requestIdentity(new Controller(null));
    }

    public WYImageLoader getWyImageLoader() {
        return wyImageLoader;
    }

    /**
     * 设置图片加载器
     */
    public void setWyImageLoader(WYImageLoader wyImageLoader) {
        this.wyImageLoader = wyImageLoader;
    }

    /**
     * 设置加载更多的回调
     */
    public void setWyLoadMoreListener(WYLoadMoreListener wyLoadMoreListener) {
        this.wyLoadMoreListener = wyLoadMoreListener;
    }

    /**
     * 设置加载更多的回调
     */
    public WYLoadMoreListener getLoadMoreListener() {
        return wyLoadMoreListener;
    }

    public void setWyWebViewListener(WYWebViewListener wyWebViewListener) {
        this.wyWebViewListener = wyWebViewListener;
    }

    /**
     * 设置是否打开二次选择数据面页
     */
    public void isShowSelectDataActivity(boolean isSelectData) {
        this.isShowSelectDataPage = isSelectData;
    }

    /**
     * 打开加载更多
     */
    public void openLoadMore(boolean load) {
        this.isLoadMore = load;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    /**
     * 设置为合作方的app支付
     */
    public void setMyAppPay(boolean isMyAppPay) {
        this.isMyAppPay = isMyAppPay;
    }

    /**
     * 设置发生支付时候的回调
     */
    public void setWyPayOrderListener(WYPayOrderListener wyPayOrderListener) {
        this.wyPayOrderListener = wyPayOrderListener;
    }

    public void setSelectDataPage(SelectDataActivity selectDataPage) {
        this.mSelectDataActivity = selectDataPage;
    }

    /**
     * 刷新完毕必须调这个,可以传空
     */
    public void addLoadMoreData(List<RequestStructDataBean.Block> blockList) {
        if (mSelectDataActivity != null) {
            if (blockList != null) {
                structDataBean.structData.dataBlocks.addAll(blockList);
            }
            mSelectDataActivity.addLoadMordData(blockList);
        }
    }

    public WYPayOrderListener getWyPayOrderListener() {
        return wyPayOrderListener;
    }

    public boolean isMyAppPay() {
        return isMyAppPay;
    }

    public RequestStructDataBean getStructDataBean() {
        return structDataBean;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    private String getThirdName() {
        return thirdName;
    }

    private String getThirdHeadImg() {
        return thirdHeadImg;
    }

    /**
     * 设置合作方的名字
     */
    public void setThirdName(String name) {
        thirdName = name;
    }

    /**
     * 设置合作方的头像
     */
    public void setThirdHeadImg(String url) {
        thirdHeadImg = url;
    }

    public String getThemeColor() {
        return themeColor;
    }

    /**
     * 设置主题颜色 16进制的颜色 如: f56971
     */
    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getIdentity() {
        return identity;
    }

    public int getChannel() {
        return channel;
    }

    private String getOpenId() {
        return openId;
    }

    private boolean isLogin() {
        return !TextUtils.isEmpty(getIdentity())
                && lastLoginTime + 2 * 60 * 60 * 1000 > System.currentTimeMillis();
    }

    private void requestIdentity(final Controller controller) {
        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                RequestUserInfoBean bean = new RequestUserInfoBean();
                bean.openId = getOpenId();
                bean.name = getThirdName();
                bean.headImg = getThirdHeadImg();
                final UserInfoBean userInfo = mHttpStore.getUserInfo(bean);
                handleResult(userInfo, controller, new HandleResultListener() {
                    @Override
                    public void ok() {

                        lastLoginTime = System.currentTimeMillis();
                        identity = userInfo.identity;
                        guid = userInfo.guid;
                        token = userInfo.token;
                        timestamp = userInfo.timestamp;
                        channel = userInfo.client;
                        callSuccess(controller, userInfo);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    private RequestStructDataBean.Resource fillRes(String url, String lowPixelUrl, long originalTime, int width, int height, String des) {
        RequestStructDataBean.Resource resource = new RequestStructDataBean.Resource();
        resource.desc = des;
        resource.url = url;
        resource.lowPixelUrl = lowPixelUrl;
        resource.originaltime = originalTime;
        resource.width = width;
        resource.height = height;
        return resource;
    }

    /**
     * 设置封面
     *
     * @param title        封面标题
     * @param subTitle     封面副标题(可选)
     * @param url          封面高精图片路径
     * @param lowPixelUrl  封面低精图片路径
     * @param originalTime 封面照片的拍摄时间
     * @param width        封面照片宽
     * @param height       封面照片高
     */
    public void setFrontCover(String title, String subTitle, String url, String lowPixelUrl, long originalTime, int width, int height) {
        RequestStructDataBean.Cover frontCover = new RequestStructDataBean.Cover();
        frontCover.title = title;
        frontCover.subTitle = subTitle;
        frontCover.coverImgs = new ArrayList<>();
        frontCover.coverImgs.add(fillRes(url, lowPixelUrl, originalTime, width, height, ""));
        structDataBean.structData.cover = frontCover;
    }

    /**
     * 设置封底
     *
     * @param url          封底高精图片路径
     * @param lowPixelUrl  封底低精图片路径
     * @param originalTime 封底照片的拍摄时间
     * @param width        封底照片宽
     * @param height       封底照片高
     */
    public void setBackCover(String url, String lowPixelUrl, long originalTime, int width, int height) {
        RequestStructDataBean.Cover backCover = new RequestStructDataBean.Cover();
        backCover.coverImgs = new ArrayList<>();
        backCover.coverImgs.add(fillRes(url, lowPixelUrl, originalTime, width, height, ""));
        structDataBean.structData.backCover = backCover;
    }

    /**
     * 设置扉页
     *
     * @param nick         扉页昵称
     * @param url          扉页高精图片路径
     * @param lowPixelUrl  扉页低精图片路径
     * @param originalTime 扉页照片的拍摄时间
     * @param width        扉页照片宽
     * @param height       扉页照片高
     */
    public void setFlyleaf(String nick, String url, String lowPixelUrl, long originalTime, int width, int height) {
        RequestStructDataBean.Flyleaf flyleaf = new RequestStructDataBean.Flyleaf();
        flyleaf.nick = nick;
        flyleaf.headImg = fillRes(url, lowPixelUrl, originalTime, width, height, "");
        structDataBean.structData.flyleaf = flyleaf;
    }

    /**
     * 设置序言
     *
     * @param text 序言文本
     */
    public void setPreface(String text) {
        RequestStructDataBean.Preface preface = new RequestStructDataBean.Preface();
        preface.text = text;
        structDataBean.structData.preface = preface;
    }

    /**
     * 设置版权页
     *
     * @param author   版权页作者
     * @param bookName 版权页书名
     */
    public void setCopyright(String author, String bookName) {
        RequestStructDataBean.Copyright copyright = new RequestStructDataBean.Copyright();
        copyright.author = author;
        copyright.bookName = bookName;
        structDataBean.structData.copyright = copyright;
    }

    /**
     * 添加章节页
     *
     * @param title 章节标题
     * @param des   章节描述
     */
    public void addChapterBlock(String title, String des) {
        RequestStructDataBean.Block chapterBlock = getChapterBlock(title, des);
        structDataBean.structData.dataBlocks.add(chapterBlock);
    }

    /**
     * 添加照片页
     *
     * @param desc         照片描述
     * @param url          照片高精路径
     * @param lowPixelUrl  照片低精路径
     * @param originalTime 照片拍摄时间
     * @param width        照片宽
     * @param height       照片高
     */
    public void addPhotoBlock(String desc, String url, String lowPixelUrl, long originalTime, int width, int height) {
        RequestStructDataBean.Block photoBlock = getPhotoBlock(desc, url, lowPixelUrl, originalTime, width, height);
        structDataBean.structData.dataBlocks.add(photoBlock);
    }

    /**
     * 添加文本页
     *
     * @param text 文本
     */
    public void addTextBlock(String text) {
        RequestStructDataBean.Block textBlock = getTextBlock(text);
        structDataBean.structData.dataBlocks.add(textBlock);
    }

    /**
     * 提交数据入口 对照常量
     * 类型bookType makeType
     * {@link WYSdk.BookType_Big,WYSdk.MakeType_Simple,....}
     * 简胶大方书 0 0
     * 对裱影楼册 0 4
     * 迷你影楼册 0 5
     * 布纹影楼册 0 6
     * 迷你布纹册 0 7
     * 卡片 1 0
     * 照片冲印 3 0
     * 台历 4 0
     * 轻杂志 5 0
     * 对裱纪念册 5 1
     */
    public void postPrintData(final Context context, final int bookType, final int makeType, final WYListener<Object> listener) {
        if (structDataBean.structData.cover == null || structDataBean.structData.flyleaf == null || structDataBean.structData.preface == null
                || structDataBean.structData.copyright == null || structDataBean.structData.backCover == null) {
            throw new IllegalArgumentException("data not integrity!!");
        }

        if (AlbumHelper.checkPhotoCount(structDataBean.structData.dataBlocks.size(), bookType, makeType)) {
            int[] range = AlbumHelper.photoRange(bookType, makeType);
            throw new IllegalArgumentException("photos count not match, the range is " + range[0] + "-" + range[1]);
        }

        final Controller controller = new Controller(listener);
        callStart(controller);

        if (isLogin()) {
            if (isShowSelectDataPage) {
                SelectDataActivity.launch(context, bookType, makeType);
                callSuccess(controller, -1);
            } else {
                requestPrint(context, bookType, makeType, true, listener);
            }

        } else {
            final Controller c = new Controller(new WYListener<UserInfoBean>() {
                @Override
                public void onSuccess(UserInfoBean result) {
                    postPrintData(context, bookType, makeType, listener);
                }

                @Override
                public void onFail(String msg) {
                    callFail(controller, msg);
                }
            });
            requestIdentity(c);
        }
    }

    public void requestPrint(final Context context, final int bookType, final int makeType, final boolean failedClear, WYListener<Object> listener) {
        final Controller controller = new Controller(listener);
        runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                structDataBean.identity = getIdentity();
                structDataBean.bookType = bookType;
                structDataBean.bookMakeType = makeType;

                final PrintBean printBean = mHttpStore.postStructData(structDataBean);

                handleResult(printBean, controller, new HandleResultListener() {
                    @Override
                    public void ok() {
                        //清空数据
                        initStructData();

                        WYWebViewActivity.launch(context, printBean.url + "&" + HttpConstant.getToken(), true);
                        callSuccess(controller, 1000);
                    }

                    @Override
                    public void fail() {
                        //清空数据
                        if (failedClear) {
                            initStructData();
                        }
                    }
                });
            }
        });
    }

    /**
     * 打开我的作品面页
     */
    public void showProductList(Context context) {
        WYWebViewActivity.launch(context, HttpConstant.getShowProductListUrl(), false);
    }

    /**
     * 打开订单面页
     */
    public void showOrderList(Context context) {
        WYWebViewActivity.launch(context, HttpConstant.getShowOrderUrl(), false);
    }

    /**
     * 打开购物车面页
     */
    public void showShopCart(Context context) {
        WYWebViewActivity.launch(context, HttpConstant.getShowCartUrl(), false);
    }

    /**
     * 打开纸质画册
     */
    public void showPaper(Context context) {
        WYWebViewActivity.launch(context, HttpConstant.getPaperUrl(), false);
    }

    /**
     * 打开常见问题
     */
    public void showQuestion(Context context) {
        WYWebViewActivity.launch(context, HttpConstant.getQuestionUrl(), false);
    }

    /**
     * 刷新支付结果,用来ui显示的 {@link WYSdk.PAY_SUCCESS,WYSdk.PAY_FAIL,WYSdk.PAY_CANCEL,WYSdk.PAY_INVALID}
     */
    public void refreshPayState(String result) {
        if (wyWebViewListener != null) {
            wyWebViewListener.payResult(result);
        }
    }

    /**
     * 刷新订单状态
     */
    public void refreshOrderState() {
        if (wyWebViewListener != null) {
            wyWebViewListener.refreshOrder();
        }
    }

    public RequestStructDataBean.Block getChapterBlock(String title, String des) {
        RequestStructDataBean.Block chapterBlock = new RequestStructDataBean.Block();
        chapterBlock.chapter = new RequestStructDataBean.Chapter();
        chapterBlock.chapter.desc = des;
        chapterBlock.chapter.title = title;
        chapterBlock.blockType = RequestStructDataBean.TYPE_CHAPTER;
        return chapterBlock;
    }

    public RequestStructDataBean.Block getPhotoBlock(String desc, String url, String lowPixelUrl, long originalTime, int width, int height) {
        RequestStructDataBean.Block photoBlock = new RequestStructDataBean.Block();
        photoBlock.resource = fillRes(url, lowPixelUrl, originalTime, width, height, desc);
        photoBlock.blockType = RequestStructDataBean.TYPE_PHOTO;
        return photoBlock;
    }

    public RequestStructDataBean.Block getTextBlock(String text) {
        RequestStructDataBean.Block textBlock = new RequestStructDataBean.Block();
        textBlock.text = text;
        textBlock.blockType = RequestStructDataBean.TYPE_TEXT;
        return textBlock;
    }

    private static class SingleHelper {
        public static WYSdk instance = new WYSdk();
    }
}
