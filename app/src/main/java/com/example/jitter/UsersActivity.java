package com.example.jitter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        String twitterName = getIntent().getStringExtra(Constants.TWITTER_USER_NAME);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(Constants.TWITTER_USER_NAME, twitterName);

            TweetsFragment fragment = new TweetsFragment();
            fragment.setArguments(arguments);

            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.placeholder, fragment);
            // or ft.add(R.id.placeholder, new FooFragment());
            // Complete the changes added above
            ft.commit();
        }
    }
}