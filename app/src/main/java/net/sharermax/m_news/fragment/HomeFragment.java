package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.AbsActivity;
import net.sharermax.m_news.adapter.RecyclerViewAdapter;
import net.sharermax.m_news.network.WebResolve;
import net.sharermax.m_news.support.Setting;

import java.util.HashMap;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CLASS_NAME = "HomeFragment";
    private RecyclerView mRecyclerView;
    private WebResolve mWebResolve;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<HashMap<String, String>> mWebData;
    private AbsActivity mAbsActivity;
    private boolean mAutoRefreshEnable;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState && isBottom()) {
                    bottomLoad();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red_500);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (mAutoRefreshEnable) {
            onRefresh();
        }

        return rootView;
    }

    private void getSetting() {
        mAutoRefreshEnable = Setting.getInstance(getActivity()).getBoolen(Setting.KEY_AUTO_REFRESH, true);
    }

    @Override
    public void onRefresh() {
        if (null == mWebResolve) {
            mWebResolve = new WebResolve();
            mWebResolve.setTaskOverListener(new WebResolve.TaskOverListener() {
                @Override
                public void taskOver() {
                    mWebData = mWebResolve.getValidData();
                    if (mWebData.isEmpty()) {
                        Toast.makeText(getActivity(), getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                    } else {
                        if (null == mRecyclerView.getAdapter()) {
                            mRecyclerView.setAdapter(new RecyclerViewAdapter(mWebData));
                        }
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        mWebResolve.cleanData();
        mWebResolve.startTask(WebResolve.START_UP_MAIN_PAGES_FLAG);
    }

    private boolean isBottom() {
        LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        return manager.findLastCompletelyVisibleItemPosition() == (manager.getItemCount() - 1);
    }

    private boolean isTop() {
        LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        return manager.findFirstVisibleItemPosition() == 0;
    }

    private void bottomLoad() {
        if (mWebResolve != null && !mWebData.isEmpty()) {
            mWebResolve.startTask(WebResolve.START_UP_NEXT_PAGES_FLAG);
        }
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAbsActivity = (AbsActivity)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must extents AbsActivity");
        }

    }
    @Override
    public void onStart() {
        super.onStart();
//        onRefresh();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
