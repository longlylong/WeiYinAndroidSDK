package com.example.king.weiyinsdk;

import android.app.Activity;
import android.app.ProgressDialog;
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
        String frontCoverUrl = "http://image.weiyin.cc/719/185904/FD3DB404-B318-434A-9DE2-72FF157B3857@1o_800w";
        String flyleafHeadUrl = "http://image.weiyin.cc/719/185904/b6464d78-1ed9-4c4f-8fad-937a614d9893@1o_200w";
        String backCoverUrl = "http://image.weiyin.cc/719/185904/347B3767-12C6-4C60-844E-5012B1BF0F4E@1o_800w";

        //章节 微印代言人的图片
        String photoUrl1 = "http://image.weiyin.cc/719/185904/f8c4624e-f371-491c-b260-01aabe395685@1o_800w";
        String photoUrl2 = "http://image.weiyin.cc/719/185904/b970b0fe-79f5-4305-bd1d-6d84768b2c77@1o_800w";
        String photoUrl3 = "http://image.weiyin.cc/719/185904/8bb5a60a-443b-4c4c-821c-cc821b48efb8@1o_800w";
        String photoUrl4 = "http://image.weiyin.cc/719/185904/8e427964-961e-44f9-9ded-28d9272e3041@1o_800w";
        String photoUrl5 = "http://image.weiyin.cc/719/185904/e99a1f5e-20b4-4c5f-a059-3d1fc6940965@1o_800w";
        String photoUrl6 = "http://image.weiyin.cc/719/185904/36b1bf8f-7a23-4bb6-9a81-7517078ded82@1o_800w";
        String photoUrl7 = "http://image.weiyin.cc/719/185904/1deda7c7-817f-4995-9975-80b7e9d0075a@1o_800w";
        String photoUrl8 = "http://image.weiyin.cc/719/185904/a3424258-9ead-4264-99d9-ab38ebec967e@1o_800w";
        String photoUrl9 = "http://image.weiyin.cc/719/185904/1deda7c7-817f-4995-9975-80b7e9d0075a@1o_800w";
        String photoUrl10 = "http://image.weiyin.cc/719/185904/14d5fb62-e71d-4768-8481-05083223ac4d@1o_800w";
        String photoUrl11 = "http://image.weiyin.cc/719/185904/6da25ae9-c89c-410a-a437-8924102d1da1@1o_800w";

        //章节：记录旧时光的照片书
        String photoUrl12 = "http://image.weiyin.cc/719/185904/2e38af77-bf26-451a-859c-2a1383041d4d@1o_400w";
        String photoUrl12Text = "纸质书尺寸为235*235mm，对比市场上的书大小一般都是A5（21.5cm*14cm），尺寸增加25%";
        String photoUrl13 = "http://image.weiyin.cc/719/185904/557a28b6-a9fe-4978-a108-6b59ca13db10@1o_400w";
        String photoUrl13Text = "瀑布流排版：按照片顺序铺陈排版，能完整显示全部照片";
        String photoUrl14 = "http://image.weiyin.cc/719/185904/91cc8c4e-cff9-4ab0-beec-f9279408f059@1o_400w";
        String photoUrl14Text = "拼图排版：根据照片比例匹配系统模板，自动拼图排版";
        String photoUrl15 = "http://image.weiyin.cc/719/185904/8b35f243-f887-4b41-a4eb-8b52ece6e7ca@1o_400w";
        String photoUrl15Text = "内页：美感极致。纸色自然柔和，独具专利的先进涂布工艺，独特的表面质感，层次感强，是高档画册等高精印品的专业之选。";
        String photoUrl16 = "http://image.weiyin.cc/719/185904/921cc23d-293a-4939-bda1-93c5ee8aa13f@1o_400w";
        String photoUrl16Text = "内页：120克美感纸";
        String photoUrl17 = "http://image.weiyin.cc/719/185904/3ce2590a-6432-4bfe-ab74-0af8a338a918@1o_400w";
        String photoUrl17Text = "印刷：使用惠普indigo 10000高清顶级印刷。它采用HP Indigo液体电子油墨技术和独特的数字胶印工艺，能印刷出清晰的线条、绚丽夺目的图像和插图；印刷质量最高，堪比甚至超过胶印；还原性强且画质细腻。最主要，全国20多台，我们有其中一台";
        String photoUrl18 = "http://image.weiyin.cc/719/185904/7831b58e-35dd-4b8e-8ccd-120e9e8e7630@1o_400w";
        String photoUrl18Text = "内配4个护角";
        String photoUrl19 = "http://image.weiyin.cc/719/185904/30f25df8-ee53-465f-ad07-64b4b57a219e@1o_400w";
        String photoUrl19Text = photoUrl18Text;

        //章节：文艺的照片卡片
        String photoUrl27 = "http://image.weiyin.cc/719/185904/320c0493-08bc-43bb-a48c-40ef0d50ba27@1o_400w";
        String photoUrl27Text = "卡片材质：280克珠光纸";
        String photoUrl28 = "http://image.weiyin.cc/719/185904/679b4e03-36d3-4053-8a67-8d478c9eeb17@1o_400w";
        String photoUrl28Text = "尺寸：10.75cm*7.25cm";
        String photoUrl29 = "http://image.weiyin.cc/719/185904/305fabe6-020c-461c-9564-b15d1aada91c@1o_400w";
        String photoUrl30 = "http://image.weiyin.cc/719/185904/6e431524-bbbc-47b9-a998-4b35749f5579@1o_400w";

        //章节：怀旧的照片冲印
        String photoUrl39 = "http://image.weiyin.cc/719/185904/c220467d-77f1-4f1d-929d-80f2ba8b93c6@1o_400w";
        String photoUrl39Text = "300克双铜纸，画质细腻，呈现生动逼真的效果";
        String photoUrl40 = "http://image.weiyin.cc/719/185904/c03b80b7-c1a0-4d61-b387-3b8b6a9429b0@1o_400w";
        String photoUrl40Text = "高品质，双面过膜，正面过压纹膜，背面过哑膜。防水防污防氧化，平整不卷边";
        String photoUrl41 = "http://image.weiyin.cc/719/185904/fb5076c7-803c-4543-b885-ac17bf856dfc@1o_400w";
        String photoUrl41Text = "大6寸（4D）：150mmX114mm";
        String photoUrl42 = "http://image.weiyin.cc/719/185904/948ad9cb-21cf-474f-832f-9223ae7d9eaa@1o_400w";
        String photoUrl42Text = "惠普indigo10000高清顶级印刷，还原照片颜色和细节。采用惠普环保电子油墨，无银盐，呵护家人健康";

        //章节：精美照片定制台历
        String photoUrl43 = "http://image.weiyin.cc/719/185904/104a0dd8-81b6-4a10-92b2-c98b37996acf@1o_400w";
        String photoUrl43Text = "尺寸：A5大小（横版210mmX148mm）";
        String photoUrl44 = "http://image.weiyin.cc/719/185904/8784c33e-8142-4172-b53d-b0c5f02991ca@1o_400w";
        String photoUrl44Text = "内页：采用250克铜版纸印制，纸张平滑有光泽";
        String photoUrl45 = "http://image.weiyin.cc/719/185904/462f1c85-e4d2-4092-8e8d-5956431a3404@1o_400w";
        String photoUrl45Text = "装订：采用白色双线铁圈装订，360度灵活翻阅，牢固耐用";
        String photoUrl46 = "http://image.weiyin.cc/719/185904/4d96f47c-2def-4548-b354-5af45e4a2107@1o_400w";
        String photoUrl46Text = "底座：采用超厚牛皮纸板为支撑，可以平稳的立在桌面上";
        String photoUrl47 = "http://image.weiyin.cc/719/185904/17a8fcd1-0de5-43cf-ad5e-9496269ac7d6@1o_400w";
        String photoUrl47Text = "封面";
        String photoUrl48 = "http://image.weiyin.cc/719/185904/46877022-4ef3-4e53-addf-ef71624919e7@1o_400w";
        String photoUrl48Text = "内页1图，2图随机排版";
        String photoUrl49 = "http://image.weiyin.cc/719/185904/a7860f64-7b53-48ff-afb0-541f55e14a75@1o_400w";
        String photoUrl49Text = "背面可记事";

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

        WYSdk.getInstance().addChapterBlock("记录旧时光的照片书", "");
        WYSdk.getInstance().addPhotoBlock(photoUrl12Text, photoUrl12, photoUrl12, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl13Text, photoUrl13, photoUrl13, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl14Text, photoUrl14, photoUrl14, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl15Text, photoUrl15, photoUrl15, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl16Text, photoUrl16, photoUrl16, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl17Text, photoUrl17, photoUrl17, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl18Text, photoUrl18, photoUrl18, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl19Text, photoUrl19, photoUrl19, originalTime, 1289, 806);

        WYSdk.getInstance().addChapterBlock("文艺的照片卡片", "");

        WYSdk.getInstance().addPhotoBlock(photoUrl27Text, photoUrl27, photoUrl27, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl28Text, photoUrl28, photoUrl28, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl29, photoUrl29, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock("", photoUrl30, photoUrl30, originalTime, 1289, 806);

        WYSdk.getInstance().addChapterBlock("怀旧的照片冲印", "");
        WYSdk.getInstance().addPhotoBlock(photoUrl39Text, photoUrl39, photoUrl39, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl40Text, photoUrl40, photoUrl40, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl41Text, photoUrl41, photoUrl41, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl42Text, photoUrl42, photoUrl42, originalTime, 1289, 806);

        WYSdk.getInstance().addChapterBlock("精美照片定制台历", "");
        WYSdk.getInstance().addPhotoBlock(photoUrl43Text, photoUrl43, photoUrl43, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl44Text, photoUrl44, photoUrl44, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl45Text, photoUrl45, photoUrl45, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl46Text, photoUrl46, photoUrl46, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl47Text, photoUrl47, photoUrl47, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl48Text, photoUrl48, photoUrl48, originalTime, 1289, 806);
        WYSdk.getInstance().addPhotoBlock(photoUrl49Text, photoUrl49, photoUrl49, originalTime, 1289, 806);

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
