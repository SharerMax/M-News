package net.sharermax.m_news.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.Utility;

/**
 * Author: SharerMax
 * Time  : 2015/3/20
 * E-Mail: mdcw1103@gmail.com
 */
public class AbsActivity extends ActionBarActivity{
    public static final String CLASS_NAME = "AbsActivity";
    protected int mStatusBarHeight = 0;
    private Toolbar mToolBar;
    private View mStatusHeaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = Utility.getStatusBarHeight(getApplication());
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mStatusBarHeight = 0;
//        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        try {
            mStatusHeaderView = findViewById(R.id.statusHeaderView);
            mStatusHeaderView.getLayoutParams().height = mStatusBarHeight;
        } catch (NullPointerException e) {
            Log.v(CLASS_NAME, "Don't find statusHeaderView");
        }

        try {
            mToolBar = (Toolbar)findViewById(R.id.toolbar);
            ViewCompat.setElevation(mToolBar, getResources().getDimension(R.dimen.toolbar_elevation));
            setSupportActionBar(mToolBar);
        } catch (NullPointerException e) {
            Log.v(CLASS_NAME, "Don't find Toolbar");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    public View getmStatusHeaderView() {
        return mStatusHeaderView;
    }
}
