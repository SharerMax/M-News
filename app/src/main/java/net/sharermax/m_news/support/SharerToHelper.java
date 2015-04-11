package net.sharermax.m_news.support;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import net.sharermax.m_news.api.weibo.StatusesAPI;

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
    public static void sharerToWeibo(final Context context, String status,
                                     final String successMessage, final String failMessage, Location location) {
        Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(context);
        double lat = 0.0;
        double log = 0.0;
        if (null != location) {
            lat = location.getLatitude();
            log = location.getLongitude();
        }
        StatusesAPI statusesAPI = new StatusesAPI(context,Constants.APP_KEY, oauth2AccessToken);
        statusesAPI.update(status, "" + lat, "" + log, new RequestListener() {
            @Override
            public void onComplete(String s) {
                Log.v(CLASS_NAME, s);
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.v(CLASS_NAME, e.toString());
                Toast.makeText(context, failMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
