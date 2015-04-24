package net.sharermax.m_news.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import net.sharermax.m_news.R;
import net.sharermax.m_news.fragment.NewsFragment;
import net.sharermax.m_news.support.Setting;

import java.util.ArrayList;

/**
 * Author: SharerMax
 * Time  : 2015/4/15
 * E-Mail: mdcw1103@gmail.com
 */
public class MainViewPagerAdapter extends CacheFragmentStatePagerAdapter{

    public static final String CLASS_NAME = "MainViewPagerAdapter";
    private static final String [] TITLE = new String[] {"STARTUP", "GEEK"};
    private ArrayList<String> mTitles;
    private Context mContext;
    private int mScrollY;
    private Setting mSetting;
    private FragmentManager mFm;
    public MainViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mFm = fm;
        mContext = context;
        mSetting = Setting.getInstance(mContext);
        mTitles = new ArrayList<String>();
        initTitles();
    }

    private void initTitles() {
        if (!mTitles.isEmpty()) {
            mTitles.clear();
        }
        if (mSetting.getBoolen(Setting.KEY_SUB_STARTUP, true)) {
            mTitles.add(mContext.getString(R.string.title_startup));
        }
        if (mSetting.getBoolen(Setting.KEY_SUB_CSDNGEEK, true)) {
            mTitles.add(mContext.getString(R.string.title_csdn_geek));
        }
    }

    public void setScrollY(int scrollY) {
        mScrollY = scrollY;
    }

    @Override
    protected Fragment createItem(int position) {
        Fragment fragment = new NewsFragment();
        Log.v(CLASS_NAME, "" + mScrollY);
        if (0 < mScrollY) {
            Bundle args = new Bundle();
            args.putInt(NewsFragment.FLAG_INITIAL_POSITION, 1);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        initTitles();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        Log.v(CLASS_NAME, "Item Position");
        return PagerAdapter.POSITION_NONE;
    }
}
