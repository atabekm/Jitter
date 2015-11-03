package com.example.jitter.di;

import android.app.Application;
import android.content.Context;

import com.example.jitter.di.component.DaggerMyComponent;
import com.example.jitter.di.component.MyComponent;
import com.example.jitter.di.module.MyNetworkModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {
    MyComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Realm for the application
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .name("jitter.realm")
                .build();

        // Set above configuration as default, and it can be accessed from everywhere
        Realm.setDefaultConfiguration(configuration);

        component = DaggerMyComponent
                .builder()
                .myNetworkModule(new MyNetworkModule())
                .build();
    }

    public static MyComponent getComponent(Context context) {
        return ((MyApp) context.getApplicationContext()).component;
    }
}
