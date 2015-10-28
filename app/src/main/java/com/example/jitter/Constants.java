package com.example.jitter;

public class Constants {
    public static final String TWITTER_USER_NAME = "twitter_user_name";
    public static String TWITTER_USER_TIMELINE_API =
            "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
    public static String TWITTER_FAVORITES_LIST_API =
            "https://api.twitter.com/1.1/favorites/list.json?screen_name=";
    public static String TWITTER_USERNAME_PATTERN = "([A-Za-z]|[0-9]|\\_)+";
    public static int TWITTER_USERNAME_SIZE = 15;
}
