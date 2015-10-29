package com.example.jitter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jitter.R;
import com.example.jitter.adapter.TweetAdapter;
import com.example.jitter.data.Tweet;
import com.example.jitter.data.TweetJson;
import com.example.jitter.data.TwitterService;
import com.example.jitter.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TweetsFragment extends Fragment {
    private TweetAdapter mAdapter;
    private List<Tweet> mData;
    private CompositeSubscription subscription = new CompositeSubscription();
    private String twitterName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twitterName = getArguments().getString(Constants.TWITTER_USER_NAME);

        mData = new ArrayList<>();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (subscription == null || subscription.isUnsubscribed()) {
            subscription = new CompositeSubscription();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mData = new ArrayList<>();
        mAdapter = new TweetAdapter(mData, getContext());
        mRecyclerView.setAdapter(mAdapter);

        getData();

        return view;
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.TWITTER_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        TwitterService twitterService = retrofit.create(TwitterService.class);

        subscription.add(
            twitterService.getTimeline(twitterName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TweetJson>>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.updateResults(mData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());
                    }

                    @Override
                    public void onNext(List<TweetJson> tweets) {
                        for (TweetJson t : tweets) {
                            Tweet tw = new Tweet();
                            if (t.retweeted_status != null) {
                                tw.setAuthor("@" + t.retweeted_status.user.screen_name);
                                tw.setText(t.retweeted_status.text);
                                tw.setAvatarUrl(t.retweeted_status.user.profile_image_url.replace("_normal", ""));
                                tw.setIsRetweet(true);
                            } else {
                                tw.setAuthor("@" + t.user.screen_name);
                                tw.setText(t.text);
                                tw.setAvatarUrl(t.user.profile_image_url.replace("_normal", ""));
                                tw.setIsRetweet(false);
                            }
                            mData.add(tw);
                        }
                    }
                }
            )
        );
    }
}
