package com.example.jitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextTwitterName = (EditText) findViewById(R.id.edit_text_twitter_id);
        Button buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTwitterName.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                    intent.putExtra(Constants.TWITTER_USER_NAME, editTextTwitterName.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
