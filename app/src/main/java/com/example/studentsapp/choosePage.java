package com.example.studentsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class choosePage extends AppCompatActivity {
    private TextView login,register;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_page);
        getSupportActionBar().hide();
        login=findViewById(R.id.loginBtn);
        //savedSharedPreferences.clear(getApplicationContext());
        register=findViewById(R.id.registerBtn);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 0);
        }
    }

    public void login(View view){
        intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void register(View view){
        intent=new Intent(this,SignUp.class);
        startActivity(intent);
        this.finish();
    }
}

