package net.sharermax.m_news.support;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Author: SharerMax
 * Time  : 2015/5/13
 * E-Mail: mdcw1103@gmail.com
 */
public class StethoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
//        LeakCanary.install(this);
    }
}
