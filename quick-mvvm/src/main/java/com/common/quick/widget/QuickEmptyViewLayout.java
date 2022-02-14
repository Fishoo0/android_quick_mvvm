package com.common.quick.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * Recommend using the empty view for catch empty issues.
 * <p>
 * 1, You can use attribute and data binding for easy case, like changing the default empty image or empty text.
 */
@SuppressLint("NewApi")
public class QuickEmptyViewLayout extends FrameLayout {

    private static final String TAG = QuickEmptyViewLayout.class.getSimpleName();

    private final MutableLiveData<Data> mLiveData = new MutableLiveData<>();

    public static final int FLAG_LOADING = 0x00000001;
    public static final int FLAG_EMPTY = 0x00000010;
    public static final int FLAG_ERROR = 0x00000100;
    public static final int FLAG_ALL = FLAG_LOADING | FLAG_EMPTY | FLAG_ERROR;

    private static final int EmptyViewFlag = Integer.MAX_VALUE - 521;
    private static final int EmptyViewItSelf = Integer.MAX_VALUE - 524;


    public QuickEmptyViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setTag(EmptyViewItSelf, this);
        }
    }

    public final void setData(Data data) {
        if (data != null) {
            mLiveData.setValue(data);
            updateView(data);
        }
    }

    public final Data getData() {
        return mLiveData.getValue();
    }

    public final LiveData<Data> getLiveData() {
        return mLiveData;
    }

    public void updateView(Data data) {
        if (data.isLoading()) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if ((getViewEmptyFlag(v) & FLAG_LOADING) > 0) {
                    v.setVisibility(VISIBLE);
                } else {
                    v.setVisibility(GONE);
                }
            }
        } else if (data.isEmpty()) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if ((getViewEmptyFlag(v) & FLAG_EMPTY) > 0) {
                    v.setVisibility(VISIBLE);
                } else {
                    v.setVisibility(GONE);
                }
            }
        } else if (data.isError()) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if ((getViewEmptyFlag(v) & FLAG_ERROR) > 0) {
                    v.setVisibility(VISIBLE);
                } else {
                    v.setVisibility(GONE);
                }
            }
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if ((getViewEmptyFlag(v) & (FLAG_ERROR | FLAG_LOADING | FLAG_EMPTY)) > 0) {
                    v.setVisibility(GONE);
                } else {
                    v.setVisibility(VISIBLE);
                }
            }
        }
    }

    /**
     * The data for view displaying
     */
    public static class Data {
        private boolean isEmpty = false;
        private boolean isLoading = false;
        private Object data = null;
        private Throwable error = null;

        private boolean allowEmptyRetry = true;

        private final boolean ignoreLoading;

        private Data(boolean ignoreLoading) {
            this.ignoreLoading = ignoreLoading;
        }

        public static Data build(boolean ignoreLoading) {
            return new Data(ignoreLoading);
        }

        public static Data build() {
            return new Data(false);
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public void setEmpty(boolean empty) {
            isEmpty = empty;
        }

        public boolean isLoading() {
            return !ignoreLoading && isLoading;
        }

        public void setLoading(boolean loading) {
            isLoading = loading;
        }

        /**
         * Setting error if any.
         *
         * @param error
         */
        public void setError(Throwable error) {
            this.error = error;
            setLoading(false);
        }

        public Throwable getThrowable() {
            return error;
        }

        public boolean isError() {
            return error != null;
        }

        public Object getData() {
            return data;
        }

        public boolean isReady() {
            return !isLoading() && !isError() && !isEmpty();
        }

        /**
         * Set data if success
         *
         * @param data
         */
        public void setData(Object data) {
            this.data = data;
            setLoading(false);
            setError(null);
        }

        public void setData() {
            setData(null);
        }

        public boolean isIgnoreLoading() {
            return ignoreLoading;
        }

        public boolean isAllowEmptyRetry() {
            return allowEmptyRetry;
        }

        public void setAllowEmptyRetry(boolean allowRetry) {
            this.allowEmptyRetry = allowRetry;
        }
    }


    private final static int getViewEmptyFlag(View view) {
        if (view.getTag(EmptyViewFlag) instanceof Integer) {
            return (int) view.getTag(EmptyViewFlag);
        }
        view.setTag(EmptyViewFlag, 0);
        return 0;
    }


    @androidx.databinding.BindingAdapter({"data"})
    public static void bindData(QuickEmptyViewLayout view, Data object) {
        view.setData(object);
    }

    /**
     * @param view
     * @param flag Must be #FLAG_LOADING or #FLAG_EMPTY or #FLAG_ERROR, or 0 for all.
     */
    @androidx.databinding.BindingAdapter({"emptyViewFlag"})
    public static void bindEmptyView(View view, @NonNull int flag) {
        if ((flag & (FLAG_ERROR | FLAG_LOADING | FLAG_EMPTY)) == 0) {
            flag = FLAG_ALL;
        }
        view.setTag(EmptyViewFlag, getViewEmptyFlag(view) | flag);
    }

    public static QuickEmptyViewLayout findEmptyView(View view) {
        QuickEmptyViewLayout emptyView = (QuickEmptyViewLayout) view.getTag(EmptyViewItSelf);
        return emptyView;
    }

}

