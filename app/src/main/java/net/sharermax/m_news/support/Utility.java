package net.sharermax.m_news.support;

import android.content.Context;

/**
 * Author: SharerMax
 * Time  : 2015/3/20
 * E-Mail: mdcw1103@gmail.com
 */
public class Utility {
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
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
}
