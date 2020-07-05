package com.example.studentsapp;

import android.content.Context;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;


public class DataManeger {
    private String EmailConfirmP;
    private long maxid;
    private DatabaseReference referenceStudent = FirebaseDatabase.getInstance().getReference().child("Student"),
            referenceCourse = FirebaseDatabase.getInstance().getReference().child("Course"),
            referenceTest = FirebaseDatabase.getInstance().getReference();
    private boolean flag;
    private String Coursename;
    private String Cdata;

    public DataManeger() {
        referenceStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxid = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public DatabaseReference getReferenceStudent() {
        return referenceStudent;
    }

    public DatabaseReference getReferenceCourse() {
        return referenceCourse;
    }

    public DatabaseReference getReferenceTest() {
        return referenceTest;
    }

    public String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());


            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    //DONE AND TRUE
   /* public void getAllCoursesNames(final Context x){

        referenceCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> tmp=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    tmp.add(ds.getValue(String.class));
                }
                Intent intent=new Intent(x,viewCoursesPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putStringArrayListExtra("coursesData",tmp);
                x.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    */
   /*public void getAllStudentsNames(final Context x,final String ID){

        referenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> tmp=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    tmp.add(ds.child("fname").getValue(String.class).concat(" ".concat(ds.child("lname").getValue(String.class))));
                }
                Intent intent=new Intent(x,viewStudentsPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putStringArrayListExtra("studentsData",tmp);
                x.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


    public Boolean checkDataAndPassword(Student S, String ConfirmPass, Context v) {
        if (S.getPassword() == null || ConfirmPass == null || S.getEmail() == null || S.getFname() == null
                || S.getGender() == null || S.getLname() == null || S.getPhone() == null) {
            Toast.makeText(v, "Please Complete Filling In The Data!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(S.getEmail()) && !isValidPhonenumber(S.getPhone())) {
            Toast.makeText(v, "Wrong Email and Phonenumber format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPhonenumber(S.getPhone())) {
            Toast.makeText(v, "Wrong Phonenumber Format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmail(S.getEmail())) {
            Toast.makeText(v, "Wrong Email Format.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!S.getPassword().equals(ConfirmPass.trim())) {
            Toast.makeText(v, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public boolean isValidPhonenumber(String phone) {
        String regex = "^(?:\\+961|961)?(1|0?3[0-9]?|[4-6]|70|71|76|78|79|81?|9)\\d{6}$";
        return phone.matches(regex);
    }

    /*public void Login(final String ID, final String Password, final Context V){
        if(ID.charAt(0) == 'C'){
            referenceStudent.child(ID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Object PasswordP = dataSnapshot.child("password").getValue();
                    String passwordP = "";

                    if (PasswordP != null)
                        passwordP = PasswordP.toString();

                    if (!passwordP.trim().equals(Password))
                        flag = false;
                    else
                        flag = true;

                    if (flag) {
                        GetInfo(ID, V);
                        /*Intent intent = new Intent(V, mainStudentActivity.class);
                        intent.putExtra("StudentId", ID);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        V.startActivity(intent);*/
    /*
                        MainActivity.flag=true;
                    }
                    else {
                        MainActivity.flag=false;
                        Toast.makeText(V, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        if(ID.charAt(0) == 'S'){
            FirebaseDatabase.getInstance().getReference().child("CoursesTest").child(ID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Object PasswordP = dataSnapshot.child("password").getValue();
                    String passwordP = "";
                    if (PasswordP != null)
                        passwordP = PasswordP.toString();
                    if (!passwordP.trim().equals(Password))
                        flag = false;
                    else
                        flag = true;
                    if (flag) {
                        GetInfo(ID, V);
                        /*Intent intent = new Intent(V, mainTeacherActivity.class);
                        intent.putExtra("StudentId", ID);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        V.startActivity(intent);*/
    /*
                        MainActivity.flag=true;
                    }

                    else {
                        MainActivity.flag=false;
                        Toast.makeText(V, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    */

    public void GetInfo(final String ID, final Context V) {
        mainStudentActivity.S.setSID(ID);
        referenceStudent.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // boolean LocalFlag = false;
                String name = dataSnapshot.child("fname").getValue(String.class) + " " + dataSnapshot.child("lname").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);
                String password = dataSnapshot.child("password").getValue(String.class);

                ArrayList<String> a = new ArrayList<>();
                a.add(ID);
                a.add(name);
                a.add(email);
                a.add(phone);
                a.add(gender);
                a.add(address);
                a.add(password);
                Gson gson = new Gson();
                String json = gson.toJson(a);

                savedSharedPreferences.setUserName(V, json);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}