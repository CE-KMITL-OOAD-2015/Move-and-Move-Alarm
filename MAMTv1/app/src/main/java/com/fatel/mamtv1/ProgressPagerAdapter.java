package com.fatel.mamtv1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kid14 on 11/17/2015.
 */
public class ProgressPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_NUM = 2;
    private static final String[] CONTENT = new String[] { "Activity","Event" };
    public ProgressPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount() {
        return PAGE_NUM;
    }

    public Fragment getItem(int position) {
        if(position == 0)
            return ProgressActivityFragment.newInstance();
        else if(position == 1)
            return ProgressEventFragment.newInstance();
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }
}