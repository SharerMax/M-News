package net.sharermax.m_news.activity;

import android.app.FragmentManager;
import android.os.Bundle;

import net.sharermax.m_news.R;
import net.sharermax.m_news.fragment.SettingsFragment;

/**
 * Author: SharerMax
 * Time  : 2015/3/22
 * E-Mail: mdcw1103@gmail.com
 */
public class SettingsActivity extends AbsActivity {
    SettingsFragment mSettingsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        FragmentManager fragmentManager = getFragmentManager();
        if (null == mSettingsFragment) {
            mSettingsFragment = new SettingsFragment();
        }
        fragmentManager.beginTransaction().replace(R.id.container, mSettingsFragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

}
