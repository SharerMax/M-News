package net.sharermax.m_news.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Author: SharerMax
 * Time  : 2015/3/21
 * E-Mail: mdcw1103@gmail.com
 */
public class Setting {
    public static final String CLASS_NAME = "Setting";
    public static final String SETTING_NAME = "setting";
    public static final int MODE_PRIVATE = Context.MODE_PRIVATE;
    public static final int MODE_MULTI_PROCESS = Context.MODE_MULTI_PROCESS;
    @Deprecated
    public static final int MODE_WORLD_WRITEABLE = Context.MODE_WORLD_WRITEABLE;
    @Deprecated
    public static final int MODE_WORLD_READABLE = Context.MODE_WORLD_READABLE;

    public static final String KEY_AUTO_REFRESH = "auto_refresh";
    public static final String KEY_DOUBLE_TO_TOP = "double_to_top";
    public static final String KEY_USE_CARD_VIEW = "use_card_view";
    public static final String KEY_SWIPE_BACK = "swipe_to_back";
    public static final String KEY_DISABLE_LIST_ANIMATION = "disable_animation";
    public static final String KEY_WEIBO = "weibo";
    public static final String KEY_TWITTER = "twitter";
    public static final String KEY_GITHUB = "github";
    public static final String KEY_LICENSE = "license";
    public static final String KEY_VERSION = "version";

    public static final String KEY_SUB_STARTUP = "sub_startup";
    public static final String KEY_SUB_CSDNGEEK = "sub_csdn_geek";
    public static final String KEY_SUB_HACKERNEWS = "sub_hacker_news";

    private SharedPreferences mSharedPreferences;
    public static Setting sInstance;

    public static Setting getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new Setting(context);
        }
        return sInstance;
    }

    private Setting(Context context, String settingName, int mode) {
            mSharedPreferences = context.getSharedPreferences(settingName, mode);
    }

    private Setting(Context context, String settingName) {
        this(context, settingName, MODE_PRIVATE);
    }

    private Setting(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getBoolen(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public String getString(String key, String defaultString) {
        return mSharedPreferences.getString(key, defaultString);
    }

    public int getInteger(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public float getInteger(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public Setting putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
        return this;
    }

    public Setting putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
        return this;
    }
}
