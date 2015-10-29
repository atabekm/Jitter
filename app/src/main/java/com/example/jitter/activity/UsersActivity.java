package com.example.jitter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.jitter.util.Constants;
import com.example.jitter.R;
import com.example.jitter.fragment.TweetsFragment;

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
            // Complete the changes added above
            ft.commit();
        }
    }
}
