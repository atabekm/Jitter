package com.example.jitter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jitter.data.TweetRealm;
import com.example.jitter.util.Constants;
import com.example.jitter.R;
import com.example.jitter.fragment.TweetsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UsersActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);

        String twitterName = getIntent().getStringExtra(Constants.TWITTER_USER_NAME);

        toolbar.setTitle("Timeline for @" + twitterName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(Constants.TWITTER_USER_NAME, twitterName);
            arguments.putInt(Constants.ADAPTER_TYPE, TweetRealm.TYPE_TIMELINE);
            arguments.putBoolean(Constants.ADAPTER_LIST_CLICKABLE, true);
            arguments.putInt(Constants.ADAPTER_DOWNLOAD_TYPE, Constants.ADAPTER_DOWNLOAD_TYPE_ALL);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
