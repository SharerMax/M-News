package net.sharermax.m_news.api.news.common;


/**
 * Author: SharerMax
 * Time  : 2015/7/8
 * E-Mail: mdcw1103@gmail.com
 */
public abstract class CommonAPI {
    private String mHostUrl;
    public CommonAPI(String hostUrl) {
        mHostUrl = hostUrl;
    }
    public void startMainPage() {
        parsePage(mHostUrl);
    }
    abstract public void startNextPage();
    abstract public void parsePage(String url);
    abstract public void cancel();
}
