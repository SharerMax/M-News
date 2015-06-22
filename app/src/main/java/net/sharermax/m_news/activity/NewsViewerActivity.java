package net.sharermax.m_news.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
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
public class NewsViewerActivity extends AbsActivity {
    public static final String CLASS_NAME = "NewsViewerA";
    public static final String FLAG_EXTRA_TITLE = "title";
    public static final String FLAG_EXTRA_URL = "url";

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsviewer_activity);
        ButterKnife.inject(this);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        Bundle args = getIntent().getExtras();
        String title = "TTTTT";
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
                mWebView.loadUrl(url);
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

        mWebView.loadUrl(url);
    }
}
