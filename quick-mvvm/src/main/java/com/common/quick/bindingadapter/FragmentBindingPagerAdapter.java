package com.common.quick.bindingadapter;

import android.os.Bundle;

import com.common.quick.utils.QuickUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a common fragment adapter for simple fragments.
 */
public class FragmentBindingPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Object> mList = new ArrayList<>();
    private final Map<Integer, Fragment> mFragments = new HashMap<>();

    interface IFragmentCreator {
        Fragment createFragment(Object object);
    }

    private final IFragmentCreator mFragmentCreator;

    public FragmentBindingPagerAdapter(@NonNull FragmentManager fm, List<Object> data, Map<Object, Class<? extends Fragment>> template, boolean preloadAll) {
        super(fm);
        if (template == null || template.size() <= 0) {
            throw new IllegalArgumentException("template must not be null or size == 0 !");
        }
        mFragmentCreator = new IFragmentCreator() {
            @Override
            public Fragment createFragment(Object object) {
                if (template.containsKey(object.getClass())) {
                    return FragmentBindingPagerAdapter.createFragment(template.get(object.getClass()), object);
                } else if (template.containsKey(object)) {
                    return FragmentBindingPagerAdapter.createFragment(template.get(object), object);
                } else {
                    throw new RuntimeException("Unsupported object " + object + ", can not find object or " + object.getClass() + " in template -> " + template);
                }
            }
        };
        setData(data, preloadAll);
    }


    public FragmentBindingPagerAdapter(@NonNull FragmentManager fm, List<Fragment> list) {
        super(fm);
        if (list == null) {
            list = new ArrayList<>();
        }
        mFragmentCreator = null;
        mFragments.clear();
        for (int i = 0; i < list.size(); i++) {
            mFragments.put(i, list.get(i));
        }
    }

    public void setData(List<Object> list) {
        setData(list, false);
    }

    public void setData(List<Object> list, boolean preloadAll) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mList.clear();
        mFragments.clear();
        mList.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            mFragments.put(i, preloadAll ? mFragmentCreator.createFragment(list.get(i)) : null);
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mFragments.get(position) == null) {
            mFragments.put(position, mFragmentCreator.createFragment(mList.get(position)));
        }
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    private static final Fragment createFragment(Class<? extends Fragment> fClz, Object data) {
        try {
            Fragment f = fClz.newInstance();
            Bundle bundle = new Bundle();
            QuickUtils.putExtra(bundle, "data", data);
            f.setArguments(bundle);
            return f;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
