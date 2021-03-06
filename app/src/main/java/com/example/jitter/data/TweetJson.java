package com.example.jitter.data;

/**
 * Mapper class for TwitterApi response
 */
public class TweetJson {
    public final long id;
    public final User user;
    public final Retweeted_Status retweeted_status;
    public final String text;

    public TweetJson(long id, String screen_name, String text, String profile_image_url, String retweeter_name, String retweet_text, String retweeter_image_url) {
        this.id = id;
        user = new User(screen_name, profile_image_url);
        retweeted_status = new Retweeted_Status(retweet_text, retweeter_name, retweeter_image_url);
        this.text = text;
    }

    public static class User {
        public final String screen_name;
        public final String profile_image_url;

        public User(String screen_name, String profile_image_url) {
            this.screen_name = screen_name;
            this.profile_image_url = profile_image_url;
        }
    }

    public static class Retweeted_Status {
        public final String text;
        public final User user;

        public Retweeted_Status(String text, String screen_name, String profile_image_url) {
            this.text = text;
            user = new User(screen_name, profile_image_url);
        }
    }
}


