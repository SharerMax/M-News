package net.sharermax.m_news.api.news.common;

/**
 * Author: SharerMax
 * Time  : 2015/7/8
 * E-Mail: mdcw1103@gmail.com
 */
public interface ResponseListener<T> {
    void onResponse(T response);
}
