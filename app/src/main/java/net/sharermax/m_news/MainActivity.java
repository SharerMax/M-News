package net.sharermax.m_news;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static String TAG = "MainActivty";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mTestLayout;
    private RecyclerView mRecyclerView;

    public static final List<String> data;

    static {
        data = new ArrayList<String>();
        data.add("浮夸 - 陈奕迅");
        data.add("好久不见 - 陈奕迅");
        data.add("时间都去哪儿了 - 王铮亮");
        data.add("董小姐 - 宋冬野");
        data.add("爱如潮水 - 张信哲");
        data.add("给我一首歌的时间 - 周杰伦");
        data.add("天黑黑 - 孙燕姿");
        data.add("可惜不是你 - 梁静茹");
        data.add("太委屈 - 陶晶莹");
        data.add("用心良苦 - 张宇");
        data.add("说谎 - 林宥嘉");
        data.add("独家记忆 - 陈小春");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mRecyclerView = (RecyclerView)findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mTestLayout = (LinearLayout)findViewById(R.id.test_layout);
        mRecyclerView.setAdapter(new MyAdapter(data));

    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<String> data;
        public MyAdapter(List<String> data) {
            this.data = data;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public MyViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView)itemView.findViewById(R.id.main_item_textview);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_content_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mTextView.setText(data.get(position));
        }


        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                if (mDrawerLayout.isDrawerVisible(mTestLayout)) {
                    mDrawerLayout.closeDrawer(mTestLayout);
                } else {
                    mDrawerLayout.openDrawer(mTestLayout);
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
