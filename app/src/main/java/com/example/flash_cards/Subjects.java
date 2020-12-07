package com.example.flash_cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Subjects extends AppCompatActivity {

    Button definition_btn;
    Button add_btn;
    EditText subjects_txt;
    ListView subject_list;

    FirebaseAuth firebaseAuth;
    DatabaseReference root_database;
    ArrayList<String> subjectArrayList;
    FirebaseUser user;
    SubjectsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definitions);
        setTitle("Definitions");
        add_btn = (Button) findViewById(R.id.subjects_btn);
        definition_btn = findViewById(R.id.Definitions2);

        subjects_txt = (EditText) findViewById(R.id.subjects);
        subject_list = (ListView) findViewById(R.id.subject_list);

        firebaseAuth = FirebaseAuth.getInstance();
        subjectArrayList = new ArrayList<String>();
        root_database = FirebaseDatabase.getInstance().getReference().child("users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root_database.child(user.getEmail().substring(0, user.getEmail().indexOf("@"))).child("subjects").child("math").child(subjects_txt.getText().toString());
                Toast.makeText(Subjects.this, "added data", Toast.LENGTH_SHORT).show();
                subjectArrayList.add(subjects_txt.getText().toString());
                adapter.notifyDataSetChanged();

            }
        });
        root_database.child(user.getEmail().substring(0, user.getEmail().indexOf("@"))).child("subjects").child("math").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            subjectArrayList.add(dsp.getKey());
                        }
                        subject_list.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        adapter = new SubjectsAdapter(this, subjectArrayList);



        definition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Subjects.this,Definitions.class);
                startActivity(i);
            }
        });
    }
}