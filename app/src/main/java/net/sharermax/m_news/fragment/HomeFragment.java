package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.RecyclerViewAdapter;
import net.sharermax.m_news.network.WebResolve;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private WebResolve mWebResolve;
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

        mWebResolve = new WebResolve();
        mWebResolve.setTaskOverListener(new WebResolve.TaskOverListener() {
            @Override
            public void taskOver() {
                mRecyclerView.setAdapter(new RecyclerViewAdapter(mWebResolve.getValidData()));
            }
        });
        mWebResolve.startTask();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
