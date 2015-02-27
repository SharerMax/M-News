package net.sharermax.m_news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sharermax.m_news.network.WebResolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static String CLASS_TAG = "MainActivty";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mTestLayout;
    private RecyclerView mRecyclerView;
    private Button mTestButton;
    WebResolve mWebResolve;

    private List<HashMap<String, String >> mValidData;

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
        
        mWebResolve = new WebResolve();
        mWebResolve.setTaskOverListener(new WebResolve.TaskOverListener() {
            @Override
            public void taskOver() {
                mValidData = mWebResolve.getValidData();
                mRecyclerView.setAdapter(new MyAdapter(mValidData));
            }
        });
        mWebResolve.startTask();
        mTestButton = (Button)findViewById(R.id.test_button);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<HashMap<String, String>> data;
        public MyAdapter(List<HashMap<String,String>> data) {
            this.data = data;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public MyViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView)itemView.findViewById(R.id.main_item_textview);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), MyAdapter.this.data.get(getPosition()).get("url"), Toast.LENGTH_LONG).show();
                        String url = MyAdapter.this.data.get(getPosition()).get("url");
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_content_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mTextView.setText(data.get(position).get("title"));
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
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
