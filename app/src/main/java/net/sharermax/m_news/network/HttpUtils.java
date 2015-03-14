package net.sharermax.m_news.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author: SharerMax
 * Time  : 2015/3/11
 * E-Mail: mdcw1103@gmail.com
 */
public class HttpUtils {
    public static final String CLASS_NAME = "HttpUtils";

    public static boolean isConnect(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConnect = networkInfo.isConnected();
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConnect = networkInfo.isConnected();

        return isWifiConnect || isMobileConnect;
    }
}
