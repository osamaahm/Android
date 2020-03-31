package com.example.osama.tutorialday1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SecondaryActivity extends AppCompatActivity {
    Button startRecording;
    Button stopRecording;
    Button startPlayRecording;
    Button stopPlayingLastRecording;

    public static final int requestPermissionCode = 1;
    String savedMediaPath="";

    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE);
        TextView receivedMsg = (TextView)findViewById(R.id.messageTV);
        receivedMsg.setText(message);

        startRecording = (Button) findViewById(R.id.startBtn);
        stopRecording = (Button) findViewById(R.id.stopBtn);
        startPlayRecording = (Button) findViewById(R.id.playLastRecordBtn);
        stopPlayingLastRecording = (Button) findViewById(R.id.stopRecPlayBtn);

        stopRecording.setEnabled(false);
        startPlayRecording.setEnabled(false);
        stopPlayingLastRecording.setEnabled(false);

    }

    /*************************************media record start ***********************************/
    //request and check permission for using storage and using microphone//

    public boolean checkPermission(){
        int p1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int p2 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        boolean x= p1==PackageManager.PERMISSION_GRANTED && p2 == PackageManager.PERMISSION_GRANTED;
        return x;
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(SecondaryActivity.this,
                new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, requestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case requestPermissionCode:
                if(grantResults.length > 0)
                {
                    boolean p1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean p2= grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(p1 && p2)
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //End permission//

    public String getFileName(){
        Random random = new Random();
        String str = Environment.getExternalStorageDirectory()+"/" + "Recording"+
                Integer.toString(random.nextInt(5))+".3gp";

        return str;

    }
    public void prepareMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(savedMediaPath);
    }


    public void startRecording(View view) {
        if(checkPermission()){
            savedMediaPath = getFileName();
            prepareMediaRecorder();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            }catch (IllegalStateException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
            startRecording.setEnabled(false);
            stopRecording.setEnabled(true);
        }else
            requestPermission();
    }

    public void stopRecording(View view) {

        mediaRecorder.stop();
        stopRecording.setEnabled(false);
        startRecording.setEnabled(true);
        startPlayRecording.setEnabled(true);
        Toast.makeText(this, "Recording Complete", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "File Saved in "+ savedMediaPath, Toast.LENGTH_LONG).show();
    }

    public void playLastRecord(View view) throws IOException {
        startRecording.setEnabled(false);
        stopRecording.setEnabled(false);
        startPlayRecording.setEnabled(false);
        stopPlayingLastRecording.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(savedMediaPath);
        mediaPlayer.prepare();
        mediaPlayer.start();

        if(!mediaPlayer.isPlaying()){
            startRecording.setEnabled(true);
            stopRecording.setEnabled(false);
            startPlayRecording.setEnabled(true);
            stopPlayingLastRecording.setEnabled(false);
        }


        Toast.makeText(this, "Playing Records", Toast.LENGTH_SHORT).show();
    }

    public void stopPlayingLastRecord(View view) {
        startRecording.setEnabled(true);
        startPlayRecording.setEnabled(true);
        stopPlayingLastRecording.setEnabled(false);
        mediaPlayer.stop();
        mediaPlayer.release();

    }

}
