package com.example.jitter.data;

import com.example.jitter.util.Constants;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

public interface TwitterService {
    @Headers({Constants.TWITTER_API_HEADER})
    @GET("/1.1/statuses/user_timeline.json")
    Observable<List<TweetJson>> getTimeline(@Query("screen_name") String screen_name);

    @Headers({Constants.TWITTER_API_HEADER})
    @GET("/1.1/favorites/list.json")
    Observable<List<TweetJson>> getFavourites(@Query("screen_name") String screen_name);
}
