package com.common.quick.widget;

import android.util.Log;
import android.view.View;

public class AvoidRapidOnClickListener implements View.OnClickListener {

    public static String TAG = AvoidRapidOnClickListener.class.getSimpleName();

    private long lastClick;
    private long avoidTimeMilli;

    private View.OnClickListener listener;

    public AvoidRapidOnClickListener(View.OnClickListener listener) {
        this(listener, 500);
    }

    public AvoidRapidOnClickListener(View.OnClickListener listener, long avoidTimeMilli) {
        if (listener == null) {
            throw new RuntimeException("listener can not be null !");
        }
        this.listener = listener;
        this.avoidTimeMilli = avoidTimeMilli;
    }

    @Override
    public final void onClick(View v) {
        Log.e(TAG, "onClick -> " + v);
        if (System.currentTimeMillis() - lastClick > avoidTimeMilli) {
            listener.onClick(v);
            lastClick = System.currentTimeMillis();
        }
    }
}
