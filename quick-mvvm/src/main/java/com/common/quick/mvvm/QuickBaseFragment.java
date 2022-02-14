package com.common.quick.mvvm;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.quick.BR;
import com.common.quick.utils.reflect.ClassWrapper;
import com.common.quick.utils.reflect.Generic;
import com.common.quick.utils.reflect.ObjectWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class QuickBaseFragment<B extends ViewDataBinding, V extends QuickBaseViewModel<?>> extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    protected B mDataBinding;
    protected V mViewModel;

    protected final Handler mHandler = new Handler();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        mViewModel = createViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        mDataBinding = createDataBinding(inflater, container);
        mDataBinding.setLifecycleOwner(this);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        mDataBinding.setVariable(BR.view, this);
        mDataBinding.setVariable(BR.fragment, this);
        mDataBinding.setVariable(BR.activity, getActivity());
        DialogFragment dialogFragment = getDialog();
        if (dialogFragment != null) {
            mDataBinding.setVariable(BR.dialog, dialogFragment);
        }
        // will set something to viewModel
        if (!mViewModel.isInitialized()) {
            mViewModel.setArguments(getArguments());
            mViewModel.created();
        }
        mDataBinding.setVariable(BR.viewModel, mViewModel);
        mDataBinding.setVariable(BR.model, mViewModel.getModel());
        init(mDataBinding);
    }


    /**
     * If  getParentFragment is DialogFragment, return it and cast it to target dialog.
     *
     * @param <T>
     * @return
     */
    public <T extends DialogFragment> T getDialog() {
        if (getParentFragment() instanceof DialogFragment) {
            return (T) getParentFragment();
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");
    }

    protected void setViewDataBinding(B dataBinding, V viewModel) {
    }

    protected B createDataBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return ClassWrapper.wrap(getDataBindingType()).invoke("inflate",
                ObjectWrapper.wrap(LayoutInflater.class, layoutInflater),
                ObjectWrapper.wrap(ViewGroup.class, container),
                ObjectWrapper.wrap(boolean.class, false)
        );
    }

    /**
     * If fragment viewModel's type equals activity viewModel's type, reuse it of activity.
     *
     * @return
     */
    protected V createViewModel() {
        Class<V> viewModelType = getViewModelType();
        if (getActivity() instanceof QuickBaseActivity) {
            Class parentViewModelClz = Generic.getParamTypeForSuper(getActivity().getClass(), 1);
            if (parentViewModelClz != null && viewModelType.isAssignableFrom(parentViewModelClz)) {
                return (V) new ViewModelProvider(getActivity()).get(parentViewModelClz);
            }
        }

        Fragment fragment = getParentFragment(); // 递归查找fragment
        while (fragment != null && !(fragment instanceof QuickBaseFragment)) {
            fragment = fragment.getParentFragment();
        }
        if (fragment != null && fragment instanceof QuickBaseFragment) {
            Class parentViewModelClz = Generic.getParamTypeForSuper(fragment.getClass(), 1);
            if (parentViewModelClz != null && viewModelType.isAssignableFrom(parentViewModelClz)) {
                return (V) new ViewModelProvider(fragment).get(parentViewModelClz);
            }
        }
        return new ViewModelProvider(this).get(viewModelType);
    }

    protected B getDataBinding() {
        return mDataBinding;
    }

    public V getViewModel() {
        return mViewModel;
    }

    protected void init(B binding) {
        setViewDataBinding(binding, mViewModel);
    }

    private Class<B> getDataBindingType() {
        return Generic.getParamType(getClass(), 0);
    }

    protected Class<V> getViewModelType() {
        return Generic.getParamType(getClass(), 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        getDataBinding().unbind();
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
        Log.v(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "onDetach");
    }

    /**
     * Getting handler.
     *
     * @return
     */
    public Handler getHandler() {
        return mHandler;
    }
}
