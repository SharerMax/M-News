package net.sharermax.m_news.api.news;

import android.content.Context;

import net.sharermax.m_news.api.news.common.CommonAPI;
import net.sharermax.m_news.api.news.common.NewsData;
import net.sharermax.m_news.api.news.common.ResponseListener;
import net.sharermax.m_news.api.news.hackernews.HackerNewsAPI;
import net.sharermax.m_news.api.news.startup.StartUpNewsAPI;

import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/7/9
 * E-Mail: mdcw1103@gmail.com
 */
public class NewsDistribution {
    public static final int START_UP_MAIN_PAGES_FLAG = 0x01;
    public static final int START_UP_NEXT_PAGES_FLAG = 0x02;
    public static final int HACKER_NEWS_MAIN_PAGES_FLAG = 0x03;
    public static final int HACKER_NEWS_NEXT_PAGES_FLAG = 0x04;
    private ResponseListener<List<NewsData>> mListener;
    private Context mContext;
    private CommonAPI mStartUpAPI;
    private CommonAPI mHackerNewsAPI;
    public NewsDistribution(Context context, ResponseListener<List<NewsData>> listener) {
        mContext = context.getApplicationContext();
        mListener = listener;
    }

    public void start(int flag) {
        switch (flag) {
            case START_UP_MAIN_PAGES_FLAG:
                if (null == mStartUpAPI) {
                    mStartUpAPI = new StartUpNewsAPI(mContext, mListener);
                }
                mStartUpAPI.startMainPage();
                break;
            case START_UP_NEXT_PAGES_FLAG:
                mStartUpAPI.startNextPage();
                break;
            case HACKER_NEWS_MAIN_PAGES_FLAG:
                if (null == mHackerNewsAPI ) {
                    mHackerNewsAPI = new HackerNewsAPI(mContext, mListener);
                }
                mHackerNewsAPI.startMainPage();
                break;
            case HACKER_NEWS_NEXT_PAGES_FLAG:
                mHackerNewsAPI.startNextPage();
                break;
            default:
                break;
        }
    }
}
