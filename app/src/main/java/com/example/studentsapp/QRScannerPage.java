package com.example.studentsapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class QRScannerPage extends AppCompatActivity {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView,name,id;
    ImageView profile;
    BarcodeDetector barcodeDetector;
    private ArrayList getdata;
    private String data;
    private boolean flag = true;
    Intent intent;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner_page);
        getSupportActionBar().hide();
        intent=getIntent();

        data=savedSharedPreferences.getUserName(this);
        Gson gson=new Gson();
        getdata=gson.fromJson(data, ArrayList.class);


        surfaceView=findViewById(R.id.cameraPreview);
        textView=findViewById(R.id.lowerColor);
        profile=findViewById(R.id.profilepicture);
        name=findViewById(R.id.name);
        id=findViewById(R.id.id);

        id.setText(getdata.get(0).toString());
        name.setText(getdata.get(1).toString());
         ImageLoader IMLD = new ImageLoader(getApplicationContext(), profile);
        IMLD.SetImage(profile,getApplicationContext());





        barcodeDetector=new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource=new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(640,480).build();



        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(QRScannerPage.this, new String[]{android.Manifest.permission.CAMERA}, 0);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                cameraSource.start(holder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 1500);
            }


            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);
                                flag = false;
                            }
                            final String[] A = qrCodes.valueAt(0).displayValue.split(" ");
                            final DataManeger M = new DataManeger();

                            M.getReferenceTest().child("Courses").child(A[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot Snapshot) {
                                    try {
                                        if (A[1].equals(Snapshot.child("key").getValue(String.class))) {  //Checking if read QR Code is same as New Generated One
                                            M.getReferenceTest().child("Student").child(getdata.get(0).toString())
                                                    .child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    boolean flag1 = false;
                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                        if (ds.getKey().equals(A[0])) { //Checking if He is Registed to that Course
                                                            flag1 = true;
                                                            String[] B = ds.getValue(String.class).split(" ");
                                                            //A IS THE BR CODE [CID + KEY]
                                                            //B IS STUDENT COURSE_INFO [COUNT + KEY + DATE]
                                                            //C IS COURSE INFO [COUNT + KEY + DATE]
                                                            //D IS DATE OFF COURSE [HOUR + MINUTES + SECONDS]
                                                            //E IS CURRENT_DATE [DAY + MONTH + DAY_NUM + TIME +   ZONE +    YEAR]
                                                            //                  Sat   Jan     25       16:26:38 GMT+02:00 2020
                                                            //                  0     1       2        3        4         5
                                                            //F IS CURRENT_TIME [HOUR + MINUTES + SECONDS]
                                                            //X IS TIME IN SECONDS OF COURSE_TIME
                                                            //Y IS TIME IN SECONDS OF STUDENTS_CODE_READ_TIME
                                                            //Z IS TIME IN SECONDS OF PARAMETER TO REGISTER
                                                            //"15 Jv1HPIpFFRiy4DizAV3WZ1NFqmU64G60 Sat Jan 25 16:26:38 GMT+02:00 2020"
                                                            // 0   1                               2   3   4   5       6           7
                                                            if (!(Integer.parseInt(B[0]) >= Integer.parseInt(Snapshot.child("attendanceCount").getValue(String.class)))) { //Checking if attendance Count is bigger than max Attendance
                                                                String[] E = new Date().toString().split(" ");
                                                                if (!B[1].equals(A[1])) { //Checking if Student Has Already Taken an Attendance
                                                                    String[] C = Snapshot.child("fullInfo").getValue(String.class).split(" ");
                                                                    boolean flag2 = false;
                                                                    if (C[2].equals(E[0]) && C[3].equals(E[1]) && C[4].equals(E[2]) && C[7].equals(E[5])) {
                                                                        textView.setText("100000");
                                                                        String D[] = C[5].split(":");
                                                                        String[] F = E[3].split(":");
                                                                        int X = (Integer.parseInt(D[0]) * 3600) + (Integer.parseInt(D[1]) * 60) + (Integer.parseInt(D[2]));
                                                                        int Y = (Integer.parseInt(F[0]) * 3600) + (Integer.parseInt(F[1]) * 60) + (Integer.parseInt(F[2]));
                                                                        int Z = 60 * Integer.parseInt(Snapshot.child("CourseTimeOut").getValue(String.class));
                                                                        int Count = Integer.parseInt(B[0]);
                                                                        if ((Y - X) <= Z) {
                                                                            flag2 = true;
                                                                            Count = Count + 1;
                                                                            M.getReferenceTest().child("Student").child(getdata.get(0).toString())
                                                                                    .child("Courses").child(A[0]).setValue(String.valueOf(Count).concat(" ".concat(A[1]
                                                                                    .concat(" ".concat(new Date().toString())))));
                                                                        }
                                                                        textView.setText(String.valueOf(Count));
                                                                    }
                                                                    if (!flag2) {
                                                                        Toast.makeText(getApplicationContext(), "Too Late, Code Expired!", Toast.LENGTH_SHORT).show();
                                                                        //Toast.makeText(getApplicationContext(), B[0], Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), "Attendance Takes Successfully!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else
                                                                    Toast.makeText(getApplicationContext(), "Already Taken Attendance!", Toast.LENGTH_SHORT).show();
                                                            }else{
                                                                Toast.makeText(getApplicationContext(), "Already Reached Max Attendance!", Toast.LENGTH_SHORT).show();
                                                            }
                                                            break;
                                                        }
                                                    }
                                                    if(!flag1)
                                                        Toast.makeText(getApplicationContext(), "You Are Not Registered To This Course!", Toast.LENGTH_SHORT).show();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                        else
                                            Toast.makeText(getApplicationContext(), "QR CODE INVALID!!", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "QR CODE INVALID!!", Toast.LENGTH_SHORT).show();
                                        flag = true;
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
        });
    }
}
