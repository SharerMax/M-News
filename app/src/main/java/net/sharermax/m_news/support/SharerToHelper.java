package net.sharermax.m_news.support;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import net.sharermax.m_news.api.weibo.StatusesAPI;

import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String FIELD_GEOS = "geos";
    public static final String FIELD_ADDRESS = "address";
    public static final String ERROR_ADDRESS = "UnKnow";
    public static void sendToWeibo(final Context context, String status,
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
    //坐标转地址
    public static void geoToAddress(Context context, String accessToken, Location location, final GeoToAddressListener listener) {
        String geoToAddress = "https://api.weibo.com/2/location/geo/geo_to_address.json";
        String coordinate = "" + location.getLongitude() + "," + location.getLatitude();
        String url = geoToAddress + "?access_token=" + accessToken + "&coordinate=" + coordinate;
        Log.v(CLASS_NAME, url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String address = ERROR_ADDRESS;
                        Log.v(CLASS_NAME, response.toString());
                        try {
                           address = response.getJSONArray(FIELD_GEOS).getJSONObject(0).getString(FIELD_ADDRESS);
                           Log.v(CLASS_NAME, "Address" + address);
                        } catch (JSONException e) {
                            Log.v(CLASS_NAME, e.toString());
                        }
                        if (null != listener) {
                            listener.onResponse(address);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(CLASS_NAME, error.toString());
                if (null != listener) {
                    listener.onResponse(ERROR_ADDRESS);
                }
            }
        });

        Utility.getRequestQueue(context).add(jsonObjectRequest);
    }
    //坐标转地址监听接口
    public static interface GeoToAddressListener {
        abstract public void onResponse(String address);
    }

}
