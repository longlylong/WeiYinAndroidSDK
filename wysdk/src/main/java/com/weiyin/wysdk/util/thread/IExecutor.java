package com.weiyin.wysdk.util.thread;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public interface IExecutor extends Executor {

    void execute(@NonNull Runnable r);

    void execute(Runnable r, long delayed);
}
