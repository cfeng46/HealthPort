package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class register extends AppCompatActivity {
    private EditText email_address;
    private EditText Pass;
    private EditText UserName;
    private FirebaseAuth mAuth;
    private  DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ImageView back = (ImageView) findViewById(R.id.backArrow);
        TextView backText = (TextView) findViewById(R.id.backText);
        final Button register = (Button) findViewById(R.id.register);

        email_address = (EditText) findViewById(R.id.edit_email);
        Pass = (EditText) findViewById(R.id.edit_pass);
        UserName = (EditText) findViewById(R.id.edit_name);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this, login.class));
                finish();
            }
        });

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this, login.class));
                finish();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = UserName.getText().toString().trim();
                final String email = email_address.getText().toString().trim();
                final String pass_word = Pass.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(email, pass_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            current_user_db.child("UserName").setValue(user_name);
                            Toast.makeText(register.this, "User account is created", Toast.LENGTH_SHORT).show();
                            Intent regIntent = new Intent(register.this, login.class);
                            startActivity(regIntent);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(register.this, e.getReason(), Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }





    /*private void addNewUser(final String email, final String pin) {
        final Person user = new Person(pin, email);
        final DatabaseReference healthport = FirebaseDatabase.getInstance().getReference("Users");
        healthport.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUsername()).exists()) {
                    Toast.makeText(register.this, "user already exists!", Toast.LENGTH_LONG).show();
                } else {
                    healthport.child(user.getUsername()).setValue(user);
                    Toast.makeText(register.this, "new user created!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(register.this, login.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(register.this, "Database Error", Toast.LENGTH_LONG).show();
            }
        });
    }*/
}
