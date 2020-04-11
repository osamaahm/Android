package com.example.osama.tutorialday1;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity {
    String msg ="Android : ";
    public final int REQUESTCODE= 3;
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

    public void makeCall(View view) {
        if(ActivityCompat.checkSelfPermission(MainActivity.this, CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED){
            try {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:01558986259"));
                Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show();
                startActivity(callIntent);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else{
            Toast.makeText(this, "Request", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{CALL_PHONE},REQUESTCODE );
        }
    }
}
