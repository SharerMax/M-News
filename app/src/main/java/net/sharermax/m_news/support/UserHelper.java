package net.sharermax.m_news.support;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * Author: SharerMax
 * Time  : 2015/4/8
 * E-Mail: mdcw1103@gmail.com
 */
public class UserHelper {
    private static String CLASS_NAME = "UserHelper";
    private static RequestQueue mRequestQueue;
    private static UserHelper sUserHelper;
    private static final String USER_INFO = "https://api.weibo.com/2/users/show.json";
    public static UserHelper getInstance(Context context) {
        if (null == sUserHelper) {
            sUserHelper = new UserHelper(context);
        }
        return sUserHelper;
    }
    private UserHelper(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }
    public void getUserInfo(String userToken, String userId) {
        String url = USER_INFO + "?access_token=" + userToken + "&uid=" + userId;
        Log.v(CLASS_NAME, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(CLASS_NAME, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(CLASS_NAME, error.toString());
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }
}
