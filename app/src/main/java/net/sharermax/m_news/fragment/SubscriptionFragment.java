package net.sharermax.m_news.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.sharermax.m_news.R;


/**
 * Author: SharerMax
 * Time  : 2015/4/16
 * E-Mail: mdcw1103@gmail.com
 */
public class SubscriptionFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.subscription);
    }
}
