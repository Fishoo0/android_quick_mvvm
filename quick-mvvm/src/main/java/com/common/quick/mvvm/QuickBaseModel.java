package com.common.quick.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.databinding.BaseObservable;

public class QuickBaseModel extends BaseObservable {

    protected String TAG = this.getClass().getSimpleName();
    private final Application mApplication;

    public QuickBaseModel(Application application) {
        this.mApplication = application;
        onCreated();
    }

    protected void onCreated() {
        Log.i(TAG, "onCreated");
    }

    public final void onCleared() {
        Log.i(TAG, "onCleared");
        onDestroyed();
    }

    protected void onDestroyed() {
        Log.i(TAG, "onDestroyed");
    }


}
