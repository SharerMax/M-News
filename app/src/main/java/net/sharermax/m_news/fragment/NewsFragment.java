package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
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

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

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

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CLASS_NAME = "NewsFragment";
    public static final String FLAG_INITIAL_POSITION = "FLAG_INITIAL_POSITION";
    private ObservableRecyclerView mRecyclerView;
    private WebResolve mWebResolve;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<HashMap<String, String>> mWebData;
    private AbsActivity mAbsActivity;
    private boolean mAutoRefreshEnable;
    private View mRootView;
    private int mSwipeRefreshCircleStart;
    private OnNewsScrolledListener mListener;
    private ObservableScrollViewCallbacks mScrollViewCallbacks;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }
    public NewsFragment() {
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
        mRootView = inflater.inflate(R.layout.news_fragment, container, false);
        mSwipeRefreshCircleStart = (int) getResources().getDimension(R.dimen.toolbar_height) * 2;
        initRecylerView();
        initGlobalLayoutListener();
        initSwipeRefreshLayout();

        if (mAutoRefreshEnable) {
            onRefresh();
        }

        return mRootView;
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red_500);
        mSwipeRefreshLayout.setProgressViewOffset(true, mSwipeRefreshCircleStart, mSwipeRefreshCircleStart + mSwipeRefreshCircleStart / 2);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecylerView() {
        mRecyclerView = (ObservableRecyclerView) mRootView.findViewById(R.id.news_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
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
                if (null != mScrollViewCallbacks) {
                    mScrollViewCallbacks.onScrollChanged(dy, isTop(), true);
                }


//
//                if (isTop()) {
//                    if (!mToolbarVisible) {
//                        //mToolBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//                        mToolbarVisible = true;
//                    }
//                } else {
//                    if (mToolBarDistance > HIDE_HOLD && mToolbarVisible) {
//                       // mToolBar.animate().translationY(-mToolBar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
//                        mToolbarVisible = false;
//                        mToolBarDistance = 0;
//                    } else if (mToolBarDistance < -HIDE_HOLD && !mToolbarVisible) {
//                      //  mToolBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//                        mToolbarVisible = true;
//                        mToolBarDistance = 0;
//                    }
//                }
//
//                if ((mToolbarVisible && dy > 0) || (!mToolbarVisible && dy < 0)) {
//                    mToolBarDistance += dy;
//                }
            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initGlobalLayoutListener() {
        Log.v(CLASS_NAME, "HHHHH");
        if (mAbsActivity instanceof ObservableScrollViewCallbacks) {

            Bundle args = getArguments();
            if (args != null && args.containsKey(FLAG_INITIAL_POSITION)) {
                Log.v(CLASS_NAME, "TTTTT");
                final int initialPosition = args.getInt(FLAG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.scrollVerticallyToPosition(1);

                    }
                });
            }
            mRecyclerView.setScrollViewCallbacks(mScrollViewCallbacks = (ObservableScrollViewCallbacks)mAbsActivity);
        }
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

    public void setOnNewsScrolledListerer(OnNewsScrolledListener listener) {
        mListener = listener;
    }

    public static interface OnNewsScrolledListener {
        abstract public void OnScrolled(int dy, boolean fistVisible);
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
