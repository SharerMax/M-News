package net.sharermax.m_news.network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: SharerMax
 * Time  : 2015/2/26
 * E-Mail: mdcw1103@gmail.com
 */

public class WebResolve {
    static public String CLASS_TAG = "WebResolve";
    private List<HashMap<String, String>> mValidData = 
            new ArrayList<HashMap<String, String>>();
    private TaskOverListener mTaskOverListener;
    
    public void startTask() {
        WebResolveTask mWebResolveTask = new WebResolveTask();
        mWebResolveTask.execute("http://news.dbanotes.net/news");
    }
    
    public List<HashMap<String, String>> getValidData() {
        return mValidData;
        
    }
    
    public static interface TaskOverListener {
        public abstract void taskOver();
    }
    
    public void setTaskOverListener(TaskOverListener taskOverListener) {
        mTaskOverListener = taskOverListener;        
    }
    
    class WebResolveTask extends AsyncTask<String, Integer, String> {
        public String CLASS_TAG = "WebResolveTask";
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
                if (mHttpURLConnection != null) {
                    mHttpURLConnection.setDoInput(true);
                    mHttpURLConnection.setConnectTimeout(3000);
                    mHttpURLConnection.setRequestMethod("GET");
                    if (mHttpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader mBufferedReader = new BufferedReader(
                                new InputStreamReader(mHttpURLConnection.getInputStream()));
                        String lineData = null;
                        String webData = "";
                        while ((lineData = mBufferedReader.readLine()) != null) {
                            webData += lineData;
                        }
                        return  webData;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String webData) {
            Pattern mPattern = Pattern.compile(
                    "<a target=\"_blank\" href=\"(https?://.+?)\".*?>(\\w.+?)</a>");
            Matcher mMatcher = mPattern.matcher(webData);
            while (mMatcher.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", mMatcher.group(2));
                map.put("url", mMatcher.group(1));
                mValidData.add(map);
            }
            if (mTaskOverListener != null) {
                mTaskOverListener.taskOver();
            }
        }
    }
}
