package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cfeng.healthport.Model.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);


        username = (EditText) findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.PassWord);
        TextView forget= (TextView) findViewById(R.id.forget_pass);
        /**
         * Button handler for the login button
         */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin(username.getText().toString(), password.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, register.class));
                finish();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, forget_password.class));
            }
        });
    };


    /**
     * takes the values from the textboxes and checks to see if a user
     * exists with the given username and password
     */
    private void signin(final String name, final String pass) {
        DatabaseReference healthport = FirebaseDatabase.getInstance().getReference("Users");
        healthport.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(name).exists()) {
                    if (!name.isEmpty()) {
                        Person current = dataSnapshot.child(name).getValue(Person.class);
                        if (current.getPassword().equals(pass)) {
                            Toast.makeText(MainActivity.this, "login success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, main_page.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "password is incorrect", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "username is not registered", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}

