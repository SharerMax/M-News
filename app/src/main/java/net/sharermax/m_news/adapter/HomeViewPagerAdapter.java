package net.sharermax.m_news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import net.sharermax.m_news.fragment.HomeFragment;
import net.sharermax.m_news.fragment.NewsFragment;

/**
 * Author: SharerMax
 * Time  : 2015/4/15
 * E-Mail: mdcw1103@gmail.com
 */
public class HomeViewPagerAdapter  extends CacheFragmentStatePagerAdapter{

    private static final String [] TITLE = new String[] {"STARTUP", "GEEK"};
    private HomeFragment mHomeFragment;
    public HomeViewPagerAdapter(HomeFragment homeFragment,FragmentManager fm) {
        super(fm);
        mHomeFragment = homeFragment;
    }

    @Override
    protected Fragment createItem(int position) {
        Fragment fragment = new NewsFragment();
        ((NewsFragment)fragment).setOnNewsScrolledListerer(new NewsFragment.OnNewsScrolledListener() {
            @Override
            public void OnScrolled(int dy, boolean fistVisible) {
                mHomeFragment.onNewsScrolled(dy, fistVisible);
            }
        });

        return fragment;
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }


}
