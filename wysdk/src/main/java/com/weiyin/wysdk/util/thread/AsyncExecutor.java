package com.weiyin.wysdk.util.thread;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class AsyncExecutor implements IExecutor {

    private Executor mExecutor;

    private static class SingletonHolder {
        public static final AsyncExecutor INSTANCE = new AsyncExecutor();
    }

    public static AsyncExecutor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private AsyncExecutor() {
        mExecutor = Executors.newScheduledThreadPool(64, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, "AsyncExecutor Runnable");
            }
        });
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
