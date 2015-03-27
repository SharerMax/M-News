package net.sharermax.m_news.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.Setting;

/**
 * Author: SharerMax
 * Time  : 2015/3/22
 * E-Mail: mdcw1103@gmail.com
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private SwitchPreference mAutoRefreshPreference;
    private SwitchPreference mSwipeBackPreference;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefer_main);
        mAutoRefreshPreference = (SwitchPreference)findPreference(Setting.KEY_USE_CARD_VIEW);
        mSwipeBackPreference = (SwitchPreference)findPreference(Setting.KEY_SWIPE_BACK);
        mAutoRefreshPreference.setOnPreferenceClickListener(this);
        mSwipeBackPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mAutoRefreshPreference
                || preference == mSwipeBackPreference) {
            Toast.makeText(getActivity(), getString(R.string.toast_you_need_restart), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
