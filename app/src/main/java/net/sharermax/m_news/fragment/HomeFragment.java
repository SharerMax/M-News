package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.AbsActivity;
import net.sharermax.m_news.adapter.RecyclerViewAdapter;
import net.sharermax.m_news.network.WebResolve;
import net.sharermax.m_news.support.Setting;
import net.sharermax.m_news.support.Utility;

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
    private View mRootView;
    private Toolbar mToolBar;
    private int mToolBarDistance = 0;
    private final int HIDE_HOLD = 20;
    private boolean mToolbarVisible = true;

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
        mRootView = inflater.inflate(R.layout.home_fragment, container, false);
        initToolBar();
        initRecylerView();
        initSwipeRefreshLayout();

        if (mAutoRefreshEnable) {
            onRefresh();
        }

        return mRootView;
    }

    private void initToolBar() {
        mToolBar = (Toolbar)mRootView.findViewById(R.id.toolbar);
        mAbsActivity.setSupportActionBar(mToolBar);
        mAbsActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red_500);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecylerView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.main_recyclerview);
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
                if (isTop()) {
                    if (!mToolbarVisible) {
                        mToolBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        mToolbarVisible = true;
                    }
                } else {
                    if (mToolBarDistance > HIDE_HOLD && mToolbarVisible) {
                        mToolBar.animate().translationY(-mToolBar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
                        mToolbarVisible = false;
                        mToolBarDistance = 0;
                    } else if (mToolBarDistance < -HIDE_HOLD && !mToolbarVisible) {
                        mToolBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        mToolbarVisible = true;
                        mToolBarDistance = 0;
                    }
                }

                if ((mToolbarVisible && dy > 0) || (!mToolbarVisible && dy < 0)) {
                    mToolBarDistance += dy;
                }
            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
                            mRecyclerView.setAdapter(new RecyclerViewAdapter(
                                    mWebData, Setting.getInstance(getActivity()).getBoolen(Setting.KEY_USE_CARD_VIEW, true)));
                        }
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        if (mWebResolve.isFinished()) {
            mWebResolve.cleanData();
            mWebResolve.startTask(WebResolve.START_UP_MAIN_PAGES_FLAG);
        }
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
