package net.sharermax.m_news.activity;

import android.os.Bundle;
import android.view.MenuItem;

import net.sharermax.m_news.R;

/**
 * Author: SharerMax
 * Time  : 2015/3/31
 * E-Mail: mdcw1103@gmail.com
 */
public class WeiboAuthActivity extends AbsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_auth_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
