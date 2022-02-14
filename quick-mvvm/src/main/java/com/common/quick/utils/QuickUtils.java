package com.common.quick.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.common.quick.mvvm.QuickBaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuickUtils {

    public static void putExtra(Bundle bundle, String key, Object object) {
        if (object instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) object);
        } else if (object instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) object);
        } else if (object instanceof String) {
            bundle.putString(key, (String) object);
        } else if (object instanceof Integer) {
            bundle.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) object);
        } else if (object instanceof Double) {
            bundle.putDouble(key, (Double) object);
        } else {
            throw new UnsupportedOperationException("This object type has not been implemented ! " + object);
        }
    }


    /**
     * @param view
     * @return
     */
    public static final Object tryFindFragmentOrActivity(View view) {
        // try to find activity or fragment
        Activity activity = (Activity) view.getContext();
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
            List<Fragment> fragmentList = new ArrayList<>();
            for (Fragment fragment : fragments) {
                collectFragments(fragment, fragmentList);
            }
            fragments.addAll(fragmentList);
            for (Fragment fragment : fragments) {
                if (fragment instanceof QuickBaseFragment) {
                    View rootView = fragment.getView();
                    if (matchParentChild(rootView, view)) {
                        return fragment;
                    }
                }
            }
        }
        return activity;
    }

    private static final void collectFragments(Fragment fragment, List<Fragment> list) {
        List<Fragment> fragments = fragment.getChildFragmentManager().getFragments();
        for (Fragment f : fragments) {
            list.add(f);
            collectFragments(f, list);
        }
    }


    public static final boolean matchParentChild(View parent, View child) {
        ViewParent vp = child.getParent();
        while (vp instanceof ViewGroup) {
            if (vp == parent) {
                return true;
            }
            vp = vp.getParent();
        }
        return false;
    }

    /**
     * Whether the recycle view has been added to parent or parent recycle view.
     *
     * @param child
     * @return
     */
    public static final boolean isViewAddedToParent(View child) {
        ViewParent vp = child.getParent();
        while (vp instanceof ViewParent) {
            if (vp.getClass().getName().equals("com.android.internal.policy.DecorView") || vp.getClass().getName().equals("android.view.ViewRootImpl")) {
                return true;
            }
            vp = vp.getParent();
        }
        return false;
    }

    @androidx.databinding.BindingAdapter("visibleOrGone")
    public static void setVisibleOrGone(View view, Boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    public static void setLifeCycleOwner(ViewDataBinding binding, View view) {
        if (binding.getLifecycleOwner() == null) {
            Object lifecycle = QuickUtils.tryFindFragmentOrActivity(view);
            if (!(lifecycle instanceof LifecycleOwner)) {
                lifecycle = view.getContext();
            }
            binding.setLifecycleOwner((LifecycleOwner) lifecycle);
        }
    }


    public static void setLifeCycleOwner(ViewDataBinding binding) {
        setLifeCycleOwner(binding, binding.getRoot());
    }

}
