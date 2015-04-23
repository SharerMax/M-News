package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

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
    private int mToolBarHeight;

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
        mToolBarHeight = Utility.getToolBarHeight(getActivity());
        mSwipeRefreshCircleStart = mToolBarHeight * 2;
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
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setPadding(
                mRecyclerView.getPaddingLeft(), mToolBarHeight,
                mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initGlobalLayoutListener() {
        Activity parentActivity = getActivity();
        if (parentActivity instanceof ObservableScrollViewCallbacks) {

            Bundle args = getArguments();
            if (args != null && args.containsKey(FLAG_INITIAL_POSITION)) {
                Log.v(CLASS_NAME, "TTTTT");
                final int initialPosition = args.getInt(FLAG_INITIAL_POSITION, 0);
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
    private void getSetting() {
        mAutoRefreshEnable = Setting.getInstance(getActivity()).getBoolen(Setting.KEY_AUTO_REFRESH, true);
    }

    @Override
    public void onRefresh() {
        if (null == mWebResolve) {
            mWebResolve = new WebResolve(getActivity());
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
        if (mWebResolve != null && null != mWebData && !mWebData.isEmpty()) {
            mWebResolve.startTask(WebResolve.START_UP_NEXT_PAGES_FLAG);
        }
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
