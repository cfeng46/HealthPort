package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class verification extends AppCompatActivity {
    private TextView status;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private int attempt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        status = (TextView) findViewById(R.id.status);
        Button verify = (Button) findViewById(R.id.verify);
        Button refresh = (Button) findViewById(R.id.refresh);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        final FirebaseUser user = mAuth.getCurrentUser();
        status.setText("User Email: " + user.getEmail() + "(Verfied: " + user.isEmailVerified() + ")");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    String user_id = user.getUid();
                    mDatabase.child(user_id).removeValue();
                    user.delete();
                    Toast.makeText(getApplicationContext(), "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(verification.this, login.class));
                } else {
                    startActivity(new Intent(verification.this, login.class));
                }
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt ++;
                mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        status.setText("User Email: " + user.getEmail() + "(Verfied: " + user.isEmailVerified() + ")");
                        if (user.isEmailVerified()) {
                            startActivity(new Intent(verification.this, home.class));
                        } else {
                            if (attempt == 3) {
                                user = mAuth.getCurrentUser();
                                String user_id = user.getUid();
                                mDatabase.child(user_id).removeValue();
                                user.delete();
                                Toast.makeText(getApplicationContext(), "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(verification.this, login.class));
                            }
                        }
                    }
                });
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

//    private void updateInfo() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            status.setText("User Email: " + user.getEmail() + "(Verfied: " + user.isEmailVerified() + ")");
//        } else {
//            Toast.makeText(verification.this, "signed out", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(verification.this, login.class));
//        }
//    }
}
