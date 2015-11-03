package com.example.jitter;

import com.example.jitter.data.TweetRealm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TweetRealmUnitTest {
    TweetRealm tweetRealm;
    long id = 1234L;
    String owner = "Frank";
    String userName = "Gary";
    String message = "Hi hello and how are you";
    String imageUrl = "http://www.image.com/234.jpg";
    boolean isRetweet = true;
    int type = 0;

    long wrongId = 12345L;
    String wrongOwner = "Frankenstein";
    String wrongUserName = "Gary Potter";
    String wrongMessage = "Hi hello and how are you, how are you";
    String wrongImageUrl = "http://www.image.com/23456.pdf";
    boolean wrongIsRetweet = false;
    int wrongType = 1;

    @Before
    public void initialize() throws Exception {
        tweetRealm = new TweetRealm();
    }
    
    @Test
    public void testId() throws Exception {
        tweetRealm.setId(id);
        assertEquals(tweetRealm.getId(), id);
        assertNotEquals(tweetRealm.getId(), wrongId);
    }

    @Test
    public void testOwner() throws Exception {
        tweetRealm.setOwner(owner);
        assertEquals(tweetRealm.getOwner(), owner);
        assertNotEquals(tweetRealm.getOwner(), wrongOwner);
    }

    @Test
    public void testUserName() throws Exception {
        tweetRealm.setUserName(userName);
        assertEquals(tweetRealm.getUserName(), userName);
        assertNotEquals(tweetRealm.getUserName(), wrongUserName);
    }

    @Test
    public void testMessage() throws Exception {
        tweetRealm.setMessage(message);
        assertEquals(tweetRealm.getMessage(), message);
        assertNotEquals(tweetRealm.getMessage(), wrongMessage);
    }

    @Test
    public void testImageUrl() throws Exception {
        tweetRealm.setImageUrl(imageUrl);
        assertEquals(tweetRealm.getImageUrl(), imageUrl);
        assertNotEquals(tweetRealm.getImageUrl(), wrongImageUrl);
    }

    @Test
    public void testIsRetweet() throws Exception {
        tweetRealm.setIsRetweet(isRetweet);
        assertTrue(tweetRealm.getIsRetweet());
        tweetRealm.setIsRetweet(wrongIsRetweet);
        assertFalse(tweetRealm.getIsRetweet());
    }

    @Test
    public void testType() throws Exception {
        tweetRealm.setType(type);
        assertEquals(tweetRealm.getType(), type);
        assertNotEquals(tweetRealm.getType(), wrongType);
    }
}