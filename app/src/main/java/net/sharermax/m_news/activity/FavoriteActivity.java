package net.sharermax.m_news.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import net.sharermax.m_news.R;

/**
 * Author: SharerMax
 * Time  : 2015/5/2
 * E-Mail: mdcw1103@gmail.com
 */
public class FavoriteActivity extends AbsActivity{
    public static final String CLASS_NAME = "FavoriteActivity";
    ObservableRecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (ObservableRecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
