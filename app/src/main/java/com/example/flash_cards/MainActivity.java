package com.example.flash_cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText full_name, email, password;
    Button register;
    TextView login;
    FirebaseAuth fbauth;
    ProgressBar progressBar;
    DatabaseReference root_database;
    Spinner spinner;
    String teacher_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Flash Cards");
        spinner = findViewById(R.id.teacher_student);
        full_name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_user);
        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_button);
        fbauth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        root_database = FirebaseDatabase.getInstance().getReference().child("users");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.teacher_student_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String full_name_string = full_name.getText().toString();
                String email_string = email.getText().toString();
                String password_string = password.getText().toString();

                if(TextUtils.isEmpty(full_name_string)){
                    full_name.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(email_string)){
                    email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password_string)){
                    password.setError("Password is Required");
                    return;
                }
                if(password_string.length() < 6){
                    password.setError("Password must be atleast 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fbauth.createUserWithEmailAndPassword(email_string, password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            root_database.child(email_string.substring(0 ,email_string.indexOf("@"))).setValue("stam");
                            if (teacher_student.equals("Teacher")) {
                                root_database.child(email_string.substring(0 ,email_string.indexOf("@"))).child("role").setValue(("teacher"));
                            } else {
                                root_database.child(email_string.substring(0 ,email_string.indexOf("@"))).child("role").setValue(("student"));
                            }
                            Toast.makeText(MainActivity.this, "Created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        teacher_student = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}