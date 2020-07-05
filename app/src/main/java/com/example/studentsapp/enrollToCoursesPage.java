package com.example.studentsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class enrollToCoursesPage extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    private DataManeger M = new DataManeger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_enroll_to_courses_page);
        final String ID = getIntent().getStringExtra("ID");

        M.getReferenceStudent().child(ID).child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> ArrayList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ArrayList.add(ds.getKey());
                }
                M.getReferenceTest().child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot Snapshot) {
                        HashMap<String, Integer> hashMap = new HashMap<>();
                        for (DataSnapshot dc : Snapshot.getChildren()) {
                            hashMap.put(dc.getKey(), 0);
                            for (String R : ArrayList) {
                                if (R.equals(dc.getKey()))
                                    hashMap.put(dc.getKey(), 1);

                            }
                        }

                        Set set = hashMap.entrySet();
                        Iterator iterator = set.iterator();
                        ArrayList.clear();
                        while (iterator.hasNext()) {
                            Map.Entry mp = (Map.Entry) iterator.next();
                            if (Integer.parseInt(mp.getValue().toString()) == 1) {
                                ArrayList.add("Course ID: ".concat(mp.getKey().toString().trim().concat("\nRegistered Status: True.")));
                            } else {
                                ArrayList.add("Course ID: ".concat(mp.getKey().toString().trim().concat("\nRegistered Status: False.")));
                            }
                        }
                        listView = findViewById(R.id.coursesList);
                        Collections.sort(ArrayList);
                        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ArrayList);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (ArrayList.get(i).split("\n")[1].substring(19).equals("True.")) {
                                    Toast.makeText(getApplicationContext(), "Already Registered to Course!", Toast.LENGTH_SHORT).show();
                                } else {
                                    M.getReferenceStudent().child(ID).child("Courses").child(ArrayList.get(i).split("\n")[0].substring(11)).setValue("00 ".concat
                                            (M.getAlphaNumericString(32).concat(" ".concat(new Date().toString()))));
                                    ArrayList.add(ArrayList.get(i).split("\n")[0].concat("\n".concat(ArrayList.get(i)
                                            .split("\n")[1].substring(0, 19).concat("True."))));
                                    ArrayList.remove(i);
                                    Collections.sort(ArrayList);
                                    arrayAdapter.notifyDataSetChanged();
                                    listView.setAdapter(arrayAdapter);
                                }
                                return false;
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });
*/
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(getApplicationContext(),viewCoursesPage.class);
        setIntent.putExtra("ID", getIntent().getStringExtra("ID"));
        startActivity(setIntent);
        finish();
    }
}
