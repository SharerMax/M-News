package net.sharermax.m_news.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import net.sharermax.m_news.R;
import net.sharermax.m_news.fragment.HomeFragment;
import net.sharermax.m_news.fragment.NavigationDrawerFragment;
import net.sharermax.m_news.support.Setting;

/**
 * Author: SharerMax
 * Time  : 2015/2/19
 * E-Mail: mdcw1103@gmail.com
 */

public class MainActivity extends AbsActivity implements NavigationDrawerFragment.OnFragmentInteractionListener{

    public static final String CLASS_NAME = "MainActivty";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private HomeFragment mHomeFragment;
    private boolean mDoubleClickToTopEnable;
    private long mDoubleClickSpeed = 200;  //time
    private long mPreDoubleClickTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mToolBar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDoubleClickToTopEnable) {
                    if ((System.currentTimeMillis() - mPreDoubleClickTime) > mDoubleClickSpeed) {
                        mPreDoubleClickTime = System.currentTimeMillis();
                    } else {
                        mHomeFragment.scrollToTop();
                    }
                }
            }
        });
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setUpDrawer();
    }

    private void setUpDrawer() {
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


    @Override
    public void onFragmentInteraction(int clickedItemPosition) {

        FragmentManager fragmentManager = getFragmentManager();
        switch (clickedItemPosition) {
            case NavigationDrawerFragment.LISTVIEW_ITEM_HOME:
                if (null == mHomeFragment) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                fragmentManager.beginTransaction().replace(R.id.container, mHomeFragment).commit();
                mDoubleClickToTopEnable =
                        Setting.getInstance(getApplicationContext()).getBoolen(Setting.KEY_DOUBLE_TO_TOP, true);
                break;
            case NavigationDrawerFragment.LISTVIEW_ITEM_SUBSCRIPTION:
                mDoubleClickToTopEnable = false;
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
}
