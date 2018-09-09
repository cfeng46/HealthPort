package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);


        email = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.PassWord);
        TextView forget= (TextView) findViewById(R.id.forget_pass);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        /**
         * Button handler for the login button
         */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (!TextUtils.isEmpty(login_email) && !TextUtils.isEmpty(pass)) {
                    mAuth.signInWithEmailAndPassword(login_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkUserExistence();
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
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
    public void checkUserExistence() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, main_page.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, verification.class));
                    }
                } else {
                    Toast.makeText(MainActivity.this, "User is not registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database Error", Toast.LENGTH_LONG).show();
            }
        });
    }
//    private void signin(final String name, final String pass) {
//        DatabaseReference healthport = FirebaseDatabase.getInstance().getReference("Users");
//        healthport.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(name).exists()) {
//                    if (!name.isEmpty()) {
//                        Person current = dataSnapshot.child(name).getValue(Person.class);
//                        if (current.getPassword().equals(pass)) {
//                            Toast.makeText(MainActivity.this, "login success", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(MainActivity.this, main_page.class));
//                            finish();
//                        } else {
//                            Toast.makeText(MainActivity.this, "password is incorrect", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(MainActivity.this, "username is not registered", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "Database Error", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}

