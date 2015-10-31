package com.example.jitter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jitter.R;
import com.example.jitter.adapter.RealmAdapter;
import com.example.jitter.data.TweetJson;
import com.example.jitter.data.TweetRealm;
import com.example.jitter.data.TwitterService;
import com.example.jitter.util.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TweetsFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CompositeSubscription subscription = new CompositeSubscription();
    private String twitterName;
    private Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twitterName = getArguments().getString(Constants.TWITTER_USER_NAME);

        realm = Realm.getDefaultInstance();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);
        String maxId = null;

        RealmResults<TweetRealm> data = realm.where(TweetRealm.class).equalTo("owner", twitterName).findAll();
        Log.e("TAG", "Number of search results: " + data.size());
        if (data.size() > 0) {
            maxId = data.max("id").toString();
            Log.e("TAG", "Number of max id: " + maxId);
        }
        data.sort("id", RealmResults.SORT_ORDER_DESCENDING);
        RealmAdapter adapter = new RealmAdapter(getActivity(), data);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        getData(maxId);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("TAG", "onRefresh");
                mSwipeRefreshLayout.setRefreshing(true);
                String id = realm.where(TweetRealm.class).equalTo("owner", twitterName).findAll().max("id").toString();
                Log.e("TAG", "last twitter id onRefresh: " + id);
                getData(id);
            }
        });

        return view;
    }

    private void getData(String maxId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.TWITTER_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        TwitterService twitterService = retrofit.create(TwitterService.class);

        subscription.add(
            twitterService.getTimeline(twitterName, maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TweetJson>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "onCompleted");
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());
                    }

                    @Override
                    public void onNext(List<TweetJson> tweets) {
                        Log.e("TAG", "Number of new tweets found: " + tweets.size());
                        List<TweetRealm> mData = new ArrayList<>();
                        for (TweetJson t : tweets) {
                            TweetRealm tw = new TweetRealm();
                            tw.setId(t.id);
                            tw.setOwner(t.user.screen_name);
                            if (t.retweeted_status != null) {
                                tw.setUserName("@" + t.retweeted_status.user.screen_name);
                                tw.setMessage(t.retweeted_status.text);
                                tw.setImageUrl(t.retweeted_status.user.profile_image_url.replace("_normal", ""));
                                tw.setIsRetweet(true);
                            } else {
                                tw.setUserName("@" + t.user.screen_name);
                                tw.setMessage(t.text);
                                tw.setImageUrl(t.user.profile_image_url.replace("_normal", ""));
                                tw.setIsRetweet(false);
                            }
                            mData.add(tw);
                        }

                        Log.e("TAG", "Number of new tweets to be added to DB: " + mData.size());
                        if (tweets.size() > 0) {
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(mData);
                            realm.commitTransaction();
                        }
                    }
                }
            )
        );
    }
}
