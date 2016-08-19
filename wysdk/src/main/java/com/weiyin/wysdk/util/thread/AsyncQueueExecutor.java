package com.weiyin.wysdk.util.thread;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程队列
 * Created by hu on 15/1/26.
 */
public class AsyncQueueExecutor implements IExecutor {
    private Executor mExecutor;

    private static class SingletonHolder {
        public static final AsyncQueueExecutor INSTANCE = new AsyncQueueExecutor();
    }

    public static AsyncQueueExecutor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private AsyncQueueExecutor() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable r) {
        mExecutor.execute(r);
    }

    @Override
    public void execute(final Runnable r, long delayed) {
        MainThreadExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                execute(r);
            }
        }, delayed);
    }
}
