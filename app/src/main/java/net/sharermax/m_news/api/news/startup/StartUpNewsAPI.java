package net.sharermax.m_news.api.news.startup;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.sharermax.m_news.api.news.common.CommonAPI;
import net.sharermax.m_news.api.news.common.NewsData;
import net.sharermax.m_news.api.news.common.ResponseListener;
import net.sharermax.m_news.support.Utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/7/8
 * E-Mail: mdcw1103@gmail.com
 */
public class StartUpNewsAPI extends CommonAPI {
    public static final String START_UP_HOST_NAME = "http://news.dbanotes.net";
    private ResponseListener<List<NewsData>> mListener;
    private Context mContext;
    private Response.ErrorListener mErrorListener;
    private Response.Listener<String> mSuccessListener;
    private String mNextPageUrl;
    private Request mRequest;
    public StartUpNewsAPI(Context context, ResponseListener<List<NewsData>> listener) {
        super(START_UP_HOST_NAME);
        mSuccessListener = new ResponseSuccessListener();
        mErrorListener = new ResponseErrorListener();
        mContext = context;
        mListener = listener;
    }
    
    @Override
    public void startNextPage() {
        if (null == mNextPageUrl) {
            mListener.onResponse(null);
            return;
        }
        parsePage(mNextPageUrl);
    }

    @Override
    public void parsePage(String url) {
        mRequest = new StringRequest(Request.Method.GET, url, mSuccessListener, mErrorListener);
        mRequest.setTag(Utility.VOLLEY_TAG_NEWS);
        Utility.getRequestQueue(mContext).add(mRequest);
    }

    private class ResponseSuccessListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            if (null != response && !response.isEmpty()) {
                Document document = Jsoup.parse(response, START_UP_HOST_NAME);
                Elements tbody = document.select("tbody tbody");
                Elements titles = tbody.select("td.title");
                Elements title = titles.select("a");
                Elements subTexts = tbody.select("td.subtext");
                int size = title.size();
                ArrayList<NewsData> list = new ArrayList<>();
                for (int i = 0; i < size - 1; i++) {
                    Element t = title.get(i);
                    NewsData newsData = new NewsData(
                            t.text(),
                            t.absUrl("href"),
                            subTexts.get(i).text().split("\\|")[0].trim());
                    list.add(newsData);
                }
                mNextPageUrl = START_UP_HOST_NAME + title.last().attr("href");
                mListener.onResponse(list);
            }
        }
    }

    private class ResponseErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            mListener.onResponse(null);
        }
    }

    @Override
    public void cancel() {
        mRequest.cancel();
    }
}
