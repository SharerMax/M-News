package net.sharermax.m_news.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.MainViewPagerAdapter;
import net.sharermax.m_news.view.SlidingTabLayout;

/**
 * Author: SharerMax
 * Time  : 2015/4/16
 * E-Mail: mdcw1103@gmail.com
 */
public class SubscriptionActivity extends AbsActivity {

    public static final String CLASS_NAME = "SubscriptionActivity";
    private Toolbar mToolbar;
    private View mHeaderView;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity);
        initHeaderView();
    }

    private void initHeaderView() {
        mToolbar = (Toolbar)findViewById(R.id.header_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setCustomTabView(R.layout.sliding_tab, android.R.id.text1);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

}
