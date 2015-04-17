package net.sharermax.m_news.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import net.sharermax.m_news.fragment.HomeFragment;
import net.sharermax.m_news.fragment.NewsFragment;
import net.sharermax.m_news.fragment.SubscriptionFragment;

/**
 * Author: SharerMax
 * Time  : 2015/4/15
 * E-Mail: mdcw1103@gmail.com
 */
public class MainViewPagerAdapter extends CacheFragmentStatePagerAdapter{

    public static final String CLASS_NAME = "MainViewPagerAdapter";
    private static final String [] TITLE = new String[] {"STARTUP", "GEEK"};
    private int mScrollY;
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setScrollY(int scrollY) {
        mScrollY = scrollY;
    }

    @Override
    protected Fragment createItem(int position) {
        Fragment fragment = new NewsFragment();
        Log.v(CLASS_NAME, "" + mScrollY);
        if (true) {
            Bundle args = new Bundle();
            args.putInt(NewsFragment.FLAG_INITIAL_POSITION, 1);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }


}
