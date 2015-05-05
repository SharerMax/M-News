package net.sharermax.m_news.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.Setting;
import net.sharermax.m_news.support.Utility;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Author: SharerMax
 * Time  : 2015/3/20
 * E-Mail: mdcw1103@gmail.com
 */
public class AbsActivity extends AppCompatActivity implements SwipeBackActivityBase{
    public static final String CLASS_NAME = "AbsActivity";
    private SwipeBackActivityHelper mHelper;
    protected int mStatusBarHeight = 0;
    private Toolbar mToolBar;
    private View mStatusHeaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = Utility.getStatusBarHeight(getApplication());
        }

        boolean swipeBack = Setting.getInstance(this).getBoolen(Setting.KEY_SWIPE_BACK, true);
        setSwipeBackEnable(swipeBack);
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


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    public View getmStatusHeaderView() {
        return mStatusHeaderView;
    }
}
