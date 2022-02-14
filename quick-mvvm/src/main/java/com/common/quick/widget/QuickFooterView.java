package com.common.quick.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.common.quick.BR;
import com.common.quick.databinding.QuickFooterViewBinding;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;


import java.util.List;


public class QuickFooterView extends FrameLayout implements Observable {
    private static final String TAG = QuickFooterView.class.getSimpleName();
    private ObservableField<Data> mLiveData = new ObservableField<>();

    private final QuickFooterViewBinding mViewBinding;


    public QuickFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mViewBinding = QuickFooterViewBinding.inflate(LayoutInflater.from(context), null, false);
        mViewBinding.setVariable(BR.view, this);
        addView(mViewBinding.getRoot());
    }

    public void setData(Data data) {
        if (data != null) {
            mLiveData.set(data);
        }
    }

    public ObservableField<Data> getLiveData() {
        return mLiveData;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }

    /**
     * The data for view displaying
     */
    public static class Data {
        private String text;

        private Data() {
        }

        public static Data build(String text) {
            Data data = new Data();
            data.text = text;
            return data;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    @androidx.databinding.BindingAdapter({"data"})
    public static void bindData(QuickFooterView view, Data object) {
        view.setData(object);
    }

    /**
     * The data has footer or not.
     *
     * @param list
     * @return
     */
    public static boolean hasFooter(List list) {
        if (list != null && list.size() > 0 && list.get(list.size() - 1) instanceof Data) {
            return true;
        }
        return false;
    }


    /**
     * Adding footer.
     *
     * @param list
     * @param des
     */
    public static void addFooter(List list, String des) {
        if (list != null) {
            Data data = Data.build(des);
            list.add(data);
        }
    }


    /**
     * Adding footer.
     *
     * @param list
     */
    public static void removeFooter(List list) {
        if (hasFooter(list)) {
            list.remove(list.size() - 1);
        }
    }


}

