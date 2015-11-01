package com.example.jitter.data;

import java.util.List;
import java.util.Map;

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
