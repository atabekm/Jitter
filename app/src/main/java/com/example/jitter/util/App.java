package com.example.jitter.util;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Realm for the application
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .name("jitter.realm")
                .build();

        // Set above configuration as default, and it can be accessed from everywhere
        Realm.setDefaultConfiguration(configuration);
    }
}
