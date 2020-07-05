package com.example.studentsapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class introPage extends AppCompatActivity {
    String data;
    Intent intent;
    ArrayList getdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);
        getSupportActionBar().hide();


        Handler handler = new Handler();
        if(savedSharedPreferences.getUserName(this).length() == 0)
        {
            //savedSharedPreferences.clear(getApplicationContext());
            handler.postDelayed(new Runnable() {
                public void run() {
                    intent=new Intent(getApplicationContext(),choosePage.class);
                    startActivity(intent);
                    finish();
                }
            }, 1500);

        }
        else {
            data = savedSharedPreferences.getUserName(this);
            Gson gson = new Gson();
            getdata = gson.fromJson(data, ArrayList.class);

            handler.postDelayed(new Runnable() {
                public void run() {
                    intent = new Intent(getApplicationContext(), mainStudentActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1500);
        }
    }
}
