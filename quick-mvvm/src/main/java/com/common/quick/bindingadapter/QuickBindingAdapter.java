package com.common.quick.bindingadapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

public class QuickBindingAdapter {

    @androidx.databinding.BindingAdapter(value = {"onClick"})
    public static void quickOnClick(View view, View.OnClickListener listener) {
        view.setClickable(true);
        view.setOnClickListener(listener);
    }


    @androidx.databinding.BindingAdapter(value = {"android:src"})
    public static <T> void bindAndroidSrc(ImageView imageView, T data) {
        if (data instanceof String) {
//            ImageLoaderUtil
//                .getInstance()
//                .loadRoundImage(imageView.getContext(), (String) data, R.drawable.img_party_logo, imageView, 6);
        } else if (data instanceof Integer) {
            imageView.setImageResource((Integer) data);
        } else if (data instanceof Drawable) {
            imageView.setImageDrawable((Drawable) data);
        }
    }

    @androidx.databinding.BindingAdapter(value = {"quickSrc"})
    public static <T> void bingQuickSrc(ImageView imageView, T data) {
        bindAndroidSrc(imageView, data);
    }

    @androidx.databinding.BindingAdapter(value = {"quickCircleSrc"})
    public static void bingQuickCircleSrc(ImageView imageView, String data) {
//        ImageLoaderUtil
//            .getInstance()
//            .loadCircleImage(imageView.getContext(), Strings.isEmpty(data) ? "" : data, R.drawable.img_people, imageView);
    }

    @androidx.databinding.BindingAdapter(value = {"quickRoundSrc", "radius"})
    public static void bingQuickRoundSrc(ImageView imageView, String data, int radius) {
//        ImageLoaderUtil
//            .getInstance()
//            .loadRoundImage(imageView.getContext(), Strings.isEmpty(data) ? "" : data, R.drawable.img_party_logo, imageView, AutoInjectContextUtil.dip2px(radius));
    }

//    @androidx.databinding.BindingAdapter(value = {"onRefresh"})
//    public static void quickSetRefreshListener(SmartRefreshLayout refreshLayout, OnRefreshListener listener) {
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                listener.onRefresh(refreshLayout);
//            }
//        });
//    }
//
//    @androidx.databinding.BindingAdapter(value = {"onLoadMore"})
//    public static void quickSetLoadMoreListener(SmartRefreshLayout refreshLayout, OnLoadMoreListener listener) {
//        refreshLayout.setEnableLoadMore(true);
//        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);
////        refreshLayout.setEnableAutoLoadMore(true);
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                listener.onLoadMore(refreshLayout);
//            }
//        });
//    }
//
//    @androidx.databinding.BindingAdapter(value = {"statusData"})
//    public static void quickSetStatus(SmartRefreshLayout refreshLayout, Object object) {
//        if (object instanceof QuickResource) {
//            if (((QuickResource) object).getLoading()) {
//                return;
//            }
//        }
//        refreshLayout.finishRefresh(10);
//        refreshLayout.finishLoadMore(10);
//    }

    @androidx.databinding.BindingAdapter(value = {"android:selected"})
    public static void quickViewSelected(View view, boolean selected) {
        view.setSelected(selected);
    }

    @androidx.databinding.BindingAdapter(value = {"android:enabled"})
    public static void bindViewEnabled(View view, boolean value) {
        view.setEnabled(value);
    }

    @androidx.databinding.BindingAdapter(value = {"checkedChangeListener"})
    public static void bindRadioButtonOnCheckedChangeListener(RadioButton button, CompoundButton.OnCheckedChangeListener listener) {
        button.setOnCheckedChangeListener(listener);
    }

    @androidx.databinding.BindingAdapter(value = {"debugTag", "debugData"})
    public static void bindDebug(View view, String tag, Object data) {
        Log.e(tag, "debugData: " + data);
    }

    @androidx.databinding.BindingAdapter(value = {"initVisibility"})
    public static void bindInitVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }

    @androidx.databinding.BindingAdapter(value = {"bindFixedHeight"})
    public static void bindFixedHeight(View view, float fixedHeight) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) fixedHeight;
        view.setLayoutParams(params);
    }

    @androidx.databinding.BindingAdapter(value = {"bindFixedWidth"})
    public static void bindFixedWidth(View view, float fixedWidth) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) fixedWidth;
        view.setLayoutParams(params);
    }

    @androidx.databinding.BindingAdapter(value = {"imageBitmap"})
    public static void setImageBitmap(ImageView imageView, Bitmap imageBitmap) {
        imageView.setImageBitmap(imageBitmap);
    }


//    @JvmStatic
//    @BindingAdapter("enableLoadMore")
//    public static void setEnableLoadMore(SmartRefreshLayout smartRefreshLayout, Boolean enableLoadMore) {
//        smartRefreshLayout.setEnableLoadMore(enableLoadMore);
//    }
//
//    @JvmStatic
//    @BindingAdapter("completeRefresh")
//    public static void setCompleteRefresh(SmartRefreshLayout smartRefreshLayout, Boolean completeRefresh) {
//        if (completeRefresh) {
//            smartRefreshLayout.finishRefresh();
//        }
//    }
//
//    @JvmStatic
//    @BindingAdapter("completeLoadMore")
//    public static void setCompleteLoadMore(SmartRefreshLayout smartRefreshLayout, Boolean completeLoadMore) {
//        if (completeLoadMore) {
//            smartRefreshLayout.finishLoadMore();
//        }
//    }
}
