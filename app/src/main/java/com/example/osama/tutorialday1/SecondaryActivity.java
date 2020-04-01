package com.example.osama.tutorialday1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
    AudioManager audioManager;
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

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

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
    //*************************** End of Media capture   ********************///

   //**************************** Start of Audio Manager ********************///
    public void vibrateMode(View view){
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        Toast.makeText(this, "Changed to vibrate mode", Toast.LENGTH_SHORT).show();
    }

    public void silentMode(View view){

        //**********this part does not work due to some platform related issue*******************//

        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivity(intent);

        try {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Toast.makeText(this, "Changed to silent mode", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
    }

    public void modeCheck(View view) {
        int mode = audioManager.getRingerMode();

        if(mode == AudioManager.RINGER_MODE_NORMAL)
            Toast.makeText(this, "Current mode is "+"NORMAL" , Toast.LENGTH_LONG).show();

        else if(mode == AudioManager.RINGER_MODE_SILENT)
            Toast.makeText(this, "Current mode is "+"SILENT" , Toast.LENGTH_LONG).show();

        else if(mode == AudioManager.RINGER_MODE_VIBRATE)
            Toast.makeText(this, "Current mode is "+"VIBRATE" , Toast.LENGTH_LONG).show();

    }

    public void changeToNormalMode(View view) {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Toast.makeText(this, "Changed to Normal mode", Toast.LENGTH_SHORT).show();
    }

    //**************************** End of Audio Manager ********************///

    /* *************************** Notification tutorial *******************/
    public void showNotification(View view) {
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        notiBuilder.setSmallIcon(R.drawable.imagebutton);
        notiBuilder.setContentTitle("Notification Example");
        notiBuilder.setContentText("This is a test Notification");

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
       // inboxStyle.setBigContentTitle("Big tile detail");

        inboxStyle.addLine("OSAMA");
        inboxStyle.addLine("JUTHY");
        inboxStyle.addLine("ALI");
        inboxStyle.addLine("JUBUDU");

        notiBuilder.setStyle(inboxStyle);

        Intent intent = new Intent(this, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);
        stackBuilder.addNextIntent(intent);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT) ;
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        notiBuilder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notiBuilder.build());


    }
    /* *************************** END Notification tutorial *******************/
    
}
