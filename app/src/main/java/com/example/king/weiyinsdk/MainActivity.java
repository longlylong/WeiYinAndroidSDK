package com.example.king.weiyinsdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.king.weiyinsdk.util.ImageLoaderWrapper;
import com.weiyin.wysdk.WYSdk;
import com.weiyin.wysdk.basesdk.WYListener;
import com.weiyin.wysdk.basesdk.interfaces.WYImageLoader;
import com.weiyin.wysdk.basesdk.interfaces.WYLoadMoreListener;
import com.weiyin.wysdk.basesdk.interfaces.WYPayOrderListener;
import com.weiyin.wysdk.model.request.RequestStructDataBean;
import com.weiyin.wysdk.util.SpUtils;
import com.weiyin.wysdk.util.StatusBarUtil;
import com.weiyin.wysdk.util.thread.MainThreadExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        ImageLoaderWrapper.initDefault(this, ImageLoaderWrapper.getTmpDir(), false);

        //openid 每个合作方的每个用户唯一标识 建议写法 前缀+唯一标识 如 WY_xxxxxxx
        String openId = SpUtils.getUniqueId(this);
        if (TextUtils.isEmpty(openId)) {
            openId = "wy_sdk_demo_" + UUID.randomUUID().toString();
            SpUtils.saveUniqueId(this, openId);
        }
        WYSdk.getInstance().setSdk(this, "52HJR62BDS6SDD21", "VlYmY2ZjBmOWFmZTJlZTk3NzdhN2M0ODM0MjE3", openId);

        //编辑面页设置
        edit();

        //这个是打开订单页,需要的时候可以调用
        //WYSdk.getInstance().showOrderList(this);
        //刷新订单页状态,如果用合作方支付是需要刷新支付状态的
        //WYSdk.getInstance().refreshOrderState();

        // 设置主题颜色 16进制的颜色 如: f56971
        // WYSdk.getInstance().setThemeColor("ff00ff");
    }

    //数据编辑面页 默认是打开的 关闭直接就跳去排版页
    private void edit() {
        //长按可以编辑文本
        WYSdk.getInstance().isShowSelectDataActivity(true);

        ImageLoaderWrapper.DisplayConfig.Builder builder = new ImageLoaderWrapper.DisplayConfig.Builder();
        final ImageLoaderWrapper.DisplayConfig config = builder.build();
        config.stubImageRes = R.drawable.wy_select_item_gray;

        //数据编辑面页 打开就要处理 图片加载的回调 //这个必须处理不然不显示图片的
        WYSdk.getInstance().setWyImageLoader(new WYImageLoader() {
            @Override
            public void displayImage(ImageView imageView, String path, String lowPixelUrl, int width, int height) {
                ImageLoaderWrapper.getDefault().displayImage(path, imageView, config);
            }
        });

        //上啦加载更多 默认关闭的
        //WYSdk.getInstance().openLoadMore(true);
        WYSdk.getInstance().setWyLoadMoreListener(new WYLoadMoreListener() {
            @Override
            public void loadMore() {
                MainThreadExecutor.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        //WYSdk.getInstance().getTextBlock() //创建文本
                        //WYSdk.getInstance().getChapterBlock();//创建章节的

                        String photoUrl1 = "http://img1.3lian.com/2015/w7/90/d/1.jpg";//1289 x 806
                        RequestStructDataBean.Block block = WYSdk.getInstance().getPhotoBlock("图片1", photoUrl1, photoUrl1, System.currentTimeMillis(), 1289, 806);
                        List<RequestStructDataBean.Block> blockList = new ArrayList<>();
                        blockList.add(block);
                        blockList.add(block);
                        blockList.add(block);
                        blockList.add(block);
                        blockList.add(block);
                        blockList.add(block);

                        //这个是必须调的用来关闭loading和设置数据 可传空
                        //当你没更多数据的时候你可以关闭下拉刷新 WYSdk.getInstance().openLoadMore(false);
                        WYSdk.getInstance().addLoadMoreData(blockList);
                    }
                }, 2000);
            }
        });
    }

    //设置是否合作方的app支付,默认是false
    //如果是用微印支付或合作方的pingxx支付不需要设置这些
    private void myAppPay() {
        WYSdk.getInstance().setMyAppPay(true);
        WYSdk.getInstance().setWyPayOrderListener(new WYPayOrderListener() {
            @Override
            public void pay(Activity context, String orderId, float price, String randomStr) {
                //处理支付
                //合作方需要 orderId randomStr 来通知微印服务器
                //支付成功后,合作方调微印的服务器,文档在联调时索取
            }
        });
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);

        RelativeLayout frameLayout = (RelativeLayout) findViewById(R.id.container);
        final FabActions fabLib = new FabActions();

        fabLib.setup(getApplicationContext(), frameLayout);
        fabLib.addItem(R.mipmap.icon_printf_book_big, "照片书", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //每次请求前都要添加好数据,请求成功或失败都会清除数据
                addData();
                postData(WYSdk.Print_Book);
                fabLib.closeOrOpen();

            }
        });

        fabLib.addItem(R.mipmap.icon_printf_book_photo, "照片冲印", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                postData(WYSdk.Print_Photo);
                fabLib.closeOrOpen();
            }
        });

        fabLib.addItem(R.mipmap.icon_printf_book_card, "卡片", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                postData(WYSdk.Print_Card);
                fabLib.closeOrOpen();
            }
        });

        fabLib.addItem(R.mipmap.icon_printf_book_calendar, "台历", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                postData(WYSdk.Print_Calendar);
                fabLib.closeOrOpen();
            }
        });

        fabLib.addMainItem(R.mipmap.icon_detail_printfing);
    }

    private void addData() {
        //图片素材 必须是网络路径 宽高也是必要的
        String frontCoverUrl = "http://image.weiyin.cc/719/185904/6da25ae9-c89c-410a-a437-8924102d1da1@1o_800w";//1334 x 1334
        String flyleafHeadUrl = "http://image.weiyin.cc/719/185904/b6464d78-1ed9-4c4f-8fad-937a614d9893@1o_800w";//461 x 461
        String backCoverUrl = "http://image.weiyin.cc/719/185904/9B104E17-FEB1-40D5-8921-51B515CD0156@1o_800w";//1334 x 1334

        //章节 微印代言人的图片
        String photoUrl1 = "http://image.weiyin.cc/719/185904/f8c4624e-f371-491c-b260-01aabe395685@1o_800w";//1288 x 859
        String photoUrl2 = "http://image.weiyin.cc/719/185904/b970b0fe-79f5-4305-bd1d-6d84768b2c77@1o_800w";//1288 x 859
        String photoUrl3 = "http://image.weiyin.cc/719/185904/8bb5a60a-443b-4c4c-821c-cc821b48efb8@1o_800w";//1288 x 859
        String photoUrl4 = "http://image.weiyin.cc/719/185904/8e427964-961e-44f9-9ded-28d9272e3041@1o_800w";//1288 x 859
        String photoUrl5 = "http://image.weiyin.cc/719/185904/e99a1f5e-20b4-4c5f-a059-3d1fc6940965@1o_800w";//1288 x 859
        String photoUrl6 = "http://image.weiyin.cc/719/185904/36b1bf8f-7a23-4bb6-9a81-7517078ded82@1o_800w";//1288 x 859
        String photoUrl7 = "http://image.weiyin.cc/719/185904/1deda7c7-817f-4995-9975-80b7e9d0075a@1o_800w";//1288 x 859
        String photoUrl8 = "http://image.weiyin.cc/719/185904/a3424258-9ead-4264-99d9-ab38ebec967e@1o_800w";//1288 x 859
        String photoUrl9 = "http://image.weiyin.cc/719/185904/1deda7c7-817f-4995-9975-80b7e9d0075a@1o_800w";//1288 x 859
        String photoUrl10 = "http://image.weiyin.cc/719/185904/14d5fb62-e71d-4768-8481-05083223ac4d@1o_800w";//1288 x 859
        String photoUrl11 = "http://image.weiyin.cc/719/185904/6da25ae9-c89c-410a-a437-8924102d1da1@1o_800w";//1288 x 859

        //章节 画册简介 的图片
        String photoUrl12 = "http://image.weiyin.cc/719/185904/557a28b6-a9fe-4978-a108-6b59ca13db10@1o_800w";//1288 x 859 瀑布流排版
        String photoUrl13 = "http://image.weiyin.cc/719/185904/91cc8c4e-cff9-4ab0-beec-f9279408f059@1o_800w";//1288 x 859 拼图排版
        String photoUrl14 = "http://image.weiyin.cc/719/185904/20D0D6E3-D1EB-439B-ADDC-310DF547BD27@1o_800w";//1288 x 859 通栏排版
        String photoUrl15 = "http://image.weiyin.cc/719/185904/ee8efe5b-071a-4ced-8a2c-4dbc17291481@1o_800w";//1288 x 859 美感纸
        String photoUrl16 = "http://image.weiyin.cc/719/185904/d36b0ad5-bb2d-4267-8e85-d9f8878a4c96@1o_800w";//1288 x 859 美感纸
        String photoUrl17 = "http://image.weiyin.cc/719/185904/f035e4a2-5336-47f7-a823-ac69eae80f19@1o_800w";//1288 x 859 双铜对裱覆触感膜
        String photoUrl18 = "http://image.weiyin.cc/719/185904/ca79d819-6ef4-47fa-9b2b-c4eecd6e6e8f@1o_800w";//1288 x 859 双铜对裱覆触感膜
        String photoUrl19 = "http://image.weiyin.cc/719/185904/3ce2590a-6432-4bfe-ab74-0af8a338a918@1o_800w";//1288 x 859
        String photoUrl20 = "http://image.weiyin.cc/719/185904/e96c26c8-4b50-40bf-9d04-4e16e99171e9@1o_800w";//1288 x 859 对裱装订方式：高档蝴蝶精装，无缝跨页，180度平铺，完美视觉效果
        String photoUrl21 = "http://image.weiyin.cc/719/185904/b8bfb801-2c3d-4a3d-bcef-adc64e2aa961@1o_800w";//1288 x 859 普通精装装订方式：简胶装
        String photoUrl22 = "http://image.weiyin.cc/719/185904/15b4be8d-45a4-4c0e-82ec-1a31350d5764@1o_800w";//1288 x 859
        String photoUrl23 = "http://image.weiyin.cc/719/185904/7831b58e-35dd-4b8e-8ccd-120e9e8e7630@1o_800w";//1288 x 859 内配4个护角
        String photoUrl24 = "http://image.weiyin.cc/719/185904/30f25df8-ee53-465f-ad07-64b4b57a219e@1o_800w";//1288 x 859 内配4个护角
        String photoUrl25 = "http://image.weiyin.cc/719/185904/ee454d38-88d3-4959-8a3d-9afacf62c1e7@1o_800w";//1288 x 859 外有飞机盒包装，防撞防摔，保护书本
        String photoUrl26 = "http://image.weiyin.cc/719/185904/44abdcba-86bc-43e8-a721-40327e2981cd@1o_800w";//1288 x 859 外有飞机盒包装，防撞防摔，保护书本

        //章节 纸质书实物照 的图片
        String photoUrl27 = "http://image.weiyin.cc/719/185904/4e684ef3-d54e-4d9f-9495-307f930dd932@1o_800w";//1288 x 859
        String photoUrl28 = "http://image.weiyin.cc/719/185904/68b88d25-79ed-45c6-9cf9-b3aa3d40d3a3@1o_800w";//1288 x 859
        String photoUrl29 = "http://image.weiyin.cc/719/185904/e70c0905-1150-4732-b5cb-ea75f4fe02f3@1o_800w";//1288 x 859
        String photoUrl30 = "http://image.weiyin.cc/719/185904/7b505f59-f6c7-4c9c-8dba-6a0bcaca47cc@1o_800w";//1288 x 859
        String photoUrl31 = "http://image.weiyin.cc/719/185904/a5109197-e90c-43e9-aadf-f7e42aa8a56b@1o_800w";//1288 x 859
        String photoUrl32 = "http://image.weiyin.cc/719/185904/22f59dbe-3ba1-4ff9-9121-24383de914f1@1o_800w";//1288 x 859
        String photoUrl33 = "http://image.weiyin.cc/719/185904/cb977f37-e0ac-408b-97fe-573eb1f3a5c5@1o_800w";//1288 x 859
        String photoUrl34 = "http://image.weiyin.cc/719/185904/f75eb1d4-3059-4737-92a0-eae1535a0c9e@1o_800w";//1288 x 859
        String photoUrl35 = "http://image.weiyin.cc/719/185904/6b871dd7-e121-4d41-baf8-d024864cc5a4@1o_800w";//1288 x 859
        String photoUrl36 = "http://image.weiyin.cc/719/185904/0ff2356f-e0b5-41b0-a36d-c39a4c546ed8@1o_800w";//1288 x 859
        String photoUrl37 = "http://image.weiyin.cc/719/185904/b3f71a8a-710b-4de8-a304-7f5906dfbf5b@1o_800w";//1288 x 859
        String photoUrl38 = "http://image.weiyin.cc/719/185904/921cc23d-293a-4939-bda1-93c5ee8aa13f@1o_800w";//1288 x 859
        String photoUrl39 = "http://image.weiyin.cc/719/185904/d621c028-9267-47ae-b6b5-477d8d4060ef@1o_800w";//1288 x 859
        String photoUrl40 = "http://image.weiyin.cc/719/185904/ccb33e32-6ce0-4ae8-b7c4-9f637037dc69@1o_800w";//1288 x 859

        //拍摄时间,由于是网络图片就自定义了一个时间
        long originalTime = System.currentTimeMillis() / 1000;

        WYSdk.getInstance().setFrontCover("一本画册看懂微印品质", "", frontCoverUrl, frontCoverUrl, originalTime, 1334, 1334);
        WYSdk.getInstance().setFlyleaf("爱微印", flyleafHeadUrl, flyleafHeadUrl, originalTime, 461, 461);
        WYSdk.getInstance().setPreface("微印，国内领先的智能图文排版引擎提供商。\n" +
                "微印画册APP，一键把手机照片做成书，可选丰富主题搭配，并提供纸质书生产、销售服务\n" +
                "微信服务号爱微印，可以一键将微信朋友圈制作成纸质画册。");

        WYSdk.getInstance().setCopyright("微印", "了解微印");
        WYSdk.getInstance().setBackCover(backCoverUrl, backCoverUrl, originalTime, 1334, 1334);

        WYSdk.getInstance().addChapterBlock("微印代言人", "");
        WYSdk.getInstance().addPhotoBlock("", photoUrl1, photoUrl1, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl2, photoUrl2, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl3, photoUrl3, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl4, photoUrl4, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl5, photoUrl5, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl6, photoUrl6, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl7, photoUrl7, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl8, photoUrl8, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl9, photoUrl9, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl10, photoUrl10, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl11, photoUrl11, originalTime, 1289, 806);

        WYSdk.getInstance().addChapterBlock("画册简介", "");
        WYSdk.getInstance().addPhotoBlock("瀑布流排版", photoUrl12, photoUrl12, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("拼图排版", photoUrl13, photoUrl13, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("通栏排版", photoUrl14, photoUrl14, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("美感纸", photoUrl15, photoUrl15, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("美感纸", photoUrl16, photoUrl16, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("双铜对裱覆触感膜", photoUrl17, photoUrl17, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("双铜对裱覆触感膜", photoUrl18, photoUrl18, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl19, photoUrl19, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("对裱装订方式：高档蝴蝶精装，无缝跨页，180度平铺，完美视觉效果", photoUrl20, photoUrl20, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("普通精装装订方式：简胶装", photoUrl21, photoUrl21, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl22, photoUrl22, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("内配4个护角", photoUrl23, photoUrl23, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("内配4个护角", photoUrl24, photoUrl24, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("外有飞机盒包装，防撞防摔，保护书本", photoUrl25, photoUrl25, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("外有飞机盒包装，防撞防摔，保护书本", photoUrl26, photoUrl26, originalTime, 1289, 806);

        WYSdk.getInstance().addChapterBlock("纸质书实物照", "");
        WYSdk.getInstance().addPhotoBlock("", photoUrl27, photoUrl27, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl28, photoUrl28, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl29, photoUrl29, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl30, photoUrl30, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl31, photoUrl31, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl32, photoUrl32, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl33, photoUrl33, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl34, photoUrl34, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl35, photoUrl35, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl36, photoUrl36, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl37, photoUrl37, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl38, photoUrl38, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl39, photoUrl39, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl40, photoUrl40, originalTime, 1289, 806);


//        WYSdk.getInstance().addTextBlock("对裱精装\n\n" +
//                "封面：高档硬壳封面  双铜纸覆触感膜\n\n" +
//                "内页：双铜纸过触感膜，中间夹0.3mmPVC\n\n" +
//                "对裱装订方式：高档蝴蝶精装，无缝跨页，180度平铺，完美视觉效果\n\n" +
//                "价格：48元起/10页内，100页加1页+2.5元\n\n" +
//                "尺寸：235mm*235mm\n\n" +
//                "印刷： 惠普indigo10000高清顶级印刷");
    }

    private void postData(int bookType) {
        WYSdk.getInstance().postPrintData(this, bookType, new WYListener<Object>() {

            @Override
            public void onStart() {
                progressDialog.setMessage("提交数据中");
                progressDialog.show();
            }

            @Override
            public void onSuccess(Object result) {
                progressDialog.dismiss();
            }

            @Override
            public void onFail(String msg) {
                progressDialog.dismiss();
                //msg错误码
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
