package net.sharermax.m_news.api.news.common;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author: SharerMax
 * Time  : 2015/8/11
 * E-Mail: mdcw1103@gmail.com
 */
public class OkHttpStack implements HttpStack {
    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        OkHttpClient client = new OkHttpClient();
        Headers.Builder headersBuilder = new Headers.Builder();
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.putAll(request.getHeaders());
        headerMap.putAll(additionalHeaders);
        for (String headerName : additionalHeaders.keySet()) {
            headersBuilder.add(headerName, headerMap.get(headerName));
        }
        Headers headers = headersBuilder.build();
        com.squareup.okhttp.Request.Builder requestBuilder = new com.squareup.okhttp.Request.Builder();
        setConnectionParametersForRequest(requestBuilder, request);
        com.squareup.okhttp.Request okRequest = requestBuilder.headers(headers).build();
        client.setConnectTimeout(request.getTimeoutMs(), TimeUnit.MILLISECONDS);
        Response response = client.newCall(okRequest).execute();
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        StatusLine reponseStatus = new BasicStatusLine(protocolVersion, response.code(), response.message());
        BasicHttpResponse basicHttpResponse = new BasicHttpResponse(reponseStatus);
        ResponseBody body = response.body();
        if (body != null) {
            BasicHttpEntity entity = new BasicHttpEntity();
            entity.setContent(body.byteStream());
            entity.setContentLength(body.contentLength());
            entity.setContentEncoding(body.contentType().charset().name());
            entity.setContentType(body.contentType().toString());
            basicHttpResponse.setEntity(entity);
        }

        for (String headerName : response.headers().names()) {
            basicHttpResponse.addHeader(headerName, response.headers().get(headerName));
        }

        return basicHttpResponse;
    }

    private void setConnectionParametersForRequest(com.squareup.okhttp.Request.Builder builder, Request<?> request) throws AuthFailureError {
        switch (request.getMethod()) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                byte []bytes = request.getBody();
                if (request.getBody() != null) {
                    builder.post(RequestBody.create(null, bytes));
                }
                builder.url(request.getUrl());
                break;
            case Request.Method.GET:
                builder.url(request.getUrl());
                break;
            case Request.Method.POST:
                addBodyIfExists(builder, request);
                builder.url(request.getUrl());
                break;
            case Request.Method.PUT:
                addBodyIfExists(builder, request);
                builder.url(request.getUrl());
                break;
            case Request.Method.HEAD:
                builder.url(request.getUrl());
                break;
            case Request.Method.DELETE:
                builder.url(request.getUrl());
                break;
            case Request.Method.OPTIONS:
                builder.url(request.getUrl());
                break;
            case Request.Method.PATCH:
                addBodyIfExists(builder, request);
                builder.url(request.getUrl());
                break;
            case Request.Method.TRACE:
                builder.url(request.getUrl());
                break;
            default:
                break;
        }
    }

    private void addBodyIfExists(com.squareup.okhttp.Request.Builder builder,Request<?> request) throws AuthFailureError {
        byte []bytes = request.getBody();
        if (request.getBody() != null) {
            builder.post(RequestBody.create(null, bytes));
        }
    }

}
