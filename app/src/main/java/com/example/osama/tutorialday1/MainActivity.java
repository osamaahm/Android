package com.example.osama.tutorialday1;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    String msg ="Android : ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(msg, "The onCreate()event");
    }

    public void startService(View view){
        startService(new Intent(MainActivity.this,MyService.class));


    }

    public void stopService(View view) {
        stopService(new Intent(MainActivity.this,MyService.class));
    }

}
