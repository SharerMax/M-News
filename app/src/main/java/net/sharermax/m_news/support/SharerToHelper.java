package net.sharermax.m_news.support;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import net.sharermax.m_news.api.weibo.StatusesAPI;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: SharerMax
 * Time  : 2015/4/9
 * E-Mail: mdcw1103@gmail.com
 */
public class SharerToHelper {
    public static final String CLASS_NAME = "SharerToHelper";
    public static final String SEND_TO = "https://api.weibo.com/2/statuses/update.json";
    public static final String FIELD_TOKEN = "access_token";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_VISIBLE = "visible";
    public static void sharerToWeibo(Context context, String status) {
        Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(context);
        StatusesAPI statusesAPI = new StatusesAPI(context,Constants.APP_KEY, oauth2AccessToken);
        statusesAPI.update(status, "0.0", "0.0", new RequestListener() {
            @Override
            public void onComplete(String s) {
                Log.v(CLASS_NAME, s);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.v(CLASS_NAME, e.toString());
            }
        });
    }

}
