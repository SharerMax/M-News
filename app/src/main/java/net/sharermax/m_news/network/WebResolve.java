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
    public static final int START_UP_MAIN_PAGES_FLAG = 0x01;
    public static final int START_UP_NEXT_PAGES_FLAG = 0x02;
    private List<HashMap<String, String>> mValidData;
    private String mNextUrl;
    private TaskOverListener mTaskOverListener;
    private Context mContext;
    private boolean isFinished = true;

    public WebResolve(Context context) {
        mContext = context;
        mValidData = new ArrayList<HashMap<String, String>>();
        mValidData.clear();
    }
    
    public void startTask(int flag) {
        switch (flag) {
            case START_UP_MAIN_PAGES_FLAG:
                resolveData(START_UP_HOST_NAME + "news");
                break;
            case START_UP_NEXT_PAGES_FLAG:
                resolveData(mNextUrl);
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
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (null != response) {
                    Pattern urlListPattern = Pattern.compile(
                            "<a target=\"_blank\" href=\"(https?://.+?)\".*?>(.+?)</a>");
                    Matcher urlListMatcher = urlListPattern.matcher(response);
                    while (urlListMatcher.find()) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("title", urlListMatcher.group(2));
                        map.put("url", urlListMatcher.group(1));
                        mValidData.add(map);
                    }
                    Pattern nextUrlPattern = Pattern.compile("\"/(x\\?fnid=\\w+?)\"\\W?rel");
                    Matcher nextUrlMatcher = nextUrlPattern.matcher(response);
                    if (nextUrlMatcher.find()) {
                        mNextUrl = START_UP_HOST_NAME + nextUrlMatcher.group(1);
                        Log.v(CLASS_NAME, mNextUrl);
                    }
                }

                if (mTaskOverListener != null) {
                    mTaskOverListener.taskOver();
                }
                isFinished = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isFinished = true;
            }
        });
        Utility.getRequestQueue(mContext).add(stringRequest);
    }
}
