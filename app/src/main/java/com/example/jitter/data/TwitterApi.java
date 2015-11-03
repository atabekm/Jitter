package com.example.jitter.data;

import com.example.jitter.util.Constants;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

/**
 * Retrofit interface to work with Twitter APIs
 */
public interface TwitterApi {
    @Headers({Constants.TWITTER_API_HEADER})
    @GET("/1.1/statuses/user_timeline.json")
    Observable<List<TweetJson>> getTimeline(
            @Query("screen_name") String screen_name,
            @Query("since_id") String since_id);

    @Headers({Constants.TWITTER_API_HEADER})
    @GET("/1.1/favorites/list.json")
    Observable<List<TweetJson>> getFavorites(
            @Query("screen_name") String screen_name,
            @Query("since_id") String since_id);
}
