package com.weiyin.wysdk.basesdk;


public class Controller {

    private Object[] mParams;
    private WYListener<?> mWYListener;

    public Controller(WYListener<?> WYListener, Object... params) {
        this.mWYListener = WYListener;
        this.mParams = params;
    }

    public WYListener<?> getListener() {
        return mWYListener;
    }

    public void clearListener() {
        mWYListener = null;
    }
}
