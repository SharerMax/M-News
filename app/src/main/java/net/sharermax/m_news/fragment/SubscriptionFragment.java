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
    private CheckBoxPreference mHackerPre;
    private boolean mStartUpEnable;
    private boolean mHackerEnable;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.subscription);
        initPreferenceItem();
    }

    private void initPreferenceItem() {
        mStartUpPre = (CheckBoxPreference)findPreference(Setting.KEY_SUB_STARTUP);
        mHackerPre = (CheckBoxPreference)findPreference(Setting.KEY_SUB_HACKERNEWS);
        mStartUpPre.setOnPreferenceChangeListener(this);
        mHackerPre.setOnPreferenceChangeListener(this);
        mStartUpEnable = Setting.getInstance(getActivity()).getBoolen(Setting.KEY_SUB_STARTUP, true);
        mHackerEnable = Setting.getInstance(getActivity()).getBoolen(Setting.KEY_SUB_HACKERNEWS, true);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(Setting.KEY_SUB_STARTUP) &&
                mStartUpEnable != (boolean)newValue) {
            getActivity().setResult(Activity.RESULT_OK);
            return true;
        }

        if (preference.getKey().equals(Setting.KEY_SUB_HACKERNEWS) &&
                mHackerEnable != (boolean)newValue) {
            getActivity().setResult(Activity.RESULT_OK);
            return true;
        }

        getActivity().setResult(Activity.RESULT_CANCELED);
        return true;
    }

}
