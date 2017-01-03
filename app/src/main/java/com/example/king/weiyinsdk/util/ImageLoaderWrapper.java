package com.example.king.weiyinsdk.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * String imageUri = "http://site.com/image.png"; // from Web String imageUri =
 * "file:///mnt/sdcard/image.png"; // from SD card String imageUri =
 * "content://media/external/audio/albumart/13"; // from content provider String
 * imageUri = "assets://image.png"; // from assets String imageUri =
 * "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
 */
public class ImageLoaderWrapper {
    private static ImageLoaderWrapper sDefaultInstance;
    private ImageLoader mDefaultImageLoader;

    private ImageLoaderWrapper(Context context, File cacheDir, boolean debug) {
        context = context.getApplicationContext();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        ImageLoaderConfiguration.Builder imageLoaderConfigurationBuilder = new ImageLoaderConfiguration.Builder(
                context.getApplicationContext())
                .threadPoolSize(10 * getNumCores())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory();

        imageLoaderConfigurationBuilder.diskCacheExtraOptions(screenWidth,
                screenHeight, null);
        try {
            imageLoaderConfigurationBuilder.diskCache(new LruDiskCache(cacheDir, new HashCodeFileNameGenerator(), 500 * 1024 * 1024));
        } catch (IOException e) {
            imageLoaderConfigurationBuilder.diskCache(new UnlimitedDiskCache(
                    cacheDir));
        }

        imageLoaderConfigurationBuilder.diskCacheSize(500 * 1024 * 1024);
        imageLoaderConfigurationBuilder
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator());

        //imageLoaderConfigurationBuilder.memoryCache(new FIFOLimitedMemoryCache(calculateDefaultMaxSize(context, 4)));
        imageLoaderConfigurationBuilder.memoryCache(new WeakMemoryCache());
        //imageLoaderConfigurationBuilder.memoryCache(new LruMemoryCache(2 * 1024 * 1024));

        imageLoaderConfigurationBuilder.memoryCacheSizePercentage(20);
        imageLoaderConfigurationBuilder.memoryCacheExtraOptions(screenWidth,
                screenHeight);

        imageLoaderConfigurationBuilder
                .imageDownloader(new BaseImageDownloader(context));
        imageLoaderConfigurationBuilder
                .imageDecoder(new BaseImageDecoder(debug));

        imageLoaderConfigurationBuilder
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple());

        if (debug) {
            imageLoaderConfigurationBuilder.writeDebugLogs();
        }

        ImageLoader.getInstance().init(imageLoaderConfigurationBuilder.build());
        mDefaultImageLoader = ImageLoader.getInstance();
    }

    public synchronized static ImageLoaderWrapper initDefault(Context context,
                                                              File discCacheDir, boolean debug) {
        if (sDefaultInstance == null) {
            sDefaultInstance = new ImageLoaderWrapper(context, discCacheDir,
                    debug);
        }

        return sDefaultInstance;
    }

    public synchronized static ImageLoaderWrapper getDefault() {
        if (sDefaultInstance == null) {
            throw new RuntimeException(
                    "Must be call initDefault(Context, File) befor!");
        }

        return sDefaultInstance;
    }

    public OnScrollListener newScrollListenerWithTheImageLoader(
            boolean pauseOnScroll, boolean pauseOnFling) {
        return new PauseOnScrollListener(mDefaultImageLoader, pauseOnScroll,
                pauseOnFling);
    }

    public OnScrollListener newScrollListenerWithTheImageLoader(
            boolean pauseOnScroll, boolean pauseOnFling,
            OnScrollListener customListener) {
        return new PauseOnScrollListener(mDefaultImageLoader, pauseOnScroll,
                pauseOnFling, customListener);
    }

    public ImageLoader getImageLoader() {
        return mDefaultImageLoader;
    }

    public void loadImage(String imageUri) {
        loadImage(imageUri, null, null);
    }

    public void loadImage(String imageUri, ImageLoadingListener listener) {
        loadImage(imageUri, null, listener);
    }

    public void loadImage(String imageUri, DisplayConfig config) {
        loadImage(imageUri, config, null);
    }

    public void loadImage(String imageUri, DisplayConfig config,
                          ImageLoadingListener listener) {
        if (config == null) {
            config = new DisplayConfig();
        }

        if (!TextUtils.isEmpty(imageUri)) {
            if (imageUri.contains("https:")) {
                imageUri = imageUri.replace("https:", "http:");
            }
            Uri uri = Uri.parse(imageUri);
            String scheme = uri.getScheme();
            config.supportDiskCache = "http".equals(scheme) || "https".equals(scheme);
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(config.stubImageRes)
                .showImageForEmptyUri(config.loadFailImageRes)
                .showImageOnFail(config.loadFailImageRes)
                .cacheInMemory(config.supportMemoryCache)
                .cacheOnDisk(config.supportDiskCache)
                .imageScaleType(config.imageScaleType)
                .resetViewBeforeLoading(config.isResetView)
                .displayer(config.displayer).build();

        mDefaultImageLoader.loadImage(imageUri, options, listener);
    }

    public void displayImage(String imageUri, ImageView imageView) {
        displayImage(imageUri, imageView, null, null);
    }

    public void displayImage(String imageUri, ImageView imageView,
                             ImageLoadingListener listener) {
        displayImage(imageUri, imageView, null, listener);
    }

    public void displayImage(String imageUri, ImageView imageView,
                             DisplayConfig config) {
        displayImage(imageUri, imageView, config, null);
    }

    public void displayImage(String imageUri, ImageView imageView,
                             DisplayConfig config, ImageLoadingListener listener) {
        if (config == null) {
            config = new DisplayConfig();
        }

        if (!TextUtils.isEmpty(imageUri)) {
            Uri uri = Uri.parse(imageUri);
            String scheme = uri.getScheme();
            config.supportDiskCache = "http".equals(scheme) || "https".equals(scheme);
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(config.stubImageRes)
                .showImageForEmptyUri(config.loadFailImageRes)
                .showImageOnFail(config.loadFailImageRes)
                .cacheInMemory(config.supportMemoryCache)
                .cacheOnDisk(config.supportDiskCache)
                .imageScaleType(config.imageScaleType)
                .resetViewBeforeLoading(config.isResetView)
                .bitmapConfig(Config.RGB_565).displayer(config.displayer)
                .considerExifParams(true).build();
        mDefaultImageLoader
                .displayImage(imageUri, imageView, options, listener);
    }

    public void clearDefaultLoaderMemoryCache() {
        mDefaultImageLoader.clearMemoryCache();
    }

    public void clearDefaultLoaderDiscCache() {
        mDefaultImageLoader.clearDiskCache();
    }

    /**
     * 停止所有下载图片的任务
     */
    public void pause() {
        mDefaultImageLoader.pause();
    }

    /**
     * 恢复被暂停的任务
     */
    public void resume() {
        mDefaultImageLoader.resume();
    }

    /**
     * 取消所有的下载图片的任务
     */
    public void stop() {
        mDefaultImageLoader.stop();
    }

    /**
     * 根据传入的URI搜索所有在内存中缓存的位图 注意：内存缓存可以包含不同尺寸的相同图像
     */
    public List<Bitmap> findMemoryCachedBitmapsForImageUri(String imageUri) {
        if (TextUtils.isEmpty(imageUri)) {
            return null;
        }

        return MemoryCacheUtils.findCachedBitmapsForImageUri(imageUri,
                mDefaultImageLoader.getMemoryCache());
    }

    /**
     * 根据传入的URI删除位图缓存
     */
    public void removeMemoryFromCache(String imageUri) {
        MemoryCacheUtils.removeFromCache(imageUri,
                mDefaultImageLoader.getMemoryCache());
    }

    /**
     * 根据传入的URI搜索所有在磁盘中缓存的位图文件
     */
    public File findDiscCachedBitmapsForImageUri(String imageUri) {
        return DiskCacheUtils.findInCache(imageUri,
                mDefaultImageLoader.getDiskCache());
    }

    /**
     * 根据传入的URI删除位图缓存
     */
    public void removeDiscFromCache(String imageUri) {
        DiskCacheUtils.removeFromCache(imageUri,
                mDefaultImageLoader.getDiskCache());
    }

    // CPU个数
    private int getNumCores() {
        class CpuFilter implements FileFilter {

            @Override
            public boolean accept(File pathname) {
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }

        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return Math.max(1, files.length);
        } catch (Exception e) {
            return 1;
        }
    }

    private int calculateDefaultMaxSize(Context context, int weight) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        boolean largeHeap = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            memoryClass = am.getLargeMemoryClass();
        }

        return 1024 * 1024 * Math.min(12, memoryClass / weight);
    }

    public static class DisplayConfig {
        private static final BitmapDisplayer sDefault = new SimpleBitmapDisplayer();
        private static final BitmapDisplayer sDefaultFadein = new FadeInBitmapDisplayer(
                300);
        private static final BitmapDisplayer sDefaultRound = new RoundedBitmapDisplayer(
                10);
        private final ImageScaleType imageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
        public boolean supportMemoryCache;
        public boolean supportDiskCache;
        public boolean isResetView;
        public int stubImageRes;
        public int loadFailImageRes;
        private BitmapDisplayer displayer;

        private DisplayConfig() {
            supportMemoryCache = true;
            supportDiskCache = true;
            isResetView = false;
            stubImageRes = 0;
            loadFailImageRes = 0;
            displayer = new SimpleBitmapDisplayer();
        }

        public static class Builder {
            public Builder() {
            }

            public DisplayConfig build() {
                DisplayConfig config = new DisplayConfig();
                config.displayer = sDefault;
                return config;
            }

            public DisplayConfig buildFadein() {
                DisplayConfig config = new DisplayConfig();
                config.displayer = sDefaultFadein;
                return config;
            }

            public DisplayConfig buildRounded() {
                DisplayConfig config = new DisplayConfig();
                config.displayer = sDefaultRound;
                return config;
            }

            public DisplayConfig buildFadein(int fadeInTime) {
                DisplayConfig config = new DisplayConfig();
                config.displayer = new FadeInBitmapDisplayer(fadeInTime);
                return config;
            }

            public DisplayConfig buildRounded(int round) {
                DisplayConfig config = new DisplayConfig();
                config.displayer = new RoundedBitmapDisplayer(round);
                return config;
            }
        }
    }

    /**
     * 获取临时目录
     */
    public static File getTmpDir() {
        File tmpDir;

        tmpDir = new File(Environment.getExternalStorageDirectory(),
                "WeiYinSdkDemo");

        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        return tmpDir;
    }
}
