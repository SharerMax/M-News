package net.sharermax.m_news.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import net.sharermax.m_news.R;
import net.sharermax.m_news.fragment.LicenseFragment;
import net.sharermax.m_news.fragment.SettingsFragment;

/**
 * Author: SharerMax
 * Time  : 2015/3/22
 * E-Mail: mdcw1103@gmail.com
 */
public class SettingsActivity extends AbsActivity {
    public static final String EXTRA_FLAG = "flag";
    public static final int FLAG_SETTING = 0x00;
    public static final int FLAG_LICENSE = 0x01;
    private Fragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Intent intent = getIntent();
        int flag = intent.getIntExtra(EXTRA_FLAG, FLAG_SETTING);
        setUpView(flag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setUpView(int flag) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (flag) {
            case FLAG_SETTING:
                mFragment = new SettingsFragment();
                break;
            case FLAG_LICENSE:
                mFragment = new LicenseFragment();
                break;
            default:
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
    }

    public void launcherActivity(int flag) {
        Intent intent = new Intent();
        intent.setClass(this, SettingsActivity.class);
        intent.putExtra(EXTRA_FLAG, flag);
        this.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
//        enableOverrideFinishAnimation(false);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
