package net.sharermax.m_news.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.SettingsActivity;
import net.sharermax.m_news.support.Setting;
import net.sharermax.m_news.support.Utility;

/**
 * Author: SharerMax
 * Time  : 2015/3/22
 * E-Mail: mdcw1103@gmail.com
 */
public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    public static final String CLASS_NAME = "SettingsFragment";
    private SwitchPreference mCardViewPreference;
    private SwitchPreference mSwipeBackPreference;
    private SwitchPreference mDListAnimationPreference;
    private Preference mWeiboPreference;
    private Preference mTwitterPreference;
    private Preference mGithubPreference;
    private Preference mLicensePreference;
    private Preference mVersionPreference;
    private boolean mCardViewEnable;
    private boolean mListAnimationEnable;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefer_main);
        mCardViewPreference = (SwitchPreference)findPreference(Setting.KEY_USE_CARD_VIEW);
        mSwipeBackPreference = (SwitchPreference)findPreference(Setting.KEY_SWIPE_BACK);
        mDListAnimationPreference = (SwitchPreference)findPreference(Setting.KEY_DISABLE_LIST_ANIMATION);
        mWeiboPreference = findPreference(Setting.KEY_WEIBO);
        mTwitterPreference = findPreference(Setting.KEY_TWITTER);
        mGithubPreference = findPreference(Setting.KEY_GITHUB);
        mLicensePreference = findPreference(Setting.KEY_LICENSE);
        mVersionPreference = findPreference(Setting.KEY_VERSION);
        mVersionPreference.setSummary(Utility.getVersionInfo(getActivity()));
        mSwipeBackPreference.setOnPreferenceClickListener(this);
        mWeiboPreference.setOnPreferenceClickListener(this);
        mTwitterPreference.setOnPreferenceClickListener(this);
        mGithubPreference.setOnPreferenceClickListener(this);
        mLicensePreference.setOnPreferenceClickListener(this);
        Setting setting = Setting.getInstance(getActivity().getApplicationContext());
        mCardViewEnable = setting.getBoolen(Setting.KEY_USE_CARD_VIEW, true);
        mListAnimationEnable = setting.getBoolen(Setting.KEY_DISABLE_LIST_ANIMATION, false);
//        mVersionPreference.setOnPreferenceClickListener(this);

        mCardViewPreference.setOnPreferenceChangeListener(this);
        mDListAnimationPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if ((mCardViewEnable != (boolean)newValue)
                || (mListAnimationEnable != (boolean)newValue)) {
            getActivity().setResult(Activity.RESULT_OK);
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mSwipeBackPreference) {
            Toast.makeText(getActivity(), getString(R.string.toast_you_need_restart), Toast.LENGTH_LONG).show();
            return true;
        }

        if (preference == mWeiboPreference) {
            openWebSite(getString(R.string.author_Weibo_url));
            return true;
        }

        if(preference == mTwitterPreference) {
            openWebSite(getString(R.string.author_Twitter_url));
            return true;
        }

        if (preference == mGithubPreference) {
            openWebSite(getString(R.string.github_url));
            return true;
        }

        if (preference == mLicensePreference) {
            ((SettingsActivity)getActivity()).launcherActivity(SettingsActivity.FLAG_LICENSE);
            return true;
        }

        return false;
    }

    private void openWebSite(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
