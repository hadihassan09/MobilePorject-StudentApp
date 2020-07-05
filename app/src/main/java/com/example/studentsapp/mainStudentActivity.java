package com.example.studentsapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.os.SystemClock.sleep;

public class mainStudentActivity extends AppCompatActivity {
    private static ImageView profile;
    private String studentId;
    private Intent mainIntent,intent2;
    private String data;
    private ArrayList getdata;
    private TextView name,id,phone,address,email,gender,takeAttendence,viewCourses,editProfile;
    private DataManeger dataManeger=new DataManeger();
    static Student S;
    private Button logout,generator;
    private static Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        getSupportActionBar().hide();

        c = getApplicationContext();
        if(savedSharedPreferences.getUserName(this).length() == 0)
        {
           Intent intent=new Intent(getApplicationContext(),choosePage.class);
           startActivity(intent);
           finish();
        }
        profile=findViewById(R.id.studentPicture);
        takeAttendence=findViewById(R.id.takeAttendBtn);
        viewCourses=findViewById(R.id.viewCoursesBtn);
        editProfile=findViewById(R.id.editInfoBtn);
        name=findViewById(R.id.nameTXT);
        id=findViewById(R.id.idTXT);
        phone=findViewById(R.id.phoneTXT);
        address=findViewById(R.id.addressTXT);
        gender=findViewById(R.id.genderTXT);
        email=findViewById(R.id.emailTXT);
        logout=findViewById(R.id.logoutBtn);
        generator=findViewById(R.id.QRCodeBtn);

        data=savedSharedPreferences.getUserName(this);
        Gson gson=new Gson();
        getdata=gson.fromJson(data,ArrayList.class);

        studentId=getdata.get(0).toString();
        id.setText(getdata.get(0).toString());
        name.setText(getdata.get(1).toString());
        email.setText("Email Address:"+getdata.get(2).toString());
        phone.setText("Phone:"+getdata.get(3).toString());
        gender.setText("Gender:"+getdata.get(4).toString());
        address.setText("Address:"+getdata.get(5).toString());

        //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

        //Adding an image
        final ImageLoader IMLD = new ImageLoader(getApplicationContext(), profile);
        File file            = getApplicationContext().getFileStreamPath("my_image.jpg");
        final String url =    "https://firebasestorage.googleapis.com/v0/b/studentuniattendence-84e8f.appspot.com/o/imagesS%2F" + getdata.get(0) + "?alt=media";//Retrieved url as mentioned above
        if (file.exists()) {
            IMLD.SetImage(profile,getApplicationContext());
        }else {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("imagesS/").child(getdata.get(0).toString());
            final long ONE_KELOBYTE = 1024;
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    IMLD.downloadImage.execute(url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (getdata.get(4).equals("Male")) {
                        Toast.makeText(getApplicationContext(), "testM", Toast.LENGTH_SHORT).show();
                        String n = "https://frigainc.com/wp-content/uploads/2019/08/male-pic.jpg";
                        IMLD.downloadImage.execute(n);
                    } else {
                        Toast.makeText(getApplicationContext(), "testF", Toast.LENGTH_SHORT).show();
                        String n = "https://www.doorspec.com/wp-content/uploads/2018/07/female-avatar.jpg";
                        IMLD.downloadImage.execute(n);
                    }
                }
            });
        }
        //View Courses Page
        viewCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),viewCoursesPage.class);
                intent.putExtra("ID",studentId);
                startActivity(intent);
            }
        });

        //Take Attendence Page
        takeAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent2 = new Intent(getApplicationContext(), QRScannerPage.class);
                startActivity(intent2);
            }
        });

        //Edit Profile Page
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),editProfileStudentPage.class);
                intent.putExtra("data",data);
                startActivity(intent);
                finish();
            }
        });

        //logout from Account
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedSharedPreferences.setUserName(getApplicationContext(),"");
                S=null;
                //savedSharedPreferences.clear(getApplicationContext());
                File file            = getApplicationContext().getFileStreamPath("my_image.jpg");
                if (file.delete()) Log.d("file", "my_image.jpeg deleted!");
                Intent intent=new Intent(getApplicationContext(),choosePage.class);
                startActivity(intent);
                finish();
            }
        });

        //Get My Unique QR Code
        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),studentQRCode.class);
                String s=savedSharedPreferences.getUserName(getApplicationContext());
                intent1.putExtra("data",s);
                startActivity(intent1);
            }
        });

    }
    public static ImageView getProfile() {
        return profile;
    }
   /* public static Context getContext(){
        return c;
    }*/
    public static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
    public static boolean exists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con =  (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
