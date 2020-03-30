package com.example.osama.tutorialday1;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE= "com.example.osama";
    String msg = "Android : ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(msg, "The onCreate()event");

    }

    public void startService(View view) {
        startService(new Intent(MainActivity.this, MyService.class));


    }

    public void stopService(View view) {
        stopService(new Intent(MainActivity.this, MyService.class));
    }

    public void broadcastIntent(View view) {
        Intent intent = new Intent();
        intent.setAction("com.osama.CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    public void onClickAddName(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure to Add Data");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Confirmed", Toast.LENGTH_SHORT).show();

                ContentValues values = new ContentValues();
                values.put(StudentsProvider.NAME,((EditText)findViewById(R.id.editTxt1)).getText().toString());

                values.put(StudentsProvider.GRADE, ((EditText)findViewById(R.id.editTxt2)).getText().toString());

                Uri uri = getContentResolver().insert(StudentsProvider.CONTENT_URI, values);

                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Pls check yes to insert data", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();

    }
    public void onClickRetrive(View view){
        String URL = StudentsProvider.URL;
        Uri students = Uri.parse(URL);

        Cursor c = getContentResolver().query(students, null, null, null, "name");

        if(c.moveToFirst()){
            do{
                Toast.makeText(this, c.getString(c.getColumnIndex(StudentsProvider._ID) )
                        + " , " + c.getString(c.getColumnIndex(StudentsProvider.NAME)) + " , " +
                        c.getString(c.getColumnIndex(StudentsProvider.GRADE)) ,
                        Toast.LENGTH_SHORT).show();
            }while (c.moveToNext());
        }
    }
    // Method to send data to next page
    public void goToSecondPage(View view){
        Intent intent = new Intent(this, SecondaryActivity.class);
        EditText sendET = (EditText) findViewById(R.id.editTxt3);
        String message = sendET.getText().toString();
        intent.putExtra( MESSAGE, "ANDROID "+ message );
        startActivity(intent);
    }
    //end send data

}
