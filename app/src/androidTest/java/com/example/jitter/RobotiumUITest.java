package com.example.jitter;

import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jitter.activity.FollowersActivity;
import com.example.jitter.activity.MainActivity;
import com.example.jitter.activity.UsersActivity;
import com.robotium.solo.Solo;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class RobotiumUITest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public RobotiumUITest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testMainActivityCorrectUserName() throws Exception {
        // First unlock the screen.
        solo.unlockScreen();
        // Check that we are in the same Activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Get EditView and enter valid user name ("jack")
        EditText editText = (EditText) solo.getView(R.id.edit_text_twitter_id);
        solo.clearEditText(editText);
        solo.enterText(editText, "jack");

        // Click SEARCH button
        solo.clickOnButton("SEARCH");

        // UsersActivity should be opened
        solo.assertCurrentActivity("Wrong activity", UsersActivity.class);
        // wait until ListView is opened in UsersActivity
        solo.waitForFragmentById(R.id.list_view, 1000);

        // Get Toolbar and verify its title corresponds to username
        Toolbar toolbar = (Toolbar) solo.getCurrentActivity().findViewById(R.id.toolbar);
        assertEquals(toolbar.getTitle(), "Timeline for @jack");
    }

    public void testMainActivityWrongUserName() throws Exception {
        // First unlock the screen.
        solo.unlockScreen();
        // Check that we are in the same Activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Get EditView and enter invalid user name ("j@ck#")
        EditText editText = (EditText) solo.getView(R.id.edit_text_twitter_id);
        solo.clearEditText(editText);
        solo.enterText(editText, "j@ck#");

        // Click SEARCH button
        solo.clickOnButton("SEARCH");

        // Verify we are still in MainActivity
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Verify TextView with error message is showed up
        TextView error = (TextView) solo.getView(R.id.text_view_twitter_id_error);
        assertEquals(error.getVisibility(), View.VISIBLE);
    }

    public void testUsersActivityListClick() throws Exception {
        // First unlock the screen.
        solo.unlockScreen();
        // Check that we are in the same Activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Get EditView and enter user name ("jack")
        EditText editText = (EditText) solo.getView(R.id.edit_text_twitter_id);
        solo.clearEditText(editText);
        solo.enterText(editText, "jack");

        // Click SEARCH button
        solo.clickOnButton("SEARCH");

        // UsersActivity should be opened
        solo.assertCurrentActivity("Wrong activity", UsersActivity.class);
        // wait until ListView is opened in UsersActivity
        solo.waitForFragmentById(R.id.list_view, 1000);

        ListView list = (ListView) solo.getView(R.id.list_view);

        // for first items in ListView
        for (int i = 0; i < 4; i++) {
            View v = list.getChildAt(i);

            // get username
            TextView textView = (TextView) v.findViewById(R.id.author);
            String userName = textView.getText().toString();

            // get retweet_image's visibility status
            ImageView iv = (ImageView) v.findViewById(R.id.retweet_image);
            int visibility = iv.getVisibility();

            // if it is retweet message
            if (visibility == View.VISIBLE) {
                // click item
                solo.clickInList(i + 1);
                solo.waitForFragmentById(R.id.list_view, 1000);

                // check we are in FollowersActivity
                solo.assertCurrentActivity("Wrong activity", FollowersActivity.class);

                // Get Toolbar and verify its title corresponds to userName
                Toolbar toolbar = (Toolbar) solo.getCurrentActivity().findViewById(R.id.toolbar);
                assertEquals(toolbar.getTitle(), "Timeline/Favorites for " + userName);
                solo.clickOnActionBarHomeButton();

            } else if (visibility == View.GONE) { // if regular tweet
                // click item, and check if Toast message is shown up
                v.performClick();
                assertTrue(solo.waitForText(userName.replace("@", "")));
            }
        }
    }
}