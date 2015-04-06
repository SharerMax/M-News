package net.sharermax.m_news.support;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Author: SharerMax
 * Time  : 2015/4/4
 * E-Mail: mdcw1103@gmail.com
 */
public class AccessTokenKeeper {
    public static final String KEY_WEIBO_TOKEN = "weibo_token";
    public static final String KEY_WEIBO_REFRESH_TOKEN = "weibo_refresh_token";
    public static final String KEY_WEIBO_USERID = "weibo_user_id";
    public static final String KEY_WEIBO_EXPIRESTIME = "weibo_expires_time";
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token) {
            return;
        }
        Setting.getInstance(context)
                .putString(KEY_WEIBO_USERID, token.getUid())
                .putString(KEY_WEIBO_TOKEN, token.getToken())
                .putString(KEY_WEIBO_REFRESH_TOKEN, token.getRefreshToken())
                .putLong(KEY_WEIBO_EXPIRESTIME, token.getExpiresTime());
    }
    public static Oauth2AccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        Oauth2AccessToken token = new Oauth2AccessToken();
        token.setToken(Setting.getInstance(context).getString(KEY_WEIBO_TOKEN, ""));
        token.setRefreshToken(Setting.getInstance(context).getString(KEY_WEIBO_REFRESH_TOKEN, ""));
        token.setUid(Setting.getInstance(context).getString(KEY_WEIBO_USERID, ""));
        token.setExpiresTime(Setting.getInstance(context).getLong(KEY_WEIBO_EXPIRESTIME, 0));
        return token;
    }

    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        Setting.getInstance(context)
                .putString(KEY_WEIBO_USERID, "")
                .putString(KEY_WEIBO_TOKEN, "")
                .putString(KEY_WEIBO_REFRESH_TOKEN, "")
                .putLong(KEY_WEIBO_EXPIRESTIME, 0);
    }
}
