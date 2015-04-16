package net.sharermax.m_news.activity;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.views.ScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.MainViewPagerAdapter;
import net.sharermax.m_news.fragment.NewsFragment;
import net.sharermax.m_news.fragment.NavigationDrawerFragment;
import net.sharermax.m_news.fragment.HomeFragment;
import net.sharermax.m_news.support.Setting;
import net.sharermax.m_news.view.SlidingTabLayout;

/**
 * Author: SharerMax
 * Time  : 2015/2/19
 * E-Mail: mdcw1103@gmail.com
 */

public class MainActivity extends AbsActivity
        implements NavigationDrawerFragment.OnFragmentInteractionListener, ObservableScrollViewCallbacks{

    public static final String CLASS_NAME = "MainActivty";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private View mHeaderView;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private int mBaseTranslationY;
    private NewsFragment mNewsFragment;
    private HomeFragment mHomeFragment;
    private boolean mDoubleClickToTopEnable;
    private long mDoubleClickSpeed = 200;  //time
    private long mPreDoubleClickTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mToolBar.inflateMenu(R.menu.menu_main);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getToolBar().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDoubleClickToTopEnable) {
//                    if ((System.currentTimeMillis() - mPreDoubleClickTime) > mDoubleClickSpeed) {
//                        mPreDoubleClickTime = System.currentTimeMillis();
//                    } else {
//                        mHomeFragment.scrollToTop();
//                    }
//                }
//            }
//        });
        initDrawerLayout();
        initHeaderView();

    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
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
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.red_500));
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onFragmentInteraction(int clickedItemPosition) {

        switch (clickedItemPosition) {
            case NavigationDrawerFragment.LISTVIEW_ITEM_HOME:

                if (null != mViewPager) {
                    mViewPager.setCurrentItem(0);
                }
                break;
            case NavigationDrawerFragment.LISTVIEW_ITEM_SUBSCRIPTION:
                Intent subIntent = new Intent();
                subIntent.setClass(this, SubscriptionActivity.class);
                startActivity(subIntent);
                break;
            case NavigationDrawerFragment.LISTVIEW_ITEM_SETTING:
                Intent settingIntent = new Intent();
                settingIntent.setClass(this, SettingsActivity.class);
                startActivity(settingIntent);
                mDoubleClickToTopEnable = false;
                break;
            case NavigationDrawerFragment.LISTVIEW_ITEM_ACCOUNT:
                Intent accountIntent = new Intent();
                accountIntent.setClass(this, AccountBindActivity.class);
                startActivity(accountIntent);
                break;
            default:
                break;
        }
        if (null != mDrawerLayout) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
                break;
        }
        return true;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        Log.v(CLASS_NAME, "" + scrollY);
        mHeaderView.animate().translationY(-mToolbar.getHeight());
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;
    }
}
