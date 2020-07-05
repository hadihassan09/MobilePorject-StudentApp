package com.example.studentsapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView user,password,login,signup;
    Button admin;
    DataManeger M = new DataManeger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mainStudentActivity.S = new Student();
        Firebase.setAndroidContext(this);
        imageView = findViewById(R.id.imageView2);
        user = findViewById(R.id.idText);
        password = findViewById(R.id.passwordText);
        login = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.textView2);


        user.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View v) {
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivityForResult(intent, 100);
            }
        });

        {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tmp1 = user.getText().toString();
                    String tmp2 = password.getText().toString();
                    if (tmp1.equals("") && tmp2.equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter Username and Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (tmp1.equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter Username", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (tmp2.equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final String ID = tmp1;
                    final String Password = tmp2;

                        M.getReferenceStudent().child(ID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean flag;
                                Object PasswordP = dataSnapshot.child("password").getValue();
                                String passwordP = "";
                                String Gender = dataSnapshot.child("gender").getValue(String.class);

                                if (PasswordP != null)
                                    passwordP = PasswordP.toString();

                                if (!passwordP.trim().equals(Password))
                                    flag = false;
                                else
                                    flag = true;

                                if (flag) {
                                    M.GetInfo(ID, getApplicationContext());
                                    Intent intent = new Intent(getApplicationContext(), mainStudentActivity.class);
                                    intent.putExtra("StudentId", ID);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
            });
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
