package net.sharermax.m_news.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.UserHelper;
import net.sharermax.m_news.support.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author: SharerMax
 * Time  : 2015/3/5
 * E-Mail: mdcw1103@gmail.com
 */
public class NavigationDrawerFragment extends Fragment implements ListView.OnItemClickListener{

    public static final String CLASS_NAME = "NavigationDrawerFragment";
    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private SimpleAdapter mSimpleAdapter;
    private NetworkImageView mImageView;
    private View mRootView;
    private CircleImageView mProfileImage;
    private TextView mProfileIDView;
    public static final int LISTVIEW_POSITION_HOME = 0x00;
    public static final int LISTVIEW_POSITION_FAVORITE = 0x01;
    public static final int LISTVIEW_POSITION_SUBSCRIPTION = 0x02;
    public static final int LISTVIEW_POSITION_SETTING = 0x03;
    public static final int LISTVIEW_POSITION_ACCOUNT = 0x04;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (null != savedInstanceState) {
            Log.v(CLASS_NAME, "saveInstanceState is not null");
        }

        mListener.onFragmentInteraction(LISTVIEW_POSITION_HOME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.navigation_drawer_fragment, container, false);
        initListView();
        initImageView();
        return mRootView;
    }

    private void initImageView() {
        mImageView = (NetworkImageView)mRootView.findViewById(R.id.drawer_coverimage);
        mImageView.setDefaultImageResId(R.drawable.background);
        mProfileImage = (CircleImageView)mRootView.findViewById(R.id.profile_image);
        mProfileIDView = (TextView)mRootView.findViewById(R.id.profile_idstr);
        updateProfileImage();

    }

    public void updateProfileImage() {
        ImageLoader imageLoader = new ImageLoader(Utility.getRequestQueue(getActivity()), new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        String image_url = UserHelper.readUserInfo(getActivity()).get(UserHelper.KEY_COVER_IMAGE);
        String profile_image_url = UserHelper.readUserInfo(getActivity()).get(UserHelper.KEY_PROFILE_IMAGE);
        String profile_idstr = UserHelper.readUserInfo(getActivity()).get(UserHelper.KEY_SCREEN_NAME);
//        Log.v(CLASS_NAME, image_url);
        if (!image_url.isEmpty() && !profile_image_url.isEmpty() && !profile_idstr.isEmpty()) {
            mImageView.setImageUrl(image_url, imageLoader);
            ImageRequest imageRequest = new ImageRequest(profile_image_url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            mProfileImage.setImageBitmap(response);
                        }
                    }, 144, 144, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v(CLASS_NAME, error.toString());
                            mProfileImage.setImageResource(R.drawable.ic_action_person);
                        }
                    });
            Utility.getRequestQueue(getActivity()).add(imageRequest);
            mProfileIDView.setText(profile_idstr);
        }
    }

    private void initListView() {
        mListView = (ListView)mRootView.findViewById(R.id.drawer_listView);

        mSimpleAdapter = new SimpleAdapter(getActivity(), getListData(), R.layout.drawer_listview_item,
                                            new String[] {"image", "text"},
                                            new int[] {R.id.item_image, R.id.item_text});
        mListView.setAdapter(mSimpleAdapter);
        mListView.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getListData() {
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("image", R.drawable.ic_home_white);
        map.put("text", getString(R.string.drawer_home_action));
        listData.add(map);

        map = new HashMap<String, Object>();
        map.put("image", R.drawable.ic_action_favorite);
        map.put("text", getString(R.string.drawer_favorite_action));
        listData.add(map);

        map = new HashMap<String, Object>();
        map.put("image", R.drawable.ic_subscription_white);
        map.put("text", getString(R.string.drawer_subscription_action));
        listData.add(map);

        map = new HashMap<String, Object>();
        map.put("image", R.drawable.ic_settings_white);
        map.put("text", getString(R.string.drawer_setting_action));
        listData.add(map);

        map = new HashMap<String, Object>();
        map.put("image", R.drawable.ic_account_binding);
        map.put("text", getString(R.string.drawer_account_binding_action));
        listData.add(map);

        return listData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onFragmentInteraction(position);
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
        public void onFragmentInteraction(int clickedItemPosition);
    }

}
