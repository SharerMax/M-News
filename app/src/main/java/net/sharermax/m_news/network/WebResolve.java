package net.sharermax.m_news.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private List<HashMap<String, String>> mValidData = 
            new ArrayList<HashMap<String, String>>();
    private String mNextUrl;
    private TaskOverListener mTaskOverListener;
    private WebResolveTask mWebResolveTask;
    
    public void startTask(int flag) {
        mWebResolveTask = new WebResolveTask();
        switch (flag) {
            case START_UP_MAIN_PAGES_FLAG:
                mWebResolveTask.execute(START_UP_HOST_NAME + "news");
                break;
            case START_UP_NEXT_PAGES_FLAG:
                mWebResolveTask.execute(mNextUrl);
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
        if (null != mWebResolveTask) {
            return AsyncTask.Status.FINISHED == mWebResolveTask.getStatus();
        }
        return true;
    }
    
    class WebResolveTask extends AsyncTask<String, Integer, String> {
        static public final String CLASS_TAG = "WebResolveTask";
        @Override
        protected String doInBackground(String... urls) {
            String webData = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection != null) {
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setRequestMethod("GET");
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(httpURLConnection.getInputStream()));
                        String lineData = null;
                        while ((lineData = bufferedReader.readLine()) != null) {
                            webData += lineData;
                        }
                        bufferedReader.close();
                    }
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                Log.v(CLASS_TAG, "TTT");
            }
            return webData;
        }

        @Override
        protected void onPostExecute(String webData) {
            if (null != webData) {
                Pattern urlListPattern = Pattern.compile(
                        "<a target=\"_blank\" href=\"(https?://.+?)\".*?>(.+?)</a>");
                Matcher urlListMatcher = urlListPattern.matcher(webData);
                while (urlListMatcher.find()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("title", urlListMatcher.group(2));
                    map.put("url", urlListMatcher.group(1));
                    mValidData.add(map);
                }
                Pattern nextUrlPattern = Pattern.compile("\"/(x\\?fnid=\\w+?)\"\\W?rel");
                Matcher nextUrlMatcher = nextUrlPattern.matcher(webData);
                if (nextUrlMatcher.find()) {
                    mNextUrl = START_UP_HOST_NAME + nextUrlMatcher.group(1);
                    Log.v(CLASS_TAG, mNextUrl);
                }
            }

            if (mTaskOverListener != null) {
                mTaskOverListener.taskOver();
            }
        }
    }
}
