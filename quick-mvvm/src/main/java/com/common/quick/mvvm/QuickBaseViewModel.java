package com.common.quick.mvvm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.Observable;
import androidx.lifecycle.AndroidViewModel;

import com.common.quick.utils.reflect.Generic;

public class QuickBaseViewModel<M extends QuickBaseModel> extends AndroidViewModel implements Observable {

    protected final String TAG = this.getClass().getSimpleName();
    private static final Bundle EMPTY = new Bundle();

    private boolean mCreated = false;
    protected Bundle mArguments = EMPTY;
    protected M mModel;

    public QuickBaseViewModel(Application app) {
        super(app);
        mModel = createModel(app);
    }

    public void setModel(M model) {
        mModel = model;
    }

    protected Context getContext() {
        return getApplication();
    }

    protected String getString(int res) {
        return getApplication().getString(res);
    }

    protected M createModel(Application app) {
        return QuickModelProvider.getModel(getModelType(), app);
    }

    public void setArguments(Bundle arguments) {
        if (arguments == null)
            return;
        mArguments = arguments;
//        ARouter.getInstance().inject(this);
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public final void created() {
        mCreated = true;
        onCreated(getArguments());
    }

    /**
     * The view model has been initialized.
     */
    protected void onCreated(Bundle argument) {
        Log.i(TAG, "onCreated");
    }

    /**
     * Whether we have initialized or not, if we have initialized, we do nothing because
     * this is a re-used viewModel.
     *
     * @return
     */
    public boolean isInitialized() {
        return mCreated;
    }

    public M getModel() {
        return mModel;
    }

    protected Class<M> getModelType() {
        return Generic.getParamType(getClass(), 0);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }


    protected void onDestroyed() {
        Log.i(TAG, "onDestroyed");
    }

    /**
     * Call {@link #onDestroyed()} instead.
     */
    @Override
    protected final void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared");
        if (mModel != null) {
            mModel.onCleared();
        }
        onDestroyed();
    }
}
