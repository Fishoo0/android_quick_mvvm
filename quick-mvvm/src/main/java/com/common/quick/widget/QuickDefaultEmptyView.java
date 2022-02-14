package com.common.quick.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.common.quick.BR;
import com.common.quick.databinding.QuickDefaultEmptyViewBinding;
import com.common.quick.utils.QuickUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * Recommend using the empty view for catch empty issues.
 * <p>
 * 1, You can use attribute and data binding for easy case, like changing the default empty image or empty text.
 */
public class QuickDefaultEmptyView extends FrameLayout {

    private final QuickDefaultEmptyViewBinding mViewBinding;
    private final MutableLiveData<Data> mViewData = new MutableLiveData<>(Data.build());

    public QuickDefaultEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mViewBinding = QuickDefaultEmptyViewBinding.inflate(LayoutInflater.from(context), this, true);
        mViewBinding.setVariable(BR.view, this);
        setVisibility(View.GONE);
        QuickEmptyViewLayout.bindEmptyView(this, QuickEmptyViewLayout.FLAG_ALL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        QuickUtils.setLifeCycleOwner(mViewBinding);
    }

    public LiveData<QuickEmptyViewLayout.Data> getEmptyData() {
        return QuickEmptyViewLayout.findEmptyView(this).getLiveData();
    }

    @androidx.databinding.BindingAdapter(value = {"emptyImageRes", "emptyTextDes", "emptyButtonDes", "onEmptyRetry"}, requireAll = false)
    public static void bindEmptyParams(QuickDefaultEmptyView view, Object imgRes, Object value, Object value1, OnClickListener listener) {
        Data data = view.mViewData.getValue();
        if (imgRes != null) {
            data.emptyImageData = imgRes;
        }
        if (getString(view.getContext(), value) != null) {
            data.emptyTextDes = getString(view.getContext(), value);
        }
        if (getString(view.getContext(), value1) != null) {
            data.emptyButtonDes = getString(view.getContext(), getString(view.getContext(), value1));
        }
        view.mViewBinding.emptyButton.setOnClickListener(listener);
        view.mViewData.setValue(data);
    }

    @androidx.databinding.BindingAdapter(value = {"errorImageRes", "errorTextDes", "errorButtonDes", "onErrorRetry"}, requireAll = false)
    public static void bindErrorParams(QuickDefaultEmptyView view, int imgRes, Object value, Object value1, OnClickListener listener) {
        Data data = view.mViewData.getValue();
        if (imgRes > 0) {
            data.errorImageData = imgRes;
        }
        if (getString(view.getContext(), value) != null) {
            data.errorTextDes = getString(view.getContext(), value);
        }
        if (getString(view.getContext(), value1) != null) {
            data.errorButtonDes = getString(view.getContext(), getString(view.getContext(), value1));
        }
        view.mViewBinding.errorButton.setOnClickListener(listener);
        view.mViewData.setValue(data);
    }


    private static <T> String getString(Context context, T value) {
        if (value instanceof Integer) {
            return context.getString((Integer) value);
        } else if (value instanceof String) {
            return (String) value;
        }
        return null;
    }


    public MutableLiveData<Data> getViewData() {
        return mViewData;
    }


    /**
     * The data for view displaying
     */
    public static class Data {
        public String emptyTextDes;
        public String emptyButtonDes;

        private String errorTextDes;
        public String errorButtonDes;

        public String loadingTextDes;

        public Object emptyImageData = android.R.drawable.stat_notify_error;
        public Object errorImageData = android.R.drawable.stat_notify_error;

        private Data() {
        }

        public static Data build(String loadingTextDes, String emptyTextDes, String emptyButtonDes, Object emptyImageData, String errorTextDes, String errorButtonDes, Object errorImageData) {
            Data data = new Data();
            data.loadingTextDes = loadingTextDes;
            data.emptyTextDes = emptyTextDes;
            data.emptyButtonDes = emptyButtonDes;
            data.emptyImageData = emptyImageData;
            data.errorTextDes = errorTextDes;
            data.errorButtonDes = errorButtonDes;
            data.errorImageData = errorImageData;
            return data;
        }

        public static Data build(String emptyTextDes, String emptyButtonDes, Object emptyImageData) {
            return build("加载中 ...", emptyTextDes, emptyButtonDes, emptyImageData, "发生错误，请重试", "重试", android.R.drawable.stat_notify_error);
        }

        public static Data build() {
            return build("暂无数据", "重试", android.R.drawable.stat_notify_error);
        }

        public static Data build(String emptyTextDes, String emptyButtonDes) {
            return build(emptyTextDes, emptyButtonDes, android.R.drawable.stat_notify_error);
        }

        public String getErrorTextDes(Throwable error) {
            if (TextUtils.isEmpty(this.errorTextDes)) {
                if (error != null) {
                    return error.getMessage();
                } else {
                    return "发生错误，请重试";
                }
            }
            return errorTextDes;
        }
    }


}

