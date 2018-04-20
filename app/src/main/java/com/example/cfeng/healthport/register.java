package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cfeng.healthport.Model.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class register extends AppCompatActivity {
    private EditText email_address;
    private EditText Pass;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        Button back = (Button) findViewById(R.id.back);
        Button register = (Button) findViewById(R.id.register);

        email_address = (EditText) findViewById(R.id.edit_email);
        Pass = (EditText) findViewById(R.id.edit_pass);
        username = (EditText) findViewById(R.id.edit_name);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this, MainActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_address.getText().toString();
                String pass_word = Pass.getText().toString();
                String name = username.getText().toString();
                try {
                    if (Person.validInput(name, email, pass_word)) {
                        addNewUser(name, email, pass_word);
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addNewUser(final String name, final String email, final String pin) {
        final Person user = new Person(name, pin, email);
        final DatabaseReference healthport = FirebaseDatabase.getInstance().getReference("Users");
        healthport.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUsername()).exists()) {
                    Toast.makeText(register.this, "user already exists!", Toast.LENGTH_LONG).show();
                } else {
                    healthport.child(user.getUsername()).setValue(user);
                    Toast.makeText(register.this, "new user created!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(register.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(register.this, "Database Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
