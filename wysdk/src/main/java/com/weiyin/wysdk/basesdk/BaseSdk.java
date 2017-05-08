package com.weiyin.wysdk.basesdk;


import com.weiyin.wysdk.http.HttpStore;
import com.weiyin.wysdk.model.BaseResultBean;
import com.weiyin.wysdk.util.thread.AsyncExecutor;
import com.weiyin.wysdk.util.thread.AsyncQueueExecutor;
import com.weiyin.wysdk.util.thread.MainThreadExecutor;

public class BaseSdk {

    protected HttpStore mHttpStore;

    protected BaseSdk() {
        this.mHttpStore = new HttpStore();
    }

    protected final void callStart(final Controller controller) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                WYListener<?> WYListener = controller.getListener();
                if (WYListener != null) {
                    WYListener.onStart();
                }
            }
        });
    }

    protected final <R> void callSuccess(final Controller controller, final R result) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                WYListener<R> WYListener = (WYListener<R>) controller.getListener();
                if (WYListener != null) {
                    WYListener.onSuccess(result);
                }
            }
        });
    }

    protected final void callFail(final Controller controller, final String errorMsg) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                WYListener<?> WYListener = controller.getListener();
                if (WYListener != null) {
                    WYListener.onFail(errorMsg);
                }
            }
        });
    }

    public final void cancelController(Controller controller) {
        if (controller != null) {
            controller.clearListener();
        }
    }

    protected final void runOnAsyncThread(Runnable runnable) {
        AsyncExecutor.getInstance().execute(runnable);
    }

    protected final void runOnAsyncThread(Runnable runnable, int delay) {
        AsyncExecutor.getInstance().execute(runnable, delay);
    }

    protected final void runOnMainThread(Runnable runnable) {
        MainThreadExecutor.getInstance().execute(runnable);
    }

    protected final void runOnAsyncQueue(Runnable runnable) {
        AsyncQueueExecutor.getInstance().execute(runnable);
    }

    protected void handleResult(BaseResultBean resultBean, Controller controller, HandleResultListener handleResultListener) {
        if (resultBean != null) {
            if (resultBean.ok()) {
                if (handleResultListener != null) {
                    handleResultListener.ok();
                }
            } else {
                if (handleResultListener != null) {
                    handleResultListener.fail();
                }
                callFail(controller, resultBean.resultCode);
            }
        } else {
            if (handleResultListener != null) {
                handleResultListener.fail();
            }
            callFail(controller, "server or network error");
        }
    }

    public interface HandleResultListener {
        void ok();

        void fail();
    }
}
