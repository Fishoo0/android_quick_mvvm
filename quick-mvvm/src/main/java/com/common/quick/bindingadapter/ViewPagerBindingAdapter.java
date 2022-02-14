package com.common.quick.bindingadapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.common.quick.utils.QuickUtils;

import java.util.List;
import java.util.Map;

public class ViewPagerBindingAdapter {

//    @androidx.databinding.BindingAdapter(value = {"viewPager", "data"}, requireAll = true)
//    public static void bindMagicIndicatorDefaultStyleData(MagicIndicator magicIndicator, ViewPager viewPager, List<MagicNavigatorAdapterDefaultStyle.NavigatorItemData> data) {
//        if (magicIndicator != null && viewPager != null && data != null) {
//            CommonNavigator indicatorNavigator = new CommonNavigator(magicIndicator.getContext());
//            final CommonNavigatorAdapter pagerIndicatorAdapter = new MagicNavigatorAdapterDefaultStyle(viewPager, (List<MagicNavigatorAdapterDefaultStyle.NavigatorItemData>) data);
//            indicatorNavigator.setAdapter(pagerIndicatorAdapter);
//            magicIndicator.setNavigator(indicatorNavigator);
//        }
//    }
//
//    @androidx.databinding.BindingAdapter(value = {"viewPager", "data"}, requireAll = true)
//    public static <T> void bindMagicIndicatorButtonStyleData(MagicIndicator magicIndicator, ViewPager viewPager, List<MagicNavigatorAdapterButtonStyle.NavigatorItemData> data) {
//        if (magicIndicator != null && viewPager != null && data != null) {
//            CommonNavigator indicatorNavigator = new CommonNavigator(magicIndicator.getContext());
//            final CommonNavigatorAdapter pagerIndicatorAdapter = new MagicNavigatorAdapterButtonStyle(viewPager, (List<MagicNavigatorAdapterButtonStyle.NavigatorItemData>) data);
//            indicatorNavigator.setAdapter(pagerIndicatorAdapter);
//            magicIndicator.setNavigator(indicatorNavigator);
//        }
//    }
//
//    @androidx.databinding.BindingAdapter(value = {"viewPager", "data"}, requireAll = true)
//    public static <T> void bindMagicIndicatorDefaultStyleSmallData(MagicIndicator magicIndicator, ViewPager viewPager, List<MagicNavigatorAdapterDefaultSmallStyle.NavigatorItemData> data) {
//        if (magicIndicator != null && viewPager != null && data != null) {
//            CommonNavigator indicatorNavigator = new CommonNavigator(magicIndicator.getContext());
//            final CommonNavigatorAdapter pagerIndicatorAdapter = new MagicNavigatorAdapterDefaultSmallStyle(viewPager, (List<MagicNavigatorAdapterDefaultSmallStyle.NavigatorItemData>) data);
//            indicatorNavigator.setAdapter(pagerIndicatorAdapter);
//            magicIndicator.setNavigator(indicatorNavigator);
//        }
//    }
//
//    @androidx.databinding.BindingAdapter("adapter")
//    public static void setMagicIndicatorAdapter(MagicIndicator magicIndicator, MagicNavigatorAdapterDefaultStyle adapter) {
//        if (magicIndicator != null && adapter != null) {
//            CommonNavigator indicatorNavigator = new CommonNavigator(magicIndicator.getContext());
//            indicatorNavigator.setAdapter(adapter);
//            magicIndicator.setNavigator(indicatorNavigator);
//        }
//    }
//
//    @androidx.databinding.BindingAdapter("navigator")
//    public static void setMagicIndicatorNavigator(MagicIndicator magicIndicator, IPagerNavigator navigator) {
//        if (magicIndicator != null && navigator != null) {
//            magicIndicator.setNavigator(navigator);
//        }
//    }
//
//    @androidx.databinding.BindingAdapter("magicIndicator")
//    public static void bindIndicator(ViewPager viewPager, MagicIndicator indicator) {
//        if (viewPager != null && indicator != null) {
//            ViewPagerHelper.bind(indicator, viewPager);
//        }
//    }

    @androidx.databinding.BindingAdapter("fragmentAdapter")
    public static void bindFragmentAdapter(ViewPager viewPager, PagerAdapter adapter) {
        if (viewPager != null && adapter != null) {
            viewPager.setAdapter(adapter);
        }
    }

    @androidx.databinding.BindingAdapter("position")
    public static void bindPosition(ViewPager viewPager, final int position) {
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > position) {
            viewPager.setCurrentItem(position);
        }
    }

    @androidx.databinding.BindingAdapter(value = {"data", "template", "preload"})
    public static <T, K> void bindFragmentAdapter(ViewPager viewPager, List<T> data, Map<K, Class<? extends Fragment>> template, boolean preload) {
        if (viewPager != null && data != null && template != null) {
            FragmentManager fragmentManager = null;
            if (fragmentManager == null) {
                if (!QuickUtils.isViewAddedToParent(viewPager)) {
                    throw new IllegalStateException("View has not been added to parent, " +
                            "can not use this binding in xml, instead you should use app:fragmentAdapter instead.");
                }
                Object o = QuickUtils.tryFindFragmentOrActivity(viewPager);
                if (o instanceof FragmentActivity) {
                    fragmentManager = ((FragmentActivity) o).getSupportFragmentManager();
                } else if (o instanceof Fragment) {
                    fragmentManager = ((Fragment) o).getChildFragmentManager();
                } else {
                    throw new RuntimeException("Can not find FragmentManager automatic, " +
                            "can not use this binding in xml, instead you should use app:fragmentAdapter instead.");
                }
            }
            PagerAdapter adapter = new FragmentBindingPagerAdapter(fragmentManager, (List<Object>) data, (Map<Object, Class<? extends Fragment>>) template, preload);
            viewPager.setAdapter(adapter);
        }
    }
}
