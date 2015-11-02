package com.example.jitter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jitter.R;
import com.example.jitter.activity.FollowersActivity;
import com.example.jitter.adapter.RealmAdapter;
import com.example.jitter.data.TweetTuple;
import com.example.jitter.data.TweetJson;
import com.example.jitter.data.TweetRealm;
import com.example.jitter.data.TwitterService;
import com.example.jitter.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TweetsFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CompositeSubscription subscription;
    private String twitterName;
    private int fragmentType;
    private boolean fragmentListClickable;
    private int fragmentDownloadType;
    private Realm realm;
    private String TAG = TweetsFragment.class.getName();

    public static TweetsFragment getInstance(Bundle args) {
        TweetsFragment fragment = new TweetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        twitterName = args.getString(Constants.TWITTER_USER_NAME);
        fragmentType = args.getInt(Constants.ADAPTER_TYPE);
        fragmentListClickable = args.getBoolean(Constants.ADAPTER_LIST_CLICKABLE);
        fragmentDownloadType = args.getInt(Constants.ADAPTER_DOWNLOAD_TYPE);

        subscription = new CompositeSubscription();
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
        view.setTag(Math.random());
        String maxId = null;

        RealmResults<TweetRealm> data =
                realm.where(TweetRealm.class)
                        .equalTo("owner", twitterName)
                        .equalTo("type", fragmentType)
                        .findAll();
        Log.e(TAG, "Number of search results: " + data.size());
        if (data.size() > 0) {
            maxId = data.max("id").toString();
            Log.e(TAG, "Number of max id: " + maxId);
        }
        data.sort("id", RealmResults.SORT_ORDER_DESCENDING);
        RealmAdapter adapter = new RealmAdapter(getActivity(), data);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        if (fragmentListClickable) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TweetRealm tr = (TweetRealm) parent.getItemAtPosition(position);
                    String user = tr.getUserName();
                    if (user.equals(tr.getOwner())) {
                        Toast.makeText(getContext(), tr.getUserName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), FollowersActivity.class);
                        intent.putExtra(Constants.TWITTER_USER_NAME, user);
                        startActivity(intent);
                    }

                }
            });
        }

        getData(maxId);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh");
                mSwipeRefreshLayout.setRefreshing(true);
                String id = realm.where(TweetRealm.class)
                        .equalTo("owner", twitterName)
                        .equalTo("type", fragmentType)
                        .findAll()
                        .max("id").toString();
                Log.e(TAG, "last twitter id onRefresh: " + id);
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

        final TwitterService twitterService = retrofit.create(TwitterService.class);

        if (fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_ALL ||
                fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_TIMELINE) {
            Log.e(TAG, "fetching timeline");
            subscription.add(
                    twitterService.getTimeline(twitterName, maxId)
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<TweetJson>>() {
                                           @Override
                                           public void onCompleted() {
                                               Log.e(TAG, "onCompleted");
                                               if (mSwipeRefreshLayout.isRefreshing()) {
                                                   mSwipeRefreshLayout.setRefreshing(false);
                                               }
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               Log.e(TAG, e.getMessage());
                                           }

                                           @Override
                                           public void onNext(List<TweetJson> tweets) {
                                               Log.e(TAG, "onNext");
                                               Log.e(TAG, "Number of new tweets found: " + tweets.size());
                                               saveData(tweets, TweetRealm.TYPE_TIMELINE, null);
                                           }
                                       }
                            )
            );
        }

        if (fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_ALL) {
            Log.e(TAG, "fetching others timeline&favorites");
            subscription.add(
                    twitterService.getTimeline(twitterName, maxId)
                            .flatMap(new Func1<List<TweetJson>, Observable<TweetJson>>() {
                                @Override
                                public Observable<TweetJson> call(List<TweetJson> timelineJsons) {
                                    return Observable.from(timelineJsons);
                                }
                            })
                            .filter(new Func1<TweetJson, Boolean>() {
                                @Override
                                public Boolean call(TweetJson timelineJson) {
                                    return (timelineJson.retweeted_status != null);
                                }
                            })
                            .flatMap(new Func1<TweetJson, Observable<TweetTuple>>() {
                                @Override
                                public Observable<TweetTuple> call(final TweetJson tweetJson) {
                                    final String userName = tweetJson.retweeted_status.user.screen_name;
                                    return Observable.zip(
                                            twitterService.getTimeline(userName, null),
                                            twitterService.getFavorites(userName, null),
                                            new Func2<List<TweetJson>, List<TweetJson>, TweetTuple>() {
                                                @Override
                                                public TweetTuple call(List<TweetJson> timelineJsons, List<TweetJson> favoritesJsons) {
                                                    TweetTuple tweetTuple = new TweetTuple();
                                                    tweetTuple.setTimeline(timelineJsons);
                                                    Map<String, List<TweetJson>> fav = new HashMap<>();
                                                    fav.put(userName, favoritesJsons);
                                                    tweetTuple.setFavorites(fav);
                                                    return tweetTuple;
                                                }
                                            }
                                    );
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TweetTuple>() {
                                           @Override
                                           public void onCompleted() {
                                               Log.e(TAG, "onComplete");
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               Log.e(TAG, "onError " + e.toString());
                                           }

                                           @Override
                                           public void onNext(TweetTuple tweetTuple) {
                                               Log.e(TAG, "onNext");
                                               Log.e(TAG, "Number of new timelines found: " + tweetTuple.getTimeline().size());
                                               saveData(tweetTuple.getTimeline(), TweetRealm.TYPE_TIMELINE, null);
                                               Log.e(TAG, "Number of new favorites found: " + tweetTuple.getFavorites().size());
                                               Map<String, List<TweetJson>> favs = tweetTuple.getFavorites();
                                               for (String key : favs.keySet()) {
                                                   saveData(favs.get(key), TweetRealm.TYPE_FAVORITES, key);
                                               }
                                           }
                                       }
                            )
            );
        }

        if (fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_FAVORITES) {
            Log.e(TAG, "fetching favorites");
            subscription.add(
                    twitterService.getFavorites(twitterName, null)
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<TweetJson>>() {
                                           @Override
                                           public void onCompleted() {
                                               Log.e(TAG, "onCompleted");
                                               if (mSwipeRefreshLayout.isRefreshing()) {
                                                   mSwipeRefreshLayout.setRefreshing(false);
                                               }
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               Log.e(TAG, e.getMessage());
                                               if (mSwipeRefreshLayout.isRefreshing()) {
                                                   mSwipeRefreshLayout.setRefreshing(false);
                                               }
                                           }

                                           @Override
                                           public void onNext(List<TweetJson> tweets) {
                                               Log.e(TAG, "onNext");
                                               Log.e(TAG, "Number of new tweets found: " + tweets.size());
                                               saveData(tweets, TweetRealm.TYPE_FAVORITES, null);
                                           }
                                       }
                            )
            );
        }
    }

    private void saveData(List<TweetJson> data, int type, String favoritesOwner) {
        List<TweetRealm> mData = new ArrayList<>();
        for (TweetJson t : data) {
            TweetRealm tw = new TweetRealm();
            tw.setId(t.id);
            if (type == TweetRealm.TYPE_FAVORITES) {
                tw.setOwner(favoritesOwner);
            } else {
                tw.setOwner(t.user.screen_name);
            }
            tw.setType(type);
            if (t.retweeted_status != null) {
                tw.setUserName(t.retweeted_status.user.screen_name);
                tw.setMessage(t.retweeted_status.text);
                tw.setImageUrl(t.retweeted_status.user.profile_image_url.replace("_normal", ""));
                tw.setIsRetweet(true);
            } else {
                tw.setUserName(t.user.screen_name);
                tw.setMessage(t.text);
                tw.setImageUrl(t.user.profile_image_url.replace("_normal", ""));
                tw.setIsRetweet(false);
            }
            mData.add(tw);
        }

        Log.e(TAG, "Number of new tweets to be added to DB: " + mData.size());
        if (data.size() > 0) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(mData);
            realm.commitTransaction();
        }
    }
}
