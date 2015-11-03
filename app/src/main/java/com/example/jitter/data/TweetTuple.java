package com.example.jitter.data;

import java.util.List;
import java.util.Map;

/**
 * A class to hold List and Map structures, used in TweetsFragment to ease argument passing between
 * RxJava observables
 */
public class TweetTuple {
    private List<TweetJson> timeline;
    private Map<String, List<TweetJson>> favorites;

    public List<TweetJson> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TweetJson> timeline) {
        this.timeline = timeline;
    }

    public Map<String, List<TweetJson>> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String, List<TweetJson>> favorites) {
        this.favorites = favorites;
    }
}
