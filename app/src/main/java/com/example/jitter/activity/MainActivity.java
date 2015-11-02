package com.example.jitter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jitter.util.Constants;
import com.example.jitter.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.edit_text_twitter_id) EditText etTwitterName;
    @Bind(R.id.text_view_twitter_id_error) TextView tvTwitterNameError;
    @Bind(R.id.button_search) Button btnSearch;

    @OnClick(R.id.button_search)
    public void search() {
        String twitterName = etTwitterName.getText().toString();
        tvTwitterNameError.setVisibility(View.GONE);
        if (twitterName.length() >= Constants.TWITTER_USERNAME_MIN_LENGTH &&
                twitterName.length() <= Constants.TWITTER_USERNAME_MAX_LENGTH &&
                twitterName.matches(Constants.TWITTER_USERNAME_PATTERN)) {
            Intent intent = new Intent(MainActivity.this, UsersActivity.class);
            intent.putExtra(Constants.TWITTER_USER_NAME, twitterName);
            startActivity(intent);
        } else {
            etTwitterName.setText("");
            tvTwitterNameError.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }
}