package com.example.osama.tutorialday1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity {
    String msg ="Android : ";
    public final int REQUESTCODE= 3;
    private Set<BluetoothDevice> pairedDevices;
    BluetoothAdapter BA;
    ListView btlistLV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(msg, "The onCreate()event");

        BA = BluetoothAdapter.getDefaultAdapter();
        btlistLV = findViewById(R.id.devicelistLV);


    }
    public final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = btDevice.getName();
                String hardwareAddress = btDevice.getAddress();
                Toast.makeText(context, "Device: "+ deviceName+" >> "+ hardwareAddress, Toast.LENGTH_SHORT).show();
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

                Toast.makeText(context, "DISCOVERY_STARTED: ", Toast.LENGTH_SHORT).show();
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                Toast.makeText(context, "DISCOVERY_FINISHED: ", Toast.LENGTH_SHORT).show();
            }



        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:01558986259"));
            startActivity(callIntent);

        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{CALL_PHONE},REQUESTCODE );
        }
    }


    public void turnOnBlueTooth(View view) {
        Intent turnon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnon,0);
    }

    public void visibilityChange(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        IntentFilter intentFilter =  new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, intentFilter);
        BA.startDiscovery();


    }

    public void showBTDevies(View view) {

        ArrayList list = new ArrayList();

        pairedDevices = BA.getBondedDevices();
        for (BluetoothDevice btDevice : pairedDevices) {
            list.add(btDevice.getName());
            Toast.makeText(this, btDevice.getName(), Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter adapter = new ArrayAdapter(this,  android.R.layout.simple_expandable_list_item_1, list);
        btlistLV.setAdapter(adapter);

    }

    public void turnOffBT(View view) {
        BA.disable();
    }
}
