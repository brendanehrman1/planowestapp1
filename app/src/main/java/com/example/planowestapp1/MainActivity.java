package com.example.planowestapp1;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class MainActivity extends Activity {
    public static final String PREF_NAME = "sharedPrefs";

    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        loadAccountData();
        try {
            String info = getAccountData(displayName);
            JSONObject json = new JSONObject(info);
            if (!json.getString("status").equals("notExist") && displayName != null)
                startActivity(new Intent(MainActivity.this, AccountActivity.class));
            else
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadAccountData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        displayName = sharedPreferences.getString("NAME", null);
    }

    public void goToAccount(View v) throws IOException {
        startActivity(new Intent(MainActivity.this, AccountActivity.class));
    }

    public void goToTimes(View v) throws IOException {
        //startActivity(new Intent(MainActivity.this, TimeActivity.class));
    }

    public void goToCalendar(View v) throws IOException {
        //startActivity(new Intent(MainActivity.this, CalendarActivity.class));
    }

    public void goToFriends(View v) throws IOException {
        //startActivity(new Intent(MainActivity.this, FriendActivity.class));
    }

    public String getAccountData(String displayName) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL("http://ec2-3-23-128-64.us-east-2.compute.amazonaws.com:8080/login/user?displayName=" + displayName).openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if(responseCode == 200){
            String response = "";
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();

            return response;
        }

        // an error happened
        return null;
    }

}