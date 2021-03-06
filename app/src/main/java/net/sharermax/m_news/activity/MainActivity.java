package net.sharermax.m_news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.DatabaseAdapter;
import net.sharermax.m_news.adapter.MainViewPagerAdapter;
import net.sharermax.m_news.fragment.NavigationDrawerFragment;
import net.sharermax.m_news.fragment.NewsFragment;
import net.sharermax.m_news.support.CrashHandler;
import net.sharermax.m_news.view.SlidingTabLayout;

/**
 * Author: SharerMax
 * Time  : 2015/2/19
 * E-Mail: mdcw1103@gmail.com
 */

public class MainActivity extends AbsActivity
        implements NavigationDrawerFragment.OnFragmentInteractionListener, ObservableScrollViewCallbacks{

    public static final String CLASS_NAME = "MainActivty";
    public static final int FLAG_ACTIVITY_SUB = 0;
    public static final int FLAG_ACTIVITY_SET = 1;
    public static final int FLAG_ACTIVITY_ACC = 2;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private View mHeaderView;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private int mBaseTranslationY;
    private MainViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        initCrashHandler();
        setContentView(R.layout.activity_main);
        initDrawerLayout();
        initHeaderView();

    }
    private void initCrashHandler() {
        CrashHandler.init(getApplicationContext());
        CrashHandler.register();
    }
    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }
    private void initHeaderView() {
        mToolbar = (Toolbar)findViewById(R.id.header_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        mViewPagerAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setCustomTabView(R.layout.sliding_tab, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.red_500));
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.v(CLASS_NAME, "scrolled");
            }

            @Override
            public void onPageSelected(int position) {
                propagateToolbarState(toolbarIsShown());
//                Log.v(CLASS_NAME, "selected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.v(CLASS_NAME, "changed");
            }
        });
    }

    @Override
    public void onFragmentInteraction(int clickedItemPosition) {

        switch (clickedItemPosition) {
            case NavigationDrawerFragment.LISTVIEW_POSITION_HOME:
                if (null != mViewPager) {
                    mViewPager.setCurrentItem(0);
                }
                break;
            case NavigationDrawerFragment.LISTVIEW_POSITION_FAVORITE:
                Intent favIntent = new Intent();
                favIntent.setClass(this, FavoriteActivity.class);
                startActivity(favIntent);
                overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case NavigationDrawerFragment.LISTVIEW_POSITION_SUBSCRIPTION:
                Intent subIntent = new Intent();
                subIntent.setClass(this, SubscriptionActivity.class);
                startActivityForResult(subIntent, FLAG_ACTIVITY_SUB);
                overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case NavigationDrawerFragment.LISTVIEW_POSITION_SETTING:
                Intent settingIntent = new Intent();
                settingIntent.setClass(this, SettingsActivity.class);
                startActivityForResult(settingIntent, FLAG_ACTIVITY_SET);
                overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case NavigationDrawerFragment.LISTVIEW_POSITION_ACCOUNT:
                Intent accountIntent = new Intent();
                accountIntent.setClass(this, AccountBindActivity.class);
                startActivityForResult(accountIntent, FLAG_ACTIVITY_ACC);
                overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            default:
                break;
        }
        if (null != mDrawerLayout) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
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
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
        return true;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbar.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        // ObservableXxxViews have same API
        // but currently they don't have any common interfaces.
        adjustToolbar(scrollState, view);
    }

    private void adjustToolbar(ScrollState scrollState, View view) {
        int toolbarHeight = mToolbar.getHeight();
        final Scrollable scrollView = (Scrollable) view.findViewById(R.id.news_recyclerview);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mViewPagerAdapter.getItemAt(mViewPager.getCurrentItem());
    }

    private void propagateToolbarState(boolean isShown) {
        int toolbarHeight = mToolbar.getHeight();

        // Set scrollY for the fragments that are not created yet
        mViewPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mViewPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mViewPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            propagateToolbarState(isShown, view, toolbarHeight);
        }
    }

    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
        Scrollable scrollView = (Scrollable) view.findViewById(R.id.news_recyclerview);
        if (scrollView == null) {
            return;
        }
        if (isShown) {
            // Scroll up
            if (0 < scrollView.getCurrentScrollY()) {
                scrollView.scrollVerticallyTo(0);
            }
        } else {
            // Scroll down (to hide padding_toolbar)
            if (scrollView.getCurrentScrollY() < toolbarHeight) {
                scrollView.scrollVerticallyTo(toolbarHeight);
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbar.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator animator = ViewPropertyAnimator.animate(mHeaderView);
            animator.setDuration(200);
            animator.translationY(0);
            animator.setInterpolator(new DecelerateInterpolator(2));
            animator.start();
//            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbar.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator animator = ViewPropertyAnimator.animate(mHeaderView);
            animator.setDuration(200);
            animator.translationY(-toolbarHeight);
            animator.setInterpolator(new DecelerateInterpolator(2));
            animator.start();
//            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
        propagateToolbarState(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG_ACTIVITY_SUB:
                if (Activity.RESULT_OK == resultCode) {
                    mViewPagerAdapter.notifyDataSetChanged();
                    mSlidingTabLayout.setViewPager(mViewPager);
                }
                break;
            case FLAG_ACTIVITY_SET:
                if (Activity.RESULT_OK == resultCode) {
                    for (int i =0; i<mViewPagerAdapter.getCount(); i++) {
                        Fragment fragment = mViewPagerAdapter.getItemAt(i);
                        if (null != fragment) {
                            try {
                                ((NewsFragment)fragment).updateItemView();
                                ((NewsFragment) fragment).refeshData();
                                if (toolbarIsHidden()) {
                                    showToolbar();
                                }
                            } catch (ClassCastException e) {
                                Log.v(CLASS_NAME, e.toString());
                            }
                        }
                    }
                }
                break;
            case FLAG_ACTIVITY_ACC:
                if (Activity.RESULT_OK == resultCode) {
                    NavigationDrawerFragment fragment =
                            (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigationDrawer_fragment);
                    fragment.updateProfileImage();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        enableOverrideFinishAnimation(false);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        DatabaseAdapter.close();

        super.onDestroy();
    }
}
