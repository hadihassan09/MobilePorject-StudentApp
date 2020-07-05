package com.example.studentsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class viewCoursesPage extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    Button enrollCourses;
    Intent intent;
    DataManeger M = new DataManeger();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses_page);
        getSupportActionBar().hide();
        enrollCourses = findViewById(R.id.enrollCourses);

        intent=getIntent();
        final String ID =intent.getStringExtra("ID");

        M.getReferenceStudent().child(ID).child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> ArrayList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ArrayList.add("Course ID: ".concat((ds.getKey()).concat("\nAttendance Count: "
                            .concat(ds.getValue(String.class).split(" ")[0]))));
                }

                listView = findViewById(R.id.coursesList);
                Collections.sort(ArrayList);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ArrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        enrollCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), enrollToCoursesPage.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
                finish();
            }
        });


    }
}
