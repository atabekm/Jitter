package com.example.jitter;

import com.example.jitter.data.TweetJson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TweetJsonUnitTest {
    TweetJson tweetJson;

    @Before
    public void initialize() throws Exception {
        tweetJson = new TweetJson(232323,
                "jack",
                "This is tweet",
                "http://www.example.com/image.png",
                "sam",
                "This is Sam",
                "http://www.example.com/image2.png");
    }

    @Test
    public void testId() throws Exception {
        assertEquals(tweetJson.id, 232323);
        assertNotEquals(tweetJson.id, 2);
    }

    @Test
    public void testScreenName() throws Exception {
        assertEquals(tweetJson.user.screen_name, "jack");
        assertNotEquals(tweetJson.user.screen_name, "john");
    }

    @Test
    public void testText() throws Exception {
        assertEquals(tweetJson.text, "This is tweet");
        assertNotEquals(tweetJson.text, "This is not tweet");
    }

    @Test
    public void testProfileImageUrl() throws Exception {
        assertEquals(tweetJson.user.profile_image_url, "http://www.example.com/image.png");
        assertNotEquals(tweetJson.user.profile_image_url, "http://www.example.com/document.doc");
    }

    @Test
    public void testRetweeterScreenName() throws Exception {
        assertEquals(tweetJson.retweeted_status.user.screen_name, "sam");
        assertNotEquals(tweetJson.retweeted_status.user.screen_name, "phil");
    }

    @Test
    public void testRetweeterText_isCorrect() throws Exception {
        assertEquals(tweetJson.retweeted_status.text, "This is Sam");
        assertNotEquals(tweetJson.retweeted_status.text, "This is Phil");
    }

    @Test
    public void testRetweeterProfileImageUrl() throws Exception {
        assertEquals(tweetJson.retweeted_status.user.profile_image_url, "http://www.example.com/image2.png");
        assertNotEquals(tweetJson.retweeted_status.user.profile_image_url, "http://www.example.com/presentation.ppt");
    }
}