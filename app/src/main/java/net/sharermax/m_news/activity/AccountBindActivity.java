package net.sharermax.m_news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import net.sharermax.m_news.R;
import net.sharermax.m_news.adapter.AccountBindAdapter;
import net.sharermax.m_news.support.AccessTokenKeeper;
import net.sharermax.m_news.support.Constants;
import net.sharermax.m_news.support.UserHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: SharerMax
 * Time  : 2015/3/31
 * E-Mail: mdcw1103@gmail.com
 */
public class AccountBindActivity extends AbsActivity
        implements  WeiboAuthListener, AdapterView.OnItemClickListener, UserHelper.OnGetUserInfoListener {
    public static final String CLASS_NAME = "AccountBindActivity";
    public static final String FLAG_CIRCLE_IMAGE = "circle_image";
    public static final String FLAG_TEXT = "text";
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private ListView mListView;
    private AccountBindAdapter mAdapter;
    private List<Map<String, Object>> mlist;
    private UserHelper mUserHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountbind_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mUserHelper = UserHelper.getInstance(this);
        if (mAccessToken.isSessionValid()) {
            if (mUserHelper.isUserInfoVailed()) {
                mlist = getListData(mUserHelper.readUserInfo());
            } else {
                mUserHelper.getUserInfo(mAccessToken.getToken(), mAccessToken.getUid(), this);
                mlist = getListData();
            }
        } else {
            mlist = getListData();
        }

        mListView = (ListView)findViewById(R.id.accountbind_listview);
        mAdapter = new AccountBindAdapter(this, mlist);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getListData() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FLAG_CIRCLE_IMAGE, R.drawable.ic_user_unlogin);
        map.put(FLAG_TEXT, getString(R.string.weibo_auto_item));
        return getListData(map);
    }

    private List<Map<String, Object>> getListData(Map map) {
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        if (map.containsKey(FLAG_CIRCLE_IMAGE) || map.containsKey(FLAG_TEXT)) {
            listData.add(map);
            return listData;
        }
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(FLAG_CIRCLE_IMAGE, map.get(UserHelper.KEY_PROFILE_IMAGE));
        hashMap.put(FLAG_TEXT, (String)map.get(UserHelper.KEY_SCREEN_NAME));
        listData.add(hashMap);
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
            mUserHelper.getUserInfo(mAccessToken.getToken(), mAccessToken.getUid(), this);
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

    @Override
    public void onResponse(Map userInfo) {
        Log.v(CLASS_NAME, "REPONSE");
        mlist = getListData(userInfo);
        mAdapter.notifyDataSetChanged();
    }
}
