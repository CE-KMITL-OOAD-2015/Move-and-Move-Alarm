package com.fatel.mamtv1;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ScoreboardPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_NUM = 2;
    private static final String[] CONTENT = new String[] { "User","Group" };
    public ScoreboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount() {
        return PAGE_NUM;
    }

    public Fragment getItem(int position) {
        if(position == 0)
            return ScoreboardUserFragment.newInstance();
        else if(position == 1)
            return ScoreboardGroupFragment.newInstance();
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }
}