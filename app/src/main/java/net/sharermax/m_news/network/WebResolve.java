package net.sharermax.m_news.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.sharermax.m_news.support.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: SharerMax
 * Time  : 2015/2/26
 * E-Mail: mdcw1103@gmail.com
 */

public class WebResolve {
    public static final String CLASS_NAME = "WebResolve";
    public static final String START_UP_HOST_NAME = "http://news.dbanotes.net/";
    public static final String HACKER_NEWS_HOST_NAME = "https://news.ycombinator.com/";
    public static final int START_UP_MAIN_PAGES_FLAG = 0x01;
    public static final int START_UP_NEXT_PAGES_FLAG = 0x02;
    public static final int HACKER_NEWS_MAIN_PAGES_FLAG = 0x03;
    public static final int HACKER_NEWS_NEXT_PAGES_FLAG = 0x04;
    private List<HashMap<String, String>> mValidData;
    private String mNextUrl;
    private TaskOverListener mTaskOverListener;
    private Context mContext;
    private boolean isFinished = true;
    private Pattern mUrlListPattern;
    private Pattern mNextUrlPattern;

    public WebResolve(Context context) {
        mContext = context;
        mValidData = new ArrayList<HashMap<String, String>>();
        mValidData.clear();
    }
    
    public void startTask(int flag) {
        switch (flag) {
            case START_UP_MAIN_PAGES_FLAG:
                mUrlListPattern = Pattern.compile(
                        "<a target=\"_blank\" href=\"(https?://.+?)\".*?>(.+?)</a>");
                mNextUrlPattern = Pattern.compile("\"/(x\\?fnid=\\w+?)\"\\W?rel");
                resolveData(START_UP_HOST_NAME);
                break;
            case START_UP_NEXT_PAGES_FLAG:
                resolveData(START_UP_HOST_NAME + mNextUrl);
                break;
            case HACKER_NEWS_MAIN_PAGES_FLAG:
                mUrlListPattern = Pattern.compile(
                        "</span><a href=\"(https?://.+?)\">(.+?)<");
                mNextUrlPattern = Pattern.compile("\"(news\\?p=\\d+?)\"");
                resolveData(HACKER_NEWS_HOST_NAME);
                break;
            case HACKER_NEWS_NEXT_PAGES_FLAG:
                resolveData(HACKER_NEWS_HOST_NAME + mNextUrl);
                break;
        }
    }

    public List<HashMap<String, String>> getValidData() {
        //not null
        return mValidData;
    }
    
    public static interface TaskOverListener {
        public abstract void taskOver();
    }
    
    public void setTaskOverListener(TaskOverListener taskOverListener) {
        mTaskOverListener = taskOverListener;        
    }

    public void cleanData() {
        if (!mValidData.isEmpty()) {
            mValidData.clear();
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    private void resolveData(String url) {
        isFinished = false;
        StringRequest stringRequest = new StringRequest(url, new ResponseListener(),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isFinished = true;
                    }
                });
        Utility.getRequestQueue(mContext).add(stringRequest);
    }

    private class ResponseListener implements Response.Listener<String> {

        @Override
        public void onResponse(String response) {
            if (null != response) {
                Matcher urlListMatcher = mUrlListPattern.matcher(response);
                while (urlListMatcher.find()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("title", urlListMatcher.group(2));
                    map.put("url", urlListMatcher.group(1));
                    mValidData.add(map);
                }
                Matcher nextUrlMatcher = mNextUrlPattern.matcher(response);
                if (nextUrlMatcher.find()) {
                    mNextUrl = nextUrlMatcher.group(1);
                    Log.v(CLASS_NAME, mNextUrl);
                }
            }

            if (mTaskOverListener != null) {
                mTaskOverListener.taskOver();
            }
            isFinished = true;
        }
    }
}
