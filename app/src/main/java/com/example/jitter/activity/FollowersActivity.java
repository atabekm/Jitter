package com.example.jitter.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jitter.R;
import com.example.jitter.adapter.PagerAdapter;
import com.example.jitter.util.Constants;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

public class FollowersActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.pager) ViewPager viewPager;

    @BindString(R.string.timeline_favorites_for) String timelineFavoritesFor;
    @BindString(R.string.followers_tab_timeline) String timelineTab;
    @BindString(R.string.followers_tab_favorites) String favoritesTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);

        // get twitter username passed from previous activity via intents
        String twitterName = getIntent().getStringExtra(Constants.TWITTER_USER_NAME);
        // set toolbar and its title
        toolbar.setTitle(String.format("%s @%s", timelineFavoritesFor, twitterName));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // add two tabs to TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(timelineTab));
        tabLayout.addTab(tabLayout.newTab().setText(favoritesTab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // PagerAdapter instance that gives us chance to instantiate Fragments for TabLayout
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), twitterName);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // listener for changing tabs on selection
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // when clicking home button it should work as back button, otherwise previous activity
        // might not be correctly created
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
