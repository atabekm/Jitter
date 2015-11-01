package com.example.jitter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jitter.data.TweetRealm;
import com.example.jitter.util.Constants;
import com.example.jitter.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText etTwitterName = (EditText) findViewById(R.id.edit_text_twitter_id);
        etTwitterName.setText("jack");
        final TextView tvTwitterNameError = (TextView) findViewById(R.id.text_view_twitter_id_error);
        Button buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitterName = etTwitterName.getText().toString();
                tvTwitterNameError.setVisibility(View.GONE);
                if (twitterName.length() > 1 &&
                        twitterName.length() <= Constants.TWITTER_USERNAME_SIZE &&
                        twitterName.matches(Constants.TWITTER_USERNAME_PATTERN)) {
                    Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                    intent.putExtra(Constants.TWITTER_USER_NAME, twitterName);
                    startActivity(intent);
                } else {
                    etTwitterName.setText("");
                    tvTwitterNameError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}