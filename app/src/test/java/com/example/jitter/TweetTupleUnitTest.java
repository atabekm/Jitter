package com.example.jitter;

import com.example.jitter.data.TweetJson;
import com.example.jitter.data.TweetTuple;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TweetTupleUnitTest {
    TweetTuple tweetTuple;
    TweetJson tweetJson;
    List<TweetJson> timeline;
    Map<String, List<TweetJson>> favorites;

    @Before
    public void initialize() throws Exception {
        tweetTuple = new TweetTuple();
        tweetJson = new TweetJson(232323,
                "jack",
                "This is tweet",
                "http://www.example.com/image.png",
                "sam",
                "This is Sam",
                "http://www.example.com/image2.png");
        timeline = new ArrayList<>();
        timeline.add(tweetJson);

        favorites = new HashMap<>();
        favorites.put("item", timeline);
    }
    
    @Test
    public void testTimeline() throws Exception {
        assertEquals(timeline.size(), 1);
        assertNotEquals(timeline.size(), 10);
        assertEquals(timeline.get(0), tweetJson);
        assertNotEquals(timeline.get(0), null);
    }

    @Test
    public void testFavorites() throws Exception {
        assertEquals(favorites.size(), 1);
        assertNotEquals(favorites.size(), 10);
        assertEquals(favorites.get("item"), timeline);
        assertNotEquals(favorites.get("item2"), timeline);
    }

}