package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import net.sharermax.m_news.BuildConfig;
import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.AbsActivity;
import net.sharermax.m_news.adapter.RecyclerViewAdapter;
import net.sharermax.m_news.api.news.NewsDistribution;
import net.sharermax.m_news.api.news.common.NewsData;
import net.sharermax.m_news.api.news.common.ResponseListener;
import net.sharermax.m_news.network.WebResolve;
import net.sharermax.m_news.support.Setting;
import net.sharermax.m_news.support.Utility;
import net.sharermax.m_news.view.decoration.DividerItemDecoration;

import java.util.ArrayList;
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
    private NewsDistribution mDistribution;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NewsData> mWebData;
    private View mRootView;
    private int mSwipeRefreshCircleStart;
    private Bundle mBundle;
    private int mMainPageFlag;
    private int mNextPageFlag;
    private boolean mUseCardStyle;
    private CircularProgressView mCircularPB;
    private RecyclerViewAdapter<NewsData> mAdapter;
    private boolean mFirstLoad = true;
    private boolean mListAnimationEnable;
    private Button mRetryButton;
    private DividerItemDecoration mDividerItemDecoration;
    private boolean mHaveDiver;
    private boolean mIsRefreshData;
    private boolean mIsRefreshOver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.news_fragment, container, false);
        int toolBarHeight = Utility.getToolBarHeight(getActivity());
        mSwipeRefreshCircleStart = toolBarHeight * 2;
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

        mCircularPB = (CircularProgressView)mRootView.findViewById(R.id.circular_progress_bar);
//        ViewCompat.setElevation(mCircularPB, R.dimen.progress_bar_circle_elevation);
        mCircularPB.setVisibility(View.VISIBLE);
        mRetryButton = (Button)mRootView.findViewById(R.id.retry_button);
        mRetryButton.setVisibility(View.GONE);
        mRetryButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCircularPB.setVisibility(View.VISIBLE);
                refeshData();
                return false;
            }
        });

        mIsRefreshOver = true;
        initRecyclerView();
        initGlobalLayoutListener();
        initSwipeRefreshLayout();
        initWebResolve();
        refeshData();
        return mRootView;
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red_500);
        mSwipeRefreshLayout.setProgressViewOffset(true, mSwipeRefreshCircleStart, mSwipeRefreshCircleStart + mSwipeRefreshCircleStart / 2);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            //When first refresh data (load data) swiperefreshlayout don't receive refresh event
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mFirstLoad;
            }
        });
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
        //
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !(null != mAdapter && (mAdapter.getDataSize() > 0));
            }
        });
        updateItemView();
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
        Setting setting = Setting.getInstance(getActivity());
        mUseCardStyle = setting.getBoolen(Setting.KEY_USE_CARD_VIEW, true);
        mListAnimationEnable = !setting.getBoolen(Setting.KEY_DISABLE_LIST_ANIMATION, true);
    }

    private void initWebResolve() {
//        mWebResolve = new WebResolve(getActivity());
//        mWebResolve.setTaskOverListener(new WebResolve.TaskOverListener() {
//            @Override
//            public void taskOver(List<HashMap<String, String>> dataList) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                mCircularPB.setVisibility(View.GONE);
//                mFirstLoad = false;
//                if (!dataList.isEmpty()) {
//                    if (mIsRefreshData && mAdapter.getDataSize() > 0) {
//                        mAdapter.clear();
//                        if (BuildConfig.DEBUG) {
//                            Log.v(CLASS_NAME, "clear data");
//                        }
//                    }
//                    mRetryButton.setVisibility(View.GONE);
//                    mAdapter.addItems(mAdapter.getDataSize(), dataList);
//                } else {
////                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.net_error), Toast.LENGTH_LONG).show();
//                    Snackbar.make(getView(), getString(R.string.net_error), Snackbar.LENGTH_LONG).show();
//                    if (mAdapter.getDataSize() == 0) {
//                        mRetryButton.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//        });

        mDistribution = new NewsDistribution(getActivity(),
                new ResponseListener<List<NewsData>>() {
                    @Override
                    public void onResponse(List<NewsData> response) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mCircularPB.setVisibility(View.GONE);
                        mFirstLoad = false;
                        mIsRefreshOver = true;
                        if (null != response && !response.isEmpty()) {
                            if (mIsRefreshData && mAdapter.getDataSize() > 0) {
                                mAdapter.clear();
                                if (BuildConfig.DEBUG) {
                                    Log.v(CLASS_NAME, "clear data");
                                }
                            }
                            mRetryButton.setVisibility(View.GONE);
                            mAdapter.addItems(mAdapter.getDataSize(), response);
                        } else {
                            Snackbar.make(getView(), getString(R.string.net_error), Snackbar.LENGTH_LONG).show();
                            if (mAdapter.getDataSize() == 0) {
                                mRetryButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        if (mIsRefreshOver) {
            mRetryButton.setVisibility(View.GONE);
            mDistribution.start(mMainPageFlag);
            mIsRefreshOver = false;
            mIsRefreshData = true;
        }
    }

    public void refeshData() {
        onRefresh();
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
        if (mDistribution != null && null != mWebData && !mWebData.isEmpty()) {
            mDistribution.start(mNextPageFlag);
            mIsRefreshData = false;
        }
    }

    public void updateItemView() {
        initSetting();
        if (null == mWebData) {
            mWebData = new ArrayList<>();
        }
        mAdapter = new RecyclerViewAdapter<>(
                mWebData, mUseCardStyle);
        mAdapter.setListAnimationEnable(mListAnimationEnable);
        mRecyclerView.setAdapter(mAdapter);

        if (!mUseCardStyle) {
            if (null == mDividerItemDecoration) {
                mDividerItemDecoration = new DividerItemDecoration(getActivity());
            }
            if (!mHaveDiver) {
                mRecyclerView.addItemDecoration(mDividerItemDecoration);
                mHaveDiver = true;
            }
        } else {
            if (mHaveDiver) {
                mRecyclerView.removeItemDecoration(mDividerItemDecoration);
                mHaveDiver = false;
            }
        }
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

    @Override
    public void onStop() {
//        if (mWebResolve != null) mWebResolve.cancel();
        super.onStop();
    }
}
