package net.sharermax.m_news.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.sharermax.m_news.support.Utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Author: SharerMax
 * Time  : 2015/2/26
 * E-Mail: mdcw1103@gmail.com
 */

public class WebResolve {
    public static final String CLASS_NAME = "WebResolve";
    public static final String START_UP_HOST_NAME = "http://news.dbanotes.net";
    public static final String HACKER_NEWS_HOST_NAME = "https://news.ycombinator.com/";
    public static final int START_UP_MAIN_PAGES_FLAG = 0x01;
    public static final int START_UP_NEXT_PAGES_FLAG = 0x02;
    public static final int HACKER_NEWS_MAIN_PAGES_FLAG = 0x03;
    public static final int HACKER_NEWS_NEXT_PAGES_FLAG = 0x04;
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_URL = "url";
    private List<HashMap<String, String>> mValidData;
    private String mNextUrl;
    private TaskOverListener mTaskOverListener;
    private Context mContext;
    private boolean isFinished = true;

    public WebResolve(Context context) {
        mContext = context;
        mValidData = new ArrayList<>();
        mValidData.clear();
    }
    
    public void startTask(int flag) {
        switch (flag) {
            case START_UP_MAIN_PAGES_FLAG:
                resolveData(START_UP_HOST_NAME);
                break;
            case START_UP_NEXT_PAGES_FLAG:
                resolveData(START_UP_HOST_NAME + mNextUrl);
                break;
            case HACKER_NEWS_MAIN_PAGES_FLAG:
                resolveData(HACKER_NEWS_HOST_NAME);
                break;
            case HACKER_NEWS_NEXT_PAGES_FLAG:
                resolveData(HACKER_NEWS_HOST_NAME + mNextUrl);
                Log.v(CLASS_NAME, HACKER_NEWS_HOST_NAME + mNextUrl);
                break;
        }
    }

    public List<HashMap<String, String>> getValidData() {
        //not null
        return mValidData;
    }
    
    public interface TaskOverListener {
        void taskOver(List<HashMap<String, String>> dataList);
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
        cleanData();
        StringRequest stringRequest = new StringRequest(url, new ResponseListener(),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mTaskOverListener != null) {
                            mTaskOverListener.taskOver(mValidData);
                        }
                        isFinished = true;
                    }
                });
        Utility.getRequestQueue(mContext).add(stringRequest);
    }

    private class ResponseListener implements Response.Listener<String> {

        @Override
        public void onResponse(String response) {
            if (null != response) {
                Document document = Jsoup.parse(response);
                Elements titles = document.select("td.title");
                Elements title = titles.select("a");
                int size = title.size();
                for (int i=0; i<size-1; i++) {
                    Element t = title.get(i);
                    Log.v(CLASS_NAME, "" + i + t.text() + t.attr("href"));
                    HashMap<String, String> map = new HashMap<>();
                    map.put(FIELD_TITLE, t.text());
                    map.put(FIELD_URL, t.attr("href"));
                    mValidData.add(map);
                }
                mNextUrl = title.last().attr("href");
            }

            if (mTaskOverListener != null) {
                mTaskOverListener.taskOver(mValidData);
            }
            isFinished = true;
        }
    }
}
