package net.sharermax.m_news.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.DatabaseAdapter;
import net.sharermax.m_news.adapter.FavoriteViewAdapter;
import net.sharermax.m_news.support.Setting;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/5/2
 * E-Mail: mdcw1103@gmail.com
 */
public class FavoriteActivity extends AbsActivity{
    public static final String CLASS_NAME = "FavoriteActivity";
    ObservableRecyclerView mRecyclerView;
    List<DatabaseAdapter.NewsDataRecord> mDataList;
    FavoriteViewAdapter mAdapter;
    private Handler dbHandler;

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
        boolean mUseCardStyle = Setting.getInstance(this).getBoolen(Setting.KEY_USE_CARD_VIEW, true);
        mDataList = new ArrayList<>();
        mAdapter = new FavoriteViewAdapter(mDataList, mUseCardStyle);
        mAdapter.setDataBaseAdapter(DatabaseAdapter.getInstance(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
        dbHandler = new DbHandler(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseAdapter dbAdapter = DatabaseAdapter.getInstance(getApplicationContext());
                if (!dbAdapter.isOpen()) {
                    dbAdapter.open();
                }
                List<DatabaseAdapter.NewsDataRecord> dataList = dbAdapter.queryAllData();
                Message msg = dbHandler.obtainMessage();
                msg.obj = dataList;
                dbHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateData(List<DatabaseAdapter.NewsDataRecord> dataList) {
        mDataList.addAll(mAdapter.getItemCount(), dataList);
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), dataList.size());
    }

    private static class DbHandler extends Handler {
        private WeakReference<FavoriteActivity> mActivity;
        public DbHandler(FavoriteActivity activity) {
            mActivity = new WeakReference<FavoriteActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<DatabaseAdapter.NewsDataRecord> list = (List<DatabaseAdapter.NewsDataRecord>)msg.obj;
            if (!list.isEmpty()) {
                FavoriteActivity favoriteActivity = mActivity.get();
                favoriteActivity.updateData(list);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
