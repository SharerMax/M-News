package net.sharermax.m_news.api.news.common;

/**
 * Author: SharerMax
 * Time  : 2015/7/9
 * E-Mail: mdcw1103@gmail.com
 */
public class NewsData {
    public final String title;
    public final String url;
    public final String subText;

    public NewsData(String title, String url, String subText) {
        this.title = title;
        this.url = url;
        this.subText = subText;
    }
}
