package net.sharermax.m_news.support;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Author: SharerMax
 * Time  : 2015/4/8
 * E-Mail: mdcw1103@gmail.com
 */
public class UserHelper {
    private static String CLASS_NAME = "UserHelper";
    private static final String USER_INFO = "https://api.weibo.com/2/users/show.json";

    public static final String KEY_IDSTR = "idstr";
    public static final String KEY_SCREEN_NAME = "screen_name";
    public static final String KEY_PROFILE_IMAGE = "profile_image_url";
    public static final String KEY_COVER_IMAGE = "cover_image";

    private RequestQueue mRequestQueue;
    private Context mContext;

    public static UserHelper getInstance(Context context) {
        return new UserHelper(context);
    }
    public UserHelper(Context context) {
        mContext = context;
        mRequestQueue = Utility.getRequestQueue(mContext);
    }

    public void getUserInfo(String userToken, String userId, final OnGetUserInfoListener listener) {
        getUserInfo(mContext, userToken, userId, listener);
    }

    public static void getUserInfo(final Context context, String userToken, String userId, final OnGetUserInfoListener listener) {
        String url = USER_INFO + "?access_token=" + userToken + "&uid=" + userId;
        Log.v(CLASS_NAME, url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.v(CLASS_NAME, response.toString());
                        writeUserInfo(context, response);
                        if (null != listener) {
                            listener.onResponse(readUserInfo(context));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(CLASS_NAME, error.toString());
            }
        });
        Utility.getRequestQueue(context).add(jsonObjectRequest);
    }

    public static interface OnGetUserInfoListener {
        public void onResponse(Map userInfo);
    }

    public void writeUserInfo(JSONObject jsonObject) {
        writeUserInfo(mContext, jsonObject);
    }

    public HashMap<String, String> readUserInfo() {
        return readUserInfo(mContext);
    }

    public boolean isUserInfoVailed() {
        return isUserInfoVailed(readUserInfo());
    }

    public static void writeUserInfo(Context context, JSONObject jsonObject) {
        if (null == jsonObject) {
            Log.v(CLASS_NAME, "NULL");
            return;
        }
        try {
            Setting.getInstance(context)
                    .putString(KEY_IDSTR, jsonObject.getString(KEY_IDSTR))
                    .putString(KEY_SCREEN_NAME, jsonObject.getString(KEY_SCREEN_NAME))
                    .putString(KEY_PROFILE_IMAGE, jsonObject.getString(KEY_PROFILE_IMAGE))
                    .putString(KEY_COVER_IMAGE, jsonObject.getString(KEY_COVER_IMAGE));
        } catch (JSONException e) {
            Log.v(CLASS_NAME, e.toString());
        }
    }

    public static HashMap<String, String> readUserInfo(Context context) {
        Setting setting = Setting.getInstance(context);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_IDSTR, setting.getString(KEY_IDSTR, ""));
        map.put(KEY_SCREEN_NAME, setting.getString(KEY_SCREEN_NAME, ""));
        map.put(KEY_PROFILE_IMAGE, setting.getString(KEY_PROFILE_IMAGE, ""));
        map.put(KEY_COVER_IMAGE, setting.getString(KEY_COVER_IMAGE, ""));
        return map;
    }

    public static boolean isUserInfoVailed(Map map) {
        if (null == map) {
            return false;
        }
        return !(map.get(KEY_IDSTR).toString().isEmpty() ||
                map.get(KEY_SCREEN_NAME).toString().isEmpty() ||
                map.get(KEY_PROFILE_IMAGE).toString().isEmpty() ||
                map.get(KEY_COVER_IMAGE).toString().isEmpty());
    }
}
