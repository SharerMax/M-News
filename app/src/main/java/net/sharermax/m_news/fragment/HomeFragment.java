package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.RecyclerViewAdapter;
import net.sharermax.m_news.network.HttpUtils;
import net.sharermax.m_news.network.WebResolve;

import java.util.HashMap;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String CLASS_NAME = "HomeFragment";
    private RecyclerView mRecyclerView;
    private WebResolve mWebResolve;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<HashMap<String, String>> mWebData;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red_500);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        return rootView;
    }

    @Override
    public void onRefresh() {
        if (!HttpUtils.isConnect(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.net_error),Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (null == mWebResolve) {
            mWebResolve = new WebResolve();
            mWebResolve.setTaskOverListener(new WebResolve.TaskOverListener() {
                @Override
                public void taskOver() {
                    mWebData = mWebResolve.getValidData();
                    if (null == mWebData) {
                        Toast.makeText(getActivity(), getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                    } else {
                        mRecyclerView.setAdapter(new RecyclerViewAdapter(mWebData));
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
            mWebResolve.startTask(WebResolve.START_UP_MAIN_PAGES_FLAG);
        }
        mWebResolve.startTask(WebResolve.START_UP_NEXT_PAGES_FLAG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }
    @Override
    public void onResume() {
        super.onResume();
//        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
