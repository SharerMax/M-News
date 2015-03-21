package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.sharermax.m_news.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String CLASS_NAME = "NavigationDrawerFragment";
    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private SimpleAdapter mSimpleAdapter;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (null != savedInstanceState) {
            Log.v(CLASS_NAME, "saveInstanceState is not null");
        }

        mListener.onFragmentInteraction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.navigation_drawer_fragment, container, false);
        mListView = (ListView)rootView.findViewById(R.id.drawer_listView);

        mSimpleAdapter = new SimpleAdapter(getActivity(), getListData(), R.layout.drawer_listview_item,
                                            new String[] {"image", "text"},
                                            new int[] {R.id.item_image, R.id.item_text});
        mListView.setAdapter(mSimpleAdapter);
        return rootView;
    }

    private List<Map<String, Object>> getListData() {
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("image", R.drawable.ic_home_white);
        map.put("text", getString(R.string.home));
        listData.add(map);
        return listData;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

}
