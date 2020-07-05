package com.example.studentsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.node.IntNode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class editProfileStudentPage extends AppCompatActivity {
    private TextView fname,lname,email,phone,address,nameTXT,idTXT,Edit,cancel,password,confirmPassword;
    private RadioButton male,female;
    private String data;
    private DataManeger dataManeger = new DataManeger();
    private ArrayList<String> getdata;
    private Intent intent;
    private String Email;
    private ImageView studentImage;
    private Uri filePath;
    private Button upload,choose;
    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile_page);

        nameTXT=findViewById(R.id.nameTXT);
        idTXT=findViewById(R.id.idTXT);
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        Edit=findViewById(R.id.saveBtn);
        cancel=findViewById(R.id.cancelBtn);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirmPassword);
        studentImage=findViewById(R.id.studentPicture);
        upload=findViewById(R.id.uploadBtn);
        choose=findViewById(R.id.chooseBtn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        intent=getIntent();
        data=intent.getStringExtra("data");
        Gson gson=new Gson();
        getdata=gson.fromJson(data, ArrayList.class);
        Log.d("Data", "SharedP DataStart: "+getdata.toString());
        idTXT.setText(getdata.get(0));
        nameTXT.setText(getdata.get(1));
        final String tmp[]=getdata.get(1).split(" ");
        fname.setText(tmp[0]);
        lname.setText(tmp[1]);
        email.setText(getdata.get(2));
        password.setText(getdata.get(6));
        confirmPassword.setText(getdata.get(6));
        phone.setText(getdata.get(3));


        final ImageLoader IMLD = new ImageLoader(getApplicationContext(), studentImage);
            IMLD.SetImage(studentImage,getApplicationContext());

        if(getdata.get(4).equals("Male")) {
            male.setChecked(true);
        }
        else
            female.setChecked(false);
        if(getdata.get(4).equals("Female")) {
            female.setChecked(true);
        }
        else
            female.setChecked(false);
        address.setText(getdata.get(5));
        Email = getdata.get(3);




        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                File file = getApplicationContext().getFileStreamPath("my_image.jpg");
                if (file.exists()) {
                    IMLD.SetImage(studentImage,getApplicationContext());
                }
            }

        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                File file = getApplicationContext().getFileStreamPath("my_image.jpg");
                if (file.exists()) {
                    IMLD.SetImage(studentImage,getApplicationContext());
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),mainStudentActivity.class);
                startActivity(i);
                finish();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Student editS = new Student();
                editS.setSID(idTXT.getText().toString());
                editS.setFname(fname.getText().toString());
                editS.setLname(lname.getText().toString());
                editS.setPhone(phone.getText().toString());
                editS.setAddress(address.getText().toString());
                editS.setPassword(password.getText().toString());

                data=intent.getStringExtra("data");
                Gson gson=new Gson();
                getdata=gson.fromJson(data, ArrayList.class);

                Email = getdata.get(2);


                if(!Email.toLowerCase().trim().equals(email.getText().toString().toLowerCase())) {
                    editS.setEmail(email.getText().toString());
                }
                else
                    editS.setEmail(Email);

                if (male.isChecked())
                    editS.setGender("Male");
                if(female.isChecked())
                    editS.setGender("Female");
                String confirmPass = confirmPassword.getText().toString();
                Log.d("Data", "DataBase Email: "+getdata.get(2).trim());
                Log.d("Data", "Email Var: "+Email);
                Log.d("Data", "SharedP Data: "+getdata.toString());
                Log.d("Data", "Entered Data: "+email.getText().toString().toLowerCase().trim());
                Log.d("Data", "editS Email: "+editS.getEmail());
                if (dataManeger.checkDataAndPassword(editS, confirmPass, getApplicationContext())) {
                    if (!(getdata.get(2).toLowerCase().trim().equals(email.getText().toString().toLowerCase().trim()))) {
                        Log.d("Data","NEW EMAIL ENTERED");
                        dataManeger.getReferenceStudent().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean flagEmail = false;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Log.d("Data",ds.child("email").getValue(String.class));
                                    if (ds.child("email").getValue(String.class).toLowerCase().trim().equals(editS.getEmail().toLowerCase().trim())) {
                                        Log.d("Data","MATCH FOUND( NEW EMAIL ENTERED EXISTS )");
                                        flagEmail = true;
                                        Toast.makeText(getApplicationContext(), "Email Already Exists!", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                if (!flagEmail) {
                                    Email = editS.getEmail();
                                    final HashMap<String, String> M = new HashMap<>();
                                    dataManeger.getReferenceStudent().child(getdata.get(0)).child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                            for(DataSnapshot dc:Snapshot.getChildren()){
                                                M.put(dc.getKey(),dc.getValue(String.class));
                                            }
                                            //if (!editS.getFname().equals(tmp[0]) || !editS.getLname().equals(tmp[1]) || !editS.getPhone().equals(getdata.get(3))
                                              //      || !editS.getGender().equals(getdata.get(4)) || !editS.getAddress().equals(getdata.get(5))) {
                                                Log.d("Data","Data Changed!( NEW MAIL ENTERED DOESNT EXIST )");
                                                dataManeger.getReferenceStudent().child(editS.getSID()).setValue(editS);
                                                Set set = M.entrySet();
                                                Iterator iterator = set.iterator();

                                                while(iterator.hasNext()){
                                                    Map.Entry mp = (Map.Entry)iterator.next();
                                                    dataManeger.getReferenceStudent().child(getdata.get(0)).child("Courses").child(mp.getKey().toString()
                                                            .trim()).setValue(mp.getValue().toString());
                                                }
                                                Intent intent1 = new Intent(getApplicationContext(),mainStudentActivity.class);
                                                ArrayList<String> a = new ArrayList<>();
                                                a.add(editS.getSID());
                                                a.add(editS.getFname() +" "+ editS.getLname());
                                                a.add(editS.getEmail());
                                                a.add(editS.getPhone());
                                                a.add(editS.getGender());
                                                a.add(editS.getAddress());
                                                a.add(editS.getPassword());
                                                Gson gson = new Gson();
                                                String json = gson.toJson(a);
                                                savedSharedPreferences.setUserName(getApplicationContext(), json);
                                                Log.d("Data", "SharedP Data: "+json);
                                                Log.d("Data","DATA CHANGED SUCCESS");
                                                File file            = getApplicationContext().getFileStreamPath("my_image.jpg");
                                                if (file.delete()) Log.d("file", "my_image.jpeg deleted!");
                                                startActivity(intent1);
                                                Toast.makeText(getApplicationContext(), "Data Changed!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            //}
                                            //else
                                            //{
                                                //Toast.makeText(getApplicationContext(), "Please Re-Log IN!", Toast.LENGTH_SHORT).show();
                                            //}

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    else{
                        Log.d("Data", "EMAIL FIELD NOT CHANGED");
                        final HashMap<String, String> M = new HashMap<>();
                        dataManeger.getReferenceStudent().child(getdata.get(0)).child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                for(DataSnapshot dc:Snapshot.getChildren()){
                                    M.put(dc.getKey(),dc.getValue(String.class));
                                }
                                if(!editS.getFname().equals(tmp[0]) || !editS.getLname().equals(tmp[1]) || !editS.getPhone().equals(getdata.get(3))
                                        || !editS.getGender().equals(getdata.get(4)) || !editS.getAddress().equals(getdata.get(5))) {
                                    Log.d("Data","Data Changed ( NO NEW EMAIL ENTERED )");
                                    dataManeger.getReferenceStudent().child(editS.getSID()).setValue(editS);
                                    Set set = M.entrySet();
                                    Iterator iterator = set.iterator();

                                    while (iterator.hasNext()) {
                                        Map.Entry mp = (Map.Entry) iterator.next();
                                        dataManeger.getReferenceStudent().child(getdata.get(0)).child("Courses").child(mp.getKey().toString()
                                                .trim()).setValue(mp.getValue().toString());
                                    }
                                    Intent intent1 = new Intent(getApplicationContext(),mainStudentActivity.class);
                                    ArrayList<String> a = new ArrayList<>();
                                    a.add(editS.getSID());
                                    a.add(editS.getFname() +" "+ editS.getLname());
                                    a.add(editS.getEmail());
                                    a.add(editS.getPhone());
                                    a.add(editS.getGender());
                                    a.add(editS.getAddress());
                                    a.add(editS.getPassword());
                                    Gson gson = new Gson();
                                    String json = gson.toJson(a);
                                    savedSharedPreferences.setUserName(getApplicationContext(), json);
                                    Log.d("Data", "SharedP Data: "+json);
                                        Log.d("Data","DATA CHANGED SUCCESS");
                                    File file            = getApplicationContext().getFileStreamPath("my_image.jpg");
                                    if (file.delete()) Log.d("file", "my_image.jpeg deleted!");
                                        startActivity(intent1);
                                        Toast.makeText(getApplicationContext(), "Data Changed!", Toast.LENGTH_SHORT).show();
                                        finish();
                                }
                                else {
                                    Log.d("Data","Data Not Changed ( NO CHANGE OCCURRED )");
                                    Toast.makeText(getApplicationContext(), "No Data Changed!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
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

    private void chooseImage() {
        Intent tmp = new Intent();
        tmp.setType("image/*");
        tmp.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(tmp, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Toast.makeText(this, filePath.toString(), Toast.LENGTH_SHORT).show();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageLoader IMLD = new ImageLoader(getApplicationContext(),studentImage);
                IMLD.saveImage(getApplicationContext(),bitmap,"my_image.jpg");
                IMLD.SetImage(studentImage,getApplicationContext());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Uri file = filePath;
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("imagesS/".concat(getdata.get(0).trim()));

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),mainStudentActivity.class);
        startActivity(i);
        finish();
    }
}
