package net.sharermax.m_news.support;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Author: SharerMax
 * Time  : 2015/8/15
 * E-Mail: mdcw1103@gmail.com
 */

/**
 * 位置获取助手类
 */
public class LocationHelper implements LocationListener {
    private static LocationHelper sLocationHelper;
    private Context mContext;
    private LocationManager mLocationManager;
    private WeakReference<UpdateLocationListener> mUpdateLocationListenerWeakReference;
    public static final String FIELD_ADDRESS = "address";
    public static final String ERROR_ADDRESS = "UnKnow";
    public static final String FIELD_GEOS = "geos";
    public static final String TAG_REQUEST_ADDRESS = "tag_request_address";

    private LocationHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public static synchronized LocationHelper getInstance(Context context) {
        if (null == sLocationHelper) {
            sLocationHelper = new LocationHelper(context);
        }
        return sLocationHelper;
    }

    /**
     * 获取位置信息
     */
    public void updateLocation() {
        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location;

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location  = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (null != location) {
                onLocationChanged(location);
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }

        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location  = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (null != location) {
                onLocationChanged(location);
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
    }

    /**
     * 取消位置获取
     */
    public void cannelUpdateLocation() {
        if (mLocationManager != null) mLocationManager.removeUpdates(this);
    }

    /**
     * 经纬度转换实际地址
     * @param accessToken 微博AccessToken
     * @param location 位置信息
     * @param listener 转换监听
     */
    public void geoToAddress(String accessToken, Location location, final GeoToAddressListener listener) {
        String geoToAddress = "https://api.weibo.com/2/location/geo/geo_to_address.json";
        String coordinate = "" + location.getLongitude() + "," + location.getLatitude();
        String url = geoToAddress + "?access_token=" + accessToken + "&coordinate=" + coordinate;
//        Log.v(CLASS_NAME, url);
        final WeakReference<GeoToAddressListener> wf = new WeakReference<GeoToAddressListener>(listener);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String address = ERROR_ADDRESS;
//                        Log.v(CLASS_NAME, response.toString());
                        try {
                            address = response.getJSONArray(FIELD_GEOS).getJSONObject(0).getString(FIELD_ADDRESS);
//                           Log.v(CLASS_NAME, "Address" + address);
                        } catch (JSONException e) {
//                            Log.v(CLASS_NAME, e.toString());
                        }
                        GeoToAddressListener listener = wf.get();
                        if (null != listener) {
                            listener.onResponse(address);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.v(CLASS_NAME, error.toString());
                GeoToAddressListener listener = wf.get();
                if (null != listener) {
                    listener.onResponse(ERROR_ADDRESS);
                }
            }
        });
        jsonObjectRequest.setTag(TAG_REQUEST_ADDRESS);
        Utility.getRequestQueue(mContext).add(jsonObjectRequest);
    }

    /**
     * 取消地址装换请求
     */
    public void cancelGeoToAddress() {
        Utility.getRequestQueue(mContext).cancelAll(TAG_REQUEST_ADDRESS);
    }

    /**
     * 地址转换监听器
     */
    public interface GeoToAddressListener {
        void onResponse(String address);
    }

    /**
     * 更新状态监听器
     */
    public interface UpdateLocationListener {
        void update(Location location);
    }

    /**
     * 设置位置更新监听器
     * @param listener 位置更新监听器
     */
    public void setUpdateLocationListener(UpdateLocationListener listener) {
        mUpdateLocationListenerWeakReference = new WeakReference<>(listener);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (null == location) return;
        cannelUpdateLocation();
        UpdateLocationListener updateLocationListener = mUpdateLocationListenerWeakReference.get();
        if (updateLocationListener != null) {
            updateLocationListener.update(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
