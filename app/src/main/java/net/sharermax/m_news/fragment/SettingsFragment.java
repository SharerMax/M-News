package net.sharermax.m_news.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.sharermax.m_news.R;

/**
 * Author: SharerMax
 * Time  : 2015/3/22
 * E-Mail: mdcw1103@gmail.com
 */
public class SettingsFragment extends PreferenceFragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefer_main);
    }
}
