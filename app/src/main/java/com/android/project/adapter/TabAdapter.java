package com.android.project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.project.login.LogInActivity;

import java.util.List;

/**
 * Created by Lobster on 21.05.16.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTabNames;

    public TabAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> tabNames){
        super(fragmentManager);
        mFragments = fragments;
        mTabNames = tabNames;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames.get(position);
    }

}
