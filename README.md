Android SDK开发包 版本号 1.5.0 日期 20170103

安装

当您下载了WeiYin Android SDK 的 zip 包后，进行以下步骤（对Android studio适用）:

注意本SDK引用了 RETROFIT2.1.2如果遇到JAR冲突请自行去掉

1.把wysdk放到工程根目录

2.在根目录找到settings.gradle，增加 include ':wysdk'

3.在app找到build.gradle，增加compile project(':wysdk')

4.Android Studio 会提示 Sync Now 点击即可 或手动点Sync

混淆配置

    -keepattributes Signature
    -keep class * extends java.lang.annotation.Annotation { *; }
    -keepattributes *Annotation*

    -keepattributes *JavascriptInterface*

    -keepclassmembers class
    -dontwarn com.baidu.**
    com.weiyin.wysdk.activity.WYWebViewActivity$AndroidClient {
     public *;
    }

    -keep class com.weiyin.wysdk.model.** {*;}
    -dontwarn com.weiyin.wysdk.model.**
    -keepclassmembers class com.weiyin.wysdk.model {
      <fields>;
      <methods>;
    }


AndroidManifest.xml 配置

    <!-- 通用权限 -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!--支付sdk-->
    <activity
        android:name="com.pingplusplus.android.PaymentActivity"
        android:launchMode="singleTop"
        android:theme="@android:style/Theme.Translucent.NoTitleBar" />
    <activity
        android:name="com.alipay.sdk.app.H5PayActivity"
        android:configChanges="orientation|keyboardHidden|navigation"
        android:exported="false"
        android:screenOrientation="behind" />
    <activity
        android:name="com.alipay.sdk.auth.AuthActivity"
        android:configChanges="orientation|keyboardHidden|navigation"
        android:exported="false"
        android:screenOrientation="behind" />

    <activity-alias
        android:name=".wxapi.WXPayEntryActivity"
        android:exported="true"
        android:targetActivity="com.pingplusplus.android.PaymentActivity" />
    <!--支付sdk-->


初始化

    //openid 每个合作方的每个用户唯一标识 建议写法 前缀+唯一标识 如 WY_xxxxxxx
    WYSdk.getInstance().setSDK(this, "52HJR62BDS6SDD21", "VlYmY2ZjBmOWFmZTJlZTk3NzdhN2M0ODM0MjE3", "openid");


编辑页

    //数据编辑面页 默认是打开的 关闭直接就跳去排版页
    private void edit() {
    //长按可以编辑文本
    WYSdk.getInstance().isShowSelectDataActivity(true);

    //数据编辑面页 打开就要处理 图片加载的回调 //这个必须处理不然不显示图片的
    WYSdk.getInstance().setWyImageLoader(new WYImageLoader() {
        @Override
        public void displayImage(ImageView imageView, String path, String lowPixelUrl, int width, int height) {
            ImageLoaderWrapper.getDefault().displayImage(path, imageView);
        }
    });
    //上拉加载更多 默认关闭的
    WYSdk.getInstance().openLoadMore(true);
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

                    //这个是必须调的用来关闭loading和设置数据 可传空
                    //当你没更多数据的时候你可以关闭下拉刷新 WYSdk.getInstance().openLoadMore(false);
                    WYSdk.getInstance().addLoadMoreData(blockList);
                }
            }, 2000);
        }
    });
    }


合作方支付

    //设置是否合作方的app支付,默认是false
    //如果是用微印支付或合作方的pingxx支付不需要设置这些
    private void myAppPay() {
    WYSdk.getInstance().setMyAppPay(true);
    WYSdk.getInstance().setWyPayOrderListener(new WYPayOrderListener() {
        @Override
        public void pay(Activity context,String orderId, float price, String randomStr) {
            //处理支付
            //合作方需要 orderId randomStr 来通知微印服务器
            //支付成功后,合作方服务器调微印的服务器更新支付结果,文档在联调时索取
        }
    });
    }

    //刷新支结果,用来ui显示的 {@link WYSdk.PAY_SUCCESS,WYSdk.PAY_FAIL,WYSdk.PAY_CANCEL,WYSdk.PAY_INVALID}
    WYSdk.getInstance().refreshPayState(String result)


购物车

    //这个是打开购物车,需要的时候可以调用
    WYSdk.getInstance().showShopCart(this);


订单页

    //这个是打开订单页,需要的时候可以调用
    WYSdk.getInstance().showOrderList(this);
    //刷新订单页状态,如果用合作方支付是需要刷新支付状态的
    WYSdk.getInstance().refreshOrderState();


了解纸质画册

    //这个是打开纸质画册,需要的时候可以调用
    WYSdk.getInstance().showPaper(this);


排版页

    //设置好上述相关数据后调用 postPrintData() 即可预览排版页
    //bookType=> WYSdk.Print_Book,WYSdk.Print_Card,WYSdk.Print_Photo,WYSdk.Print_Calendar
    WYSdk.getInstance().postPrintData(this,bookType, new WYListener<Object>())


其他设置

    // 设置主题颜色 16进制的颜色 如: f56971
    WYSdk.getInstance().setThemeColor("f56971")


SDK使用注意事项

照片页和文本页是可以从属在章节页下的，add时候的顺序要注意

如果想某个图片或文本要从属在某个章节页下，就先要addChapterBlock再add照片页或文本页

当然如果不想放在某个章节下面就直接add照片和文本即可

这些add都是有先后顺序区分的，排版页看到的顺序是add先到后



例子

    public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();

    ImageLoaderWrapper.initDefault(this, ImageLoaderWrapper.getTmpDir(), false);
    //openid 每个合作方的每个用户唯一标识 建议写法 前缀+唯一标识 如 WY_xxxxxxx
    WYSdk.getInstance().setSDK(this, "52HJR62BDS6SDD21", "VlYmY2ZjBmOWFmZTJlZTk3NzdhN2M0ODM0MjE3", "openid");

    //编辑面页设置
    //edit();

    //这个是打开订单页,需要的时候可以调用
    //WYSdk.getInstance().showOrderList(this);
    //刷新订单页状态,如果用合作方支付是需要刷新支付状态的
    //WYSdk.getInstance().refreshOrderState();
    }

    //设置是否合作方的app支付,默认是false
    //如果是用微印支付或合作方的pingxx支付不需要设置这些
    private void myAppPay() {
    WYSdk.getInstance().setMyAppPay(true);
    WYSdk.getInstance().setWyPayOrderListener(new WYPayOrderListener() {
        @Override
        public void pay(Activity context,String orderId, float price, String randomStr) {
            //处理支付
            //合作方需要 orderId randomStr 来通知微印服务器
            //支付成功后,合作方服务器调微印的服务器更新支付结果,文档在联调时索取
        }
    });
    }

    //数据编辑面页 默认是打开的 关闭直接就跳去排版页
    private void edit() {
    //长按可以编辑文本
    WYSdk.getInstance().isShowSelectDataActivity(true);

    //数据编辑面页 打开就要处理 图片加载的回调 //这个必须处理不然不显示图片的
    WYSdk.getInstance().setWyImageLoader(new WYImageLoader() {
        @Override
        public void displayImage(ImageView imageView, String path, String lowPixelUrl, int width, int height) {
            ImageLoaderWrapper.getDefault().displayImage(path, imageView);
        }
    });

    //上拉加载更多 默认关闭的
    WYSdk.getInstance().openLoadMore(true);
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

                    //这个是必须调的用来关闭loading和设置数据 可传空
                    //当你没更多数据的时候你可以关闭下拉刷新 WYSdk.getInstance().openLoadMore(false);
                    WYSdk.getInstance().addLoadMoreData(blockList);
                }
            }, 2000);
        }
    });
    }

    private void initView() {
    progressDialog = new ProgressDialog(this);
    TextView textview = (TextView) findViewById(R.id.text);
    textview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //每次请求前都要添加好数据,请求成功或失败都会清除数据
            addData();
            postData();
        }
    });
    }

    private void addData() {
    //图片素材 必须是网络路径 宽高也是必要的
    String frontCoverUrl = "http://img1.3lian.com/2015/w7/98/d/22.jpg";//1210 x 681
    String flyleafHeadUrl = "http://img21.mtime.cn/mg/2011/05/18/161045.63077415.jpg";//251 x 251
    String backCoverUrl = "http://img.61gequ.com/allimg/2011-4/201142614314278502.jpg";//1358 x 765

    String photoUrl1 = "http://img1.3lian.com/2015/w7/90/d/1.jpg";//1289 x 806
    String photoUrl2 = "http://img2.3lian.com/img2007/23/08/025.jpg";//1001 x 751
    String photoUrl3 = "http://img1.goepe.com/201303/1362711681_6600.jpg";//988 x 738
    String photoUrl4 = "http://pic1.ooopic.com/00/87/39/27b1OOOPICf7.jpg";//813 x 592
    String photoUrl5 = "http://www.ctps.cn/PhotoNet/Profiles2011/20110503/20115302844162622467.jpg";//1208 x 806
    String photoUrl6 = "http://img2.3lian.com/2014/f2/110/d/57.jpg";//626 x 468

    //拍摄时间,由于是网络图片就自定义了一个时间
    long originalTime = System.currentTimeMillis() / 1000;

    WYSdk.getInstance().setFrontCover("封面也就是书名", "封面副标题", frontCoverUrl, frontCoverUrl, originalTime, 1210, 681);
    WYSdk.getInstance().setFlyleaf("头像", flyleafHeadUrl, flyleafHeadUrl, originalTime, 251, 251);
    WYSdk.getInstance().setPreface("这是序言");
    WYSdk.getInstance().setCopyright("这是作者名称", "这个是书名");
    WYSdk.getInstance().setBackCover(backCoverUrl, backCoverUrl, originalTime, 1358, 765);

    WYSdk.getInstance().addPhotoBlock("图片1", photoUrl1, photoUrl1, originalTime, 1289, 806);
    WYSdk.getInstance().addTextBlock("这是一段大文本1哦,我没跟在章节后面的哦");
    WYSdk.getInstance().addPhotoBlock("这个是照片2的描述哦,我也没跟在章节后面呢", photoUrl2, photoUrl2, originalTime, 1001, 751);

    WYSdk.getInstance().addChapterBlock("我是一个章节占一页哦", "我是章节的描述好吧");
    WYSdk.getInstance().addPhotoBlock("这个是照片3的描述哦,我也跟在章节后面呢", photoUrl3, photoUrl3, originalTime, 988, 738);
    WYSdk.getInstance().addPhotoBlock("这个是照片4的描述哦,我也跟在章节后面呢", photoUrl4, photoUrl4, originalTime, 813, 592);
    WYSdk.getInstance().addTextBlock("我是一个跟章节后面的文本2");

    WYSdk.getInstance().addChapterBlock("我是章节2", "我是章节的描述好吧");
    WYSdk.getInstance().addPhotoBlock("", photoUrl5, photoUrl5, originalTime, 1208, 806);
    WYSdk.getInstance().addPhotoBlock("这个是照片6的描述哦,我也跟在章节后面呢", photoUrl6, photoUrl6, originalTime, 626, 468);
    WYSdk.getInstance().addTextBlock("我是跟章节2后面的文本");
    }

    private void postData() {
      WYSdk.getInstance().postPrintData(this, new WYListener<Object>() {

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
