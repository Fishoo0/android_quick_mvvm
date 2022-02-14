package com.common.quick.widget;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a common fragment adapter for simple fragments.
 */
public class SimpleFragmentsPagerAdapter extends FragmentStatePagerAdapter {

    protected final List<Fragment> mFragments;

    public SimpleFragmentsPagerAdapter(@NonNull FragmentManager fm, List<Fragment> list) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        if (list != null) {
            mFragments = list;
        } else {
            mFragments = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
