package com.common.quick.utils;


import android.os.Handler;
import android.os.Message;


/**
 * Sometime like RxJava's BackPressure System, but build by {@link Handler}
 */
public class BackPressureHandler extends Handler {

    public static final int PRESSURE_ACCEPT_LAST = 0;
    public static final int PRESSURE_ACCEPT_FIRST = 1;


    private long mLastEmmit = 0;
    private long mBackPressureFactor = 100;
    private int mPressurePolicy = PRESSURE_ACCEPT_LAST;

    public void setBackPressure(int pressure) {
        mBackPressureFactor = pressure;
    }

    public void setBackPressurePolicy(int policy) {
        mPressurePolicy = policy;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }

    public void postWithBackPressure(Runnable runnable) {
        postWithBackPressure(runnable, mBackPressureFactor);
    }

    public void postWithBackPressure(Runnable runnable, long delay) {
        long current = System.currentTimeMillis();
        boolean redLight = current - mLastEmmit < mBackPressureFactor;
        // remove all
        if (mPressurePolicy == PRESSURE_ACCEPT_LAST) {
            if (redLight) {
                removeCallbacksAndMessages(null);
            }
            postDelayed(runnable, Math.max(delay, mBackPressureFactor));
            mLastEmmit = current;
        } else if (mPressurePolicy == PRESSURE_ACCEPT_FIRST) {
            if (redLight) {
                // do nothing
            } else {
                postDelayed(runnable, delay);
                mLastEmmit = current;
            }
        }
    }

}
