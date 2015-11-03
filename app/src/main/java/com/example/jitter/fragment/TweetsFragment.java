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
import com.example.jitter.data.TwitterApi;
import com.example.jitter.di.MyApp;
import com.example.jitter.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TweetsFragment extends Fragment {
    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.swipe_to_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject TwitterApi twitterApi;

    private CompositeSubscription subscription;
    private String twitterName;
    private int fragmentType;
    private boolean fragmentListClickable;
    private int fragmentDownloadType;
    private Realm realm;
    private String TAG = TweetsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getComponent(getActivity()).inject(this);

        // get arguments from parent Activity
        Bundle args = getArguments();
        // twitter user name to work with
        twitterName = args.getString(Constants.TWITTER_USER_NAME);
        // type of the fragment, it can be either timeline or favorites, it will be used while
        // making Realm queries
        fragmentType = args.getInt(Constants.ADAPTER_TYPE);
        // if the list is clickable, it will be only clickable in UsersActivity
        fragmentListClickable = args.getBoolean(Constants.ADAPTER_LIST_CLICKABLE);
        // download type, it will determine what data it should download
        fragmentDownloadType = args.getInt(Constants.ADAPTER_DOWNLOAD_TYPE);

        subscription = new CompositeSubscription();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onPause() {
        super.onPause();
        // as soon as fragment becomes invisible, unsubscribe from all subscriptions
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // subscribe to subscriptions
        if (subscription == null || subscription.isUnsubscribed()) {
            subscription = new CompositeSubscription();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // close Realm instance after quitting
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);
        ButterKnife.bind(this, view);
        String maxId = null;

        // retrieve Twitter data for specified user
        RealmResults<TweetRealm> data =
            realm.where(TweetRealm.class)
                .equalTo("owner", twitterName)
                .equalTo("type", fragmentType)
                .findAll();

        Log.d(TAG, "onCreateView() | Number of search results for user @" + twitterName + ": " + data.size());
        // if search results are not empty, assign its maximum id value to variable
        if (data.size() > 0) {
            maxId = data.max("id").toString();
            Log.d(TAG, "onCreateView() | Number of max id: " + maxId);
        }
        // sort results by id in descending order
        data.sort("id", RealmResults.SORT_ORDER_DESCENDING);

        // make an adapter with found data, and set it to listview
        RealmAdapter adapter = new RealmAdapter(getActivity(), data);
        listView.setAdapter(adapter);

        // if fragment is clickable, process onclick
        if (fragmentListClickable) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TweetRealm tr = (TweetRealm) parent.getItemAtPosition(position);
                    String user = tr.getUserName();
                    // for own tweets, show toast, for retweets go to Followers activity
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

        // check whether data needs to be updated
        getData(maxId);

        // on swipe refresh, it will check whether new data is available to download, and if it is
        // it will download, store to Realm, and update the listview
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onCreateView() -> onRefresh");
                mSwipeRefreshLayout.setRefreshing(true);
                Number maxId = realm.where(TweetRealm.class)
                    .equalTo("owner", twitterName)
                    .equalTo("type", fragmentType)
                    .findAll()
                    .max("id");

                String id = null;
                if (maxId != null) {
                    id = maxId.toString();
                    Log.d(TAG, "onCreateView() -> onRefresh | Last tweet id: " + id);
                }

                getData(id);
            }
        });

        return view;
    }

    /**
     * A method to make network requests to Twitter APIs
     *
     * @param maxId Id of the latest tweet available in DB
     */
    private void getData(String maxId) {

        // if entering UsersActivity or swipe refreshing at FollowersActivity's Timeline
        // tab, get Timeline data and save
        if (fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_ALL ||
                fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_TIMELINE) {
            Log.d(TAG, "getData() | Fetching timeline for @" + twitterName);
            subscription.add(
                twitterApi.getTimeline(twitterName, maxId)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver(TweetRealm.TYPE_TIMELINE, maxId))
            );
        }

        // if entering UsersActivity, get Timeline and favorites data for other users and save
        if (fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_ALL) {
            Log.d(TAG, "getData() | Fetching others' timeline&favorites");
            subscription.add(
                twitterApi.getTimeline(twitterName, maxId)
                    // Convert Observable<List<TweetJson>> to Observable<TweetJson>
                    .flatMap(new Func1<List<TweetJson>, Observable<TweetJson>>() {
                        @Override
                        public Observable<TweetJson> call(List<TweetJson> timelineJsons) {
                            return Observable.from(timelineJsons);
                        }
                    })
                    // Get read of all TweetJson that are not retweets
                    .filter(new Func1<TweetJson, Boolean>() {
                        @Override
                        public Boolean call(TweetJson timelineJson) {
                            return (timelineJson.retweeted_status != null);
                        }
                    })
                    // Get TweetTuple with all timelines and favorites for TweetJson user
                    .flatMap(new Func1<TweetJson, Observable<TweetTuple>>() {
                        @Override
                        public Observable<TweetTuple> call(final TweetJson tweetJson) {
                            final String userName = tweetJson.retweeted_status.user.screen_name;
                            return Observable.zip(
                                    twitterApi.getTimeline(userName, null),
                                    twitterApi.getFavorites(userName, null),
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
                               Log.d(TAG, "getData() -> onComplete");
                           }

                           @Override
                           public void onError(Throwable e) {
                               Log.e(TAG, "getData() -> onError: " + e.toString());
                           }

                           @Override
                           public void onNext(TweetTuple tweetTuple) {
                               Log.d(TAG, "getData() -> onNext | Number of new timelines found: " + tweetTuple.getTimeline().size());
                               saveData(tweetTuple.getTimeline(), TweetRealm.TYPE_TIMELINE, null);
                               Map<String, List<TweetJson>> favs = tweetTuple.getFavorites();
                               for (String key : favs.keySet()) {
                                   Log.d(TAG, "getData() -> onNext | Number of new favorites found: " + favs.get(key).size());
                                   saveData(favs.get(key), TweetRealm.TYPE_FAVORITES, key);
                               }
                           }
                       }
                    )
            );
        }

        // if entering swipe refreshing at FollowersActivity's Favorites
        // tab, get Favorites data and save
        if (fragmentDownloadType == Constants.ADAPTER_DOWNLOAD_TYPE_FAVORITES) {
            Log.d(TAG, "fetching favorites for @" + twitterName);
            subscription.add(
                twitterApi.getFavorites(twitterName, maxId)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver(TweetRealm.TYPE_FAVORITES, maxId))
            );
        }
    }

    /**
     * A method to process data save to DB
     *
     * @param data Data that needs to be saved to DB
     * @param type Type of the data, it can be either 0 (for timeline) or 1 (for favorites)
     * @param favoritesOwner If saved data is for favorites, then it will supply its the owner
     */
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
            // if he data is retweet, we save its retweet username, text, and profile_image_url,
            // otherwise, save real username, text, and profile_image_url
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

        Log.d(TAG, "saveData() | Number of new tweets to be added to DB: " + mData.size());
        // if we found new data, save it to DB
        if (data.size() > 0) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(mData);
            realm.commitTransaction();
        }
    }

    /**
     * A method to generate an Observer according to fragment type and latest id
     *
     * @param type Type of the data, it can be either 0 (for timeline) or 1 (for favorites)
     * @param maxId If saved data is for favorites, then it will supply its the owner
     */
    private Observer<List<TweetJson>> getObserver(final int type, final String maxId) {
        return new Observer<List<TweetJson>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "getObserver() -> onCompleted");
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "getObserver() -> onError | " + e.getMessage());
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onNext(List<TweetJson> tweets) {
                Log.d(TAG, "getObserver() -> onNext | Number of new tweets found: " + tweets.size());
                saveData(tweets, type, maxId);
            }
        };
    }
}
