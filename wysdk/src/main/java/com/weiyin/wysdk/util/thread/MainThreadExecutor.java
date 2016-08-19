package com.weiyin.wysdk.util.thread;

import android.os.Looper;
import android.support.annotation.NonNull;


public class MainThreadExecutor implements IExecutor {
    private final WeakHandler mHandler;

    private static class SingletonHolder {
        public static final MainThreadExecutor INSTANCE = new MainThreadExecutor();
    }

    public static MainThreadExecutor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private MainThreadExecutor() {
        mHandler = new WeakHandler(Looper.getMainLooper());
    }

    @Override
    public void execute(@NonNull Runnable r) {
        mHandler.post(r);
    }

    @Override
    public void execute(Runnable r, long delayed) {
        mHandler.postDelayed(r, delayed);
    }
}
