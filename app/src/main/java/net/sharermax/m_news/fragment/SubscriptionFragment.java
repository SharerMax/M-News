package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.Setting;


/**
 * Author: SharerMax
 * Time  : 2015/4/16
 * E-Mail: mdcw1103@gmail.com
 */
public class SubscriptionFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference mStartUpPre;
    private CheckBoxPreference mGeekPre;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.subscription);
        initPreferenceItem();
    }

    private void initPreferenceItem() {
        mStartUpPre = (CheckBoxPreference)findPreference(Setting.KEY_SUB_STARTUP);
        mGeekPre = (CheckBoxPreference)findPreference(Setting.KEY_SUB_HACKERNEWS);
        mStartUpPre.setOnPreferenceChangeListener(this);
        mGeekPre.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        getActivity().setResult(Activity.RESULT_OK);
        return true;
    }

}
