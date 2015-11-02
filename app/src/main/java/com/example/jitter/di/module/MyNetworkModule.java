package com.example.jitter.di.module;

import com.example.jitter.data.TwitterApi;
import com.example.jitter.di.scope.PerApp;
import com.example.jitter.util.Constants;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class MyNetworkModule {

    @Provides
    @PerApp
    TwitterApi provideTwitterApi(Retrofit retrofit) {
        return retrofit.create(TwitterApi.class);
    }

    @Provides
    @PerApp
    Retrofit provideRetrofit(GsonConverterFactory gson, RxJavaCallAdapterFactory rx) {
        return new Retrofit.Builder()
                .baseUrl(Constants.TWITTER_BASE_API)
                .addConverterFactory(gson)
                .addCallAdapterFactory(rx)
                .build();
    }

    @Provides
    @PerApp
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @PerApp
    RxJavaCallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }
}
