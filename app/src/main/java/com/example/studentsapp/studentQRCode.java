package com.example.studentsapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class studentQRCode extends AppCompatActivity {
    ImageView imageView;
    TextView studentName,studentId;
    ArrayList<String> dataGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_qrcode);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Gson gson=new Gson();
        dataGet=gson.fromJson(data,ArrayList.class);



        Toast.makeText(this, dataGet.toString(), Toast.LENGTH_LONG).show();
        studentId=findViewById(R.id.studentId);
        studentName=findViewById(R.id.studentName);
        studentName.setText("Student Name: "+ dataGet.get(1));
        studentId.setText("Student ID: ".concat(dataGet.get(0)));

        imageView=findViewById(R.id.qrCode);


        QRGenerator qrGenerator=new QRGenerator(dataGet.get(0),imageView);
    }
}
