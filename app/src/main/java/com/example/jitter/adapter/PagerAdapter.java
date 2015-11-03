package com.example.jitter.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jitter.data.TweetRealm;
import com.example.jitter.fragment.TweetsFragment;
import com.example.jitter.util.Constants;

public class PagerAdapter extends FragmentPagerAdapter {
    private final int NUMBER_OF_FRAGMENTS = 2;
    private String twitterName;

    public PagerAdapter(FragmentManager fm, String twitterName) {
        super(fm);
        this.twitterName = twitterName;
    }

    @Override
    public Fragment getItem(int position) {
        // create fragment with argument according to its type (timeline, or favorites)
        switch (position) {
            case TweetRealm.TYPE_TIMELINE:
                Bundle argsTimeline = new Bundle();
                argsTimeline.putString(Constants.TWITTER_USER_NAME, twitterName);
                argsTimeline.putInt(Constants.ADAPTER_TYPE, TweetRealm.TYPE_TIMELINE);
                argsTimeline.putBoolean(Constants.ADAPTER_LIST_CLICKABLE, false);
                argsTimeline.putInt(Constants.ADAPTER_DOWNLOAD_TYPE, Constants.ADAPTER_DOWNLOAD_TYPE_TIMELINE);

                TweetsFragment fragmentTimeline = new TweetsFragment();
                fragmentTimeline.setArguments(argsTimeline);
                return fragmentTimeline;

            case TweetRealm.TYPE_FAVORITES:
                Bundle argsFavorites = new Bundle();
                argsFavorites.putString(Constants.TWITTER_USER_NAME, twitterName);
                argsFavorites.putInt(Constants.ADAPTER_TYPE, TweetRealm.TYPE_FAVORITES);
                argsFavorites.putBoolean(Constants.ADAPTER_LIST_CLICKABLE, false);
                argsFavorites.putInt(Constants.ADAPTER_DOWNLOAD_TYPE, Constants.ADAPTER_DOWNLOAD_TYPE_FAVORITES);

                TweetsFragment fragmentFavorites = new TweetsFragment();
                fragmentFavorites.setArguments(argsFavorites);
                return fragmentFavorites;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_FRAGMENTS;
    }
}
