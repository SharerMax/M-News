package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.AbsActivity;
import net.sharermax.m_news.adapter.RecyclerViewAdapter;
import net.sharermax.m_news.network.WebResolve;
import net.sharermax.m_news.support.Setting;
import net.sharermax.m_news.support.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CLASS_NAME = "NewsFragment";
    public static final String FLAG_INITIAL_POSITION = "FLAG_INITIAL_POSITION";
    public static final String FLAG_INITIAL_NEWS = "FLAG_INITIAL_NEWS";
    public static final int FLAG_NEWS_STARTUP = 0;
    public static final int FLAG_NEWS_HACKERNEWS = 1;
    private ObservableRecyclerView mRecyclerView;
    private WebResolve mWebResolve;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<HashMap<String, String>> mWebData;
    private boolean mAutoRefreshEnable;
    private View mRootView;
    private int mSwipeRefreshCircleStart;
    private int mToolBarHeight;
    private Bundle mBundle;
    private int mMainPageFlag;
    private int mNextPageFlag;
    private boolean mUseCardStyle;
    private ProgressBarCircularIndeterminate mCircularPB;
    private RecyclerViewAdapter<HashMap<String, String>> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.news_fragment, container, false);
        mToolBarHeight = Utility.getToolBarHeight(getActivity());
        mSwipeRefreshCircleStart = mToolBarHeight * 2;
        mBundle = getArguments();
        if (null != mBundle) {
            if (mBundle.containsKey(FLAG_INITIAL_NEWS)) {
                if (FLAG_NEWS_STARTUP == mBundle.getInt(FLAG_INITIAL_NEWS)) {
                    mMainPageFlag = WebResolve.START_UP_MAIN_PAGES_FLAG;
                    mNextPageFlag = WebResolve.START_UP_NEXT_PAGES_FLAG;
                }

                if (FLAG_NEWS_HACKERNEWS == mBundle.getInt(FLAG_INITIAL_NEWS)) {
                    mMainPageFlag = WebResolve.HACKER_NEWS_MAIN_PAGES_FLAG;
                    mNextPageFlag = WebResolve.HACKER_NEWS_NEXT_PAGES_FLAG;
                }
            }
        }
        initRecyclerView();
        initGlobalLayoutListener();
        initSwipeRefreshLayout();
        initWebResolve();
        mCircularPB = (ProgressBarCircularIndeterminate)mRootView.findViewById(R.id.circular_progress_bar);
        ViewCompat.setElevation(mCircularPB, R.dimen.progress_bar_circle_elevation);
        if (mAutoRefreshEnable) {
            onRefresh();
            mCircularPB.setVisibility(View.VISIBLE);
        } else {
            mCircularPB.setVisibility(View.GONE);
        }

        return mRootView;
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red_500);
        mSwipeRefreshLayout.setProgressViewOffset(true, mSwipeRefreshCircleStart, mSwipeRefreshCircleStart + mSwipeRefreshCircleStart / 2);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView = (ObservableRecyclerView) mRootView.findViewById(R.id.news_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState && isBottom()) {
                    if (mWebData.isEmpty()) {
                        return;
                    }
                    bottomLoad();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mWebResolve != null && mWebResolve.isFinished()) {
                    return false;
                }
                return true;
            }
        });

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mWebData = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter<>(
                mWebData, mUseCardStyle);
        mAdapter.setItemDialogEnable(true);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initGlobalLayoutListener() {
        Activity parentActivity = getActivity();
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            if (mBundle != null && mBundle.containsKey(FLAG_INITIAL_POSITION)) {
                final int initialPosition = mBundle.getInt(FLAG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.scrollVerticallyToPosition(initialPosition);

                    }
                });
            }
            mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks)parentActivity);
        }
    }
    private void initSetting() {
        mAutoRefreshEnable = Setting.getInstance(getActivity()).getBoolen(Setting.KEY_AUTO_REFRESH, true);
        mUseCardStyle = Setting.getInstance(getActivity()).getBoolen(Setting.KEY_USE_CARD_VIEW, true);
    }

    private void initWebResolve() {
        mWebResolve = new WebResolve(getActivity());
        mWebResolve.setTaskOverListener(new WebResolve.TaskOverListener() {
            @Override
            public void taskOver(List<HashMap<String, String>> dataList) {
                mCircularPB.setVisibility(View.GONE);
                if (!dataList.isEmpty()) {
                    mAdapter.addItems(mAdapter.getDataSize(), dataList);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_LONG).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (mWebResolve.isFinished()) {
            mWebResolve.cleanData();
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            mWebResolve.startTask(mMainPageFlag);
        }
    }

    private boolean isBottom() {
        LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        return manager.findLastCompletelyVisibleItemPosition() >= (manager.getItemCount() - 2);
    }

    private boolean isTop() {
        LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        return manager.findFirstVisibleItemPosition() == 0;
    }

    private void bottomLoad() {
        if (mWebResolve != null && null != mWebData && !mWebData.isEmpty()) {
            mWebResolve.startTask(mNextPageFlag);
        }
    }

    public void updateItemView() {
        mUseCardStyle = Setting.getInstance(getActivity()).getBoolen(Setting.KEY_USE_CARD_VIEW, true);
        mWebData.clear();
        mAdapter = new RecyclerViewAdapter<>(
                mWebData, mUseCardStyle);
        mAdapter.setItemDialogEnable(true);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            AbsActivity a = (AbsActivity)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must extents AbsActivity");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
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
