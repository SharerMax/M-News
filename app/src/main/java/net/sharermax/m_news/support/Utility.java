package net.sharermax.m_news.support;

import android.content.Context;
import android.content.res.TypedArray;
import android.location.LocationListener;
import android.location.LocationManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.sharermax.m_news.R;


/**
 * Author: SharerMax
 * Time  : 2015/3/20
 * E-Mail: mdcw1103@gmail.com
 */
public class Utility {
    public static final String CLASS_NAME = "Utility";
    private static RequestQueue sRequestQueue;

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    public static int getToolBarHeight(Context context) {
        TypedArray styleAttributes = context.getTheme().obtainStyledAttributes(
                new int[] {R.attr.actionBarSize});
        int toolBarHeight = (int)styleAttributes.getDimension(0, 0);
        return toolBarHeight;
    }

    public static String getVersionInfo(Context context) {
        String versionInfo = "Unknow";
        try {
            versionInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            versionInfo += "(Build" + context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode + ")";
        } catch (Exception e) {

        }
        return versionInfo;
    }

    public static long getDayFromCurren(long milliseconds) {
        long sp = 24 * 60 * 60 * 1000;
        long ms = milliseconds - System.currentTimeMillis() + sp;
        long day = ms / sp;
        return day;
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (null == sRequestQueue) {
            sRequestQueue = Volley.newRequestQueue(context);
        }
        return sRequestQueue;
    }

    public static LocationManager updateLocation(Context context, LocationListener locationListener) {

        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        }
        return locationManager;
    }


}
