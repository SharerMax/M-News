package net.sharermax.m_news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.SimpleRecyclerAdapter;

import java.util.ArrayList;

/**
 * Author: SharerMax
 * Time  : 2015/4/16
 * E-Mail: mdcw1103@gmail.com
 */
public class SubscriptionFragment extends Fragment {
    private ObservableRecyclerView mRecyclerView;
    private View mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.subscription_fragment, container, false);
        mRecyclerView = (ObservableRecyclerView)mRootView.findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(new SimpleRecyclerAdapter(getActivity(), getDummyData(20)));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int top =(int) getResources().getDimension(R.dimen.toolbar_height) * 2;
        mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), top, mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
        mSwipeRefreshLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setProgressViewOffset(true, top, top + top / 2);

        return mRootView;
    }

    public static ArrayList<String> getDummyData(int num) {
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 1; i <= num; i++) {
            items.add("Item " + i);
        }
        return items;
    }
}
