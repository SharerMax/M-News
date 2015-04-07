package net.sharermax.m_news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.AccessTokenKeeper;
import net.sharermax.m_news.support.Constants;
import net.sharermax.m_news.support.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: SharerMax
 * Time  : 2015/3/31
 * E-Mail: mdcw1103@gmail.com
 */
public class AccountBindActivity extends AbsActivity
        implements  WeiboAuthListener, AdapterView.OnItemClickListener{
    public static final String CLASS_NAME = "AccountBindActivity";
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private ListView mListView;
    private SimpleAdapter mSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountbind_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            Log.v(CLASS_NAME, "ExpiresTime:" + mAccessToken.getExpiresTime());
        }
        Log.v(CLASS_NAME, "no valid");

        mListView = (ListView)findViewById(R.id.accountbind_listview);
        mSimpleAdapter = new SimpleAdapter(this, getListData(), R.layout.accountbind_item,
                new String[] {"circle_image", "text"}, new int[] {R.id.circle_image, R.id.text});
        mListView.setAdapter(mSimpleAdapter);
        mListView.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getListData() {
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circle_image", R.drawable.ic_user_unlogin);
        map.put("text", getString(R.string.weibo_auto_item));
        listData.add(map);

        return listData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                auth();
                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete(Bundle bundle) {
        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (mAccessToken.isSessionValid()) {
            AccessTokenKeeper.writeAccessToken(this, mAccessToken);
            Log.v(CLASS_NAME, mAccessToken.getUid() + mAccessToken.getToken());
        } else {
//            String code = bundle.getString("code", "");
//            Log.v(CLASS_NAME, code);
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Log.v(CLASS_NAME, e.toString());
    }

    @Override
    public void onCancel() {
        Log.v(CLASS_NAME, "cancel");
    }

    private void auth() {
        if (null == mAuthInfo) {
            mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        }

        if (null == mSsoHandler) {
            mSsoHandler = new SsoHandler(this, mAuthInfo);
        }
        mSsoHandler.authorize(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mSsoHandler) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
