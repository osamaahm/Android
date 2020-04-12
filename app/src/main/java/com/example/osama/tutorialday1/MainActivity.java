package com.example.osama.tutorialday1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView contactLV;
    Button contactBtn;

    ArrayList<HashMap<String,String>>contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactLV = findViewById(R.id.contactLV);
        contactBtn = findViewById(R.id.getTextBtn);
        contactList = new ArrayList<>();

    }

    public void getContact(View view) {

        new GetContact().execute();

    }
    private class GetContact extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Downloading", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();
            String url = "https://api.androidhive.info/contacts/";
            String jsonStr = httpHandler.makeServiceCall(url);
            System.out.println(jsonStr);
            if(jsonStr!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray =jsonObject.getJSONArray("contacts");

                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject contact = jsonArray.getJSONObject(i);
                        String id = contact.getString("id");
                        String name = contact.getString("name");
                        String email= contact.getString("email");
                        String address= contact.getString("address");
                        String gender= contact.getString("gender");
                        JSONObject phone = contact.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        HashMap<String,String> contacts= new HashMap<>();
                        contacts.put("id", id);
                        contacts.put("name", name);
                        contacts.put("email", email);
                        contacts.put("mobile", mobile);
                        contactList.add(contacts);


                    }

                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "No response from server", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ListAdapter listAdapter = new SimpleAdapter(MainActivity.this,contactList,R.layout.list_item,
                    new String[]{"email", "mobile"}, new int[]{R.id.email,R.id.mobile}  );
            contactLV.setAdapter(listAdapter);
        }
    }
}
