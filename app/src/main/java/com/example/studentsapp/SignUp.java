package com.example.studentsapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Message.RecipientType;


public class SignUp extends AppCompatActivity {
    private Student S;
    TextView fname,lname,email,password,confirmpass,phonenumber,login,register;
    RadioButton male,female;
    DataManeger M = new DataManeger();
    private AlertDialog D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        register=findViewById(R.id.registerBtn);
        login=findViewById(R.id.loginBtn);
        fname = findViewById(R.id.firstName);
        lname = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.Pass);
        confirmpass = findViewById(R.id.confirmPass);
        phonenumber = findViewById(R.id.phoneNumber);
        male = findViewById(R.id.maleRadio);
        female = findViewById(R.id.femaleRadio);
        S = new Student();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.setEmail(email.getText().toString().trim());
                S.setFname(fname.getText().toString().trim());
                S.setLname(lname.getText().toString().trim());
                S.setPassword(password.getText().toString().trim());
                S.setPhone(phonenumber.getText().toString().trim());
                if(male.isChecked()){
                    S.setGender("Male");
                }
                else if(female.isChecked()){
                    S.setGender("Female");
                }
                S.setAddress("");
                if (M.checkDataAndPassword(S, confirmpass.getText().toString().trim(), getApplicationContext())) {
                    M.getReferenceStudent().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean flagEmail = false;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.child("email").getValue(String.class).toLowerCase().trim().equals(email.getText().toString().toLowerCase().trim())) {
                                    flagEmail = true;
                                    Toast.makeText(getApplicationContext(), "Email Already Exists!", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            if (!flagEmail) {
                                M.getReferenceStudent().child(String.valueOf(dataSnapshot.getChildrenCount() + 1)).setValue(S);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this)
                                        .setTitle("Login ID")
                                        .setMessage("Your LoginID is: ".concat(String.valueOf(dataSnapshot.getChildrenCount() + 1)))
                                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                D = alertDialogBuilder.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }
    public boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public boolean isValidPhonenumber(String phone){
        String regex = "^(?:\\+961|961)?(1|0?3[0-9]?|[4-6]|70|71|76|78|79|7|81?|9)\\d{6}$";
        return phone.matches(regex);
    }
}
