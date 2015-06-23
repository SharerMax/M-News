package net.sharermax.m_news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import net.sharermax.m_news.BuildConfig;
import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.DatabaseAdapter;


import java.lang.ref.WeakReference;

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
    private MenuItem mGobackMenuItem;
    private String mTitle;
    private String mUrl;
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
    FloatingActionsMenu mFAB;
    @InjectView(R.id.web_fab_share)
    FloatingActionButton mWebFabShare;
    @InjectView(R.id.web_fab_fav)
    FloatingActionButton mWebFabFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsviewer_activity);
        ButterKnife.inject(this);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mGestureDetector = new GestureDetector(this, this);
        initWebView();
        initFAB();
    }

    private void initWebView() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mTitle = args.getString(FLAG_EXTRA_TITLE);
            mUrl = args.getString(FLAG_EXTRA_URL);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mTitle);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (BuildConfig.DEBUG) {
                    Log.v(CLASS_NAME, "load url:" + url);
                }
                view.loadUrl(url);
                if (mWebView.canGoBack() && null != mGobackMenuItem) {
                    mGobackMenuItem.setEnabled(true);
                }
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
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });
        mWebView.loadUrl(mUrl);
    }

    private void initFAB() {
        mFAB.setVisibility(View.INVISIBLE);
        mWebFabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BuildConfig.DEBUG) {
                    Log.v(CLASS_NAME, "mWebFabFav clicked");
                }
                mFAB.toggle();
                final DataBasHandler handler = new DataBasHandler(NewsViewerActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance(NewsViewerActivity.this);
                        databaseAdapter.open();
                        DatabaseAdapter.NewsDataRecord record = new DatabaseAdapter.NewsDataRecord();
                        record.title = mTitle;
                        record.url = mUrl;
                        record.time = System.currentTimeMillis();
                        databaseAdapter.setItemRecord(record);
                        Message message = handler.obtainMessage();
                        if (databaseAdapter.isExist()) {
                            message.what = DataBasHandler.FLAG_HANDLER_EXIST;
                            handler.sendMessage(message);
                        } else if (databaseAdapter.insert() == -1){
                            message.what = DataBasHandler.FLAG_HANDLER_NO;
                            handler.sendMessage(message);
                        } else {
                            message.what = DataBasHandler.FLAG_HANDLER_OK;
                            handler.sendMessage(message);
                        }
                    }
                }).start();

            }
        });
        mWebFabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BuildConfig.DEBUG) {
                    Log.v(CLASS_NAME, "mWebFabShare clicked");
                }
                mFAB.toggle();
                Intent intent = new Intent(NewsViewerActivity.this, EditWeiboActivity.class);
                intent.putExtra(EditWeiboActivity.EXTRA_FLAG, mTitle + mUrl);
                NewsViewerActivity.this.startActivity(intent);
            }
        });
    }

    public void sendToast(String message) {
        if (BuildConfig.DEBUG) {
            Log.v(CLASS_NAME, message);
        }
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private static class DataBasHandler extends Handler {
        private WeakReference<NewsViewerActivity> wf;
        public static final int FLAG_HANDLER_OK = 1;
        public static final int FLAG_HANDLER_NO = 2;
        public static final int FLAG_HANDLER_EXIST = 3;
        public DataBasHandler(NewsViewerActivity activity){
            wf = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewsViewerActivity a = wf.get();
            switch (msg.what) {
                case FLAG_HANDLER_OK:
                    if (null != a) {
                        a.sendToast(a.getString(R.string.nva_fav_success_message));
                    }
                    break;
                case FLAG_HANDLER_EXIST:
                    if (null != a) {
                        a.sendToast(a.getString(R.string.nva_fav_exist_message));
                    }
                    break;
                case FLAG_HANDLER_NO:
                    if (null != a) {
                        a.sendToast(a.getString(R.string.nva_fav_fail_message));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsviewer_menu, menu);
        mGobackMenuItem = menu.findItem(R.id.menu_action_goback);
        mGobackMenuItem.setEnabled(false);
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
                if (mWebView.canGoBack()) {
                    mLoadProgressBar.setVisibility(View.VISIBLE);
                    mLoadProgressBar.setProgress(0);
                    mWebView.goBack();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (mFAB.isExpanded()) {
            mFAB.collapse();
        }
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
        float y1 = e1.getY();
        float y2 = e2.getY();
        if (y1 - y2 > 0 && !mFAB.isShown()) {
            mFAB.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            mFAB.startAnimation(animation);
        }

        if (y1 - y2 < 0 && mFAB.isShown()) {
            if (mFAB.isExpanded()) {
                mFAB.collapse();
            }
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
