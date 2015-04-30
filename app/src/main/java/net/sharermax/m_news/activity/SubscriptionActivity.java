package net.sharermax.m_news.activity;

import android.os.Bundle;

import net.sharermax.m_news.R;
import net.sharermax.m_news.fragment.SubscriptionFragment;


/**
 * Author: SharerMax
 * Time  : 2015/4/16
 * E-Mail: mdcw1103@gmail.com
 */
public class SubscriptionActivity extends AbsActivity {

    public static final String CLASS_NAME = "SubscriptionActivity";
    private SubscriptionFragment mSubFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSubFragment = new SubscriptionFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, mSubFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
