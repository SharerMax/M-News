package net.sharermax.m_news.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.sharermax.m_news.R;

/**
 * Author: SharerMax
 * Time  : 2015/3/29
 * E-Mail: mdcw1103@gmail.com
 */
public class LicenseFragment extends Fragment {
    private WebView mWebView;
    public LicenseFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.license_fragment, container, false);
        mWebView = (WebView)view.findViewById(R.id.web_view);
        mWebView.loadUrl("file:///android_asset/licenses.html");
        getActivity().setTitle(R.string.open_source_license);
        return view;
    }
}
