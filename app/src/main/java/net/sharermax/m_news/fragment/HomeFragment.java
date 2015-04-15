package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.AbsActivity;
import net.sharermax.m_news.adapter.HomeViewPagerAdapter;
import net.sharermax.m_news.view.SlidingTabLayout;

/**
 * Author: SharerMax
 * Time  : 2015/4/15
 * E-Mail: mdcw1103@gmail.com
 */
public class HomeFragment extends Fragment{
    public static final String CLASS_NAME = "HomeFragment";
    private AbsActivity mAbsActivity;
    private Toolbar mToolbar;
    private View mRootView;
    private ViewPager mViewPager;
    private HomeViewPagerAdapter mHomeViewPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private View mHeader;
    private int mHeaderDistance = 0;
    private final int HIDE_HOLD = 20;
    private boolean mHeaderVisible = true;
    public HomeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.home_fragment, container, false);
        initHeader();
        initViewPager();
        initSlidingLayout();
        return mRootView;
    }

    private void initHeader() {
        mToolbar = (Toolbar)mRootView.findViewById(R.id.toolbar);
        mAbsActivity.setSupportActionBar(mToolbar);
        mAbsActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHeader = mRootView.findViewById(R.id.header);
        ViewCompat.setElevation(mHeader, getResources().getDimension(R.dimen.toolbar_elevation));
    }

    private void initViewPager() {
        mViewPager = (ViewPager)mRootView.findViewById(R.id.view_pager);
        mHomeViewPagerAdapter = new HomeViewPagerAdapter(this, mAbsActivity.getSupportFragmentManager());
        mViewPager.setAdapter(mHomeViewPagerAdapter);
    }

    private void initSlidingLayout() {
        mSlidingTabLayout = (SlidingTabLayout)mRootView.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.sliding_tab, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.red_500));
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAbsActivity = (AbsActivity)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must extents AbsActivity");
        }
    }
    public void onNewsScrolled(int dy, boolean firstVisible) {
        Log.v(CLASS_NAME, "y:" + dy);
        if (firstVisible) {
            if (!mHeaderVisible) {
                mHeader.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                mHeaderVisible = true;
            }
        } else {
            if (mHeaderDistance > HIDE_HOLD && mHeaderVisible) {
                mHeader.animate().translationY(-mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
                mHeaderVisible = false;
                mHeaderDistance = 0;
            } else if (mHeaderDistance < -HIDE_HOLD && !mHeaderVisible) {
                mHeader.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                mHeaderVisible = true;
                mHeaderDistance = 0;
            }
        }

        if ((mHeaderVisible && dy > 0) || (!mHeaderVisible && dy < 0)) {
            mHeaderDistance += dy;
        }
    }
}
