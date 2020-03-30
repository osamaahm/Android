package com.example.osama.tutorialday1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE);
        TextView receivedMsg = (TextView)findViewById(R.id.messageTV);
        receivedMsg.setText(message);
    }
}
