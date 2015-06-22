package net.sharermax.m_news.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.sharermax.m_news.BuildConfig;
import net.sharermax.m_news.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author: SharerMax
 * Time  : 2015/6/22
 * E-Mail: mdcw1103@gmail.com
 */
public class NewsViewerActivity extends AbsActivity implements GestureDetector.OnGestureListener {
    public static final String CLASS_NAME = "NewsViewerA";
    public static final String FLAG_EXTRA_TITLE = "title";
    public static final String FLAG_EXTRA_URL = "url";
    private GestureDetector mGestureDetector;
    @InjectView(R.id.statusHeaderView)
    View mStatusHeaderView;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.web_view)
    WebView mWebView;
    @InjectView(R.id.load_progress)
    ProgressBar mLoadProgressBar;
    @InjectView(R.id.header_title)
    View mHeaderView;
    @InjectView(R.id.web_fab)
    FloatingActionButton mFAB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsviewer_activity);
        ButterKnife.inject(this);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mFAB.setVisibility(View.INVISIBLE);
        ViewCompat.setElevation(mFAB, getResources().getDimension(R.dimen.toolbar_elevation));
        mGestureDetector = new GestureDetector(this, this);
        Bundle args = getIntent().getExtras();
        String title = "";
        String url = "http://saeratom.us";
        if (args != null) {
            title = args.getString(FLAG_EXTRA_TITLE);
            url = args.getString(FLAG_EXTRA_URL);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (BuildConfig.DEBUG) {
                    Log.v(CLASS_NAME, "load url:" + url);
                }
                view.loadUrl(url);
                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (BuildConfig.DEBUG) {
                    Log.v(CLASS_NAME, "progress:" + newProgress);
                }
                mLoadProgressBar.setProgress(newProgress);
                if (100 == newProgress) mLoadProgressBar.setVisibility(View.GONE);
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.canGoBack();
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsviewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_action_refresh:
                mLoadProgressBar.setVisibility(View.VISIBLE);
                mLoadProgressBar.setProgress(0);
                mWebView.reload();
                return true;
            case R.id.menu_action_goback:
                mLoadProgressBar.setVisibility(View.VISIBLE);
                mLoadProgressBar.setProgress(0);
                mWebView.goBack();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (BuildConfig.DEBUG) {
            Log.v(CLASS_NAME, "SSE1:" + e1.getX() + "," + e1.getY());
            Log.v(CLASS_NAME, "SSE2:" + e2.getX() + "," + e2.getY());
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (BuildConfig.DEBUG) {
            Log.v(CLASS_NAME, "FFE1:" + e1.getX() + "," + e1.getY());
            Log.v(CLASS_NAME, "FFE2:" + e2.getX() + "," + e2.getY());
        }
        float x1 = e1.getX();
        float x2 = e2.getX();
        if ( x1 - x2 > 0 && !mFAB.isShown()) {
            mFAB.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            mFAB.startAnimation(animation);
        }

        if (x1 - x2 < 0 && mFAB.isShown()) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mFAB.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFAB.startAnimation(animation);
        }
        return true;
    }
}
