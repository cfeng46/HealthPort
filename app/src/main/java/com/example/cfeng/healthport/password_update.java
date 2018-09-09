package com.example.cfeng.healthport;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class password_update extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update);

        final EditText email = (EditText) findViewById(R.id.email_address);
        final EditText old_pass = (EditText) findViewById(R.id.old_pass);
        final EditText new_pass = (EditText) findViewById(R.id.new_pass);
        ImageView good = (ImageView) findViewById(R.id.good_icon);
        ImageView cancel = (ImageView) findViewById(R.id.cancelX);

        mAuth = FirebaseAuth.getInstance();

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                String emailAdress = email.getText().toString().trim();
                String old_password = old_pass.getText().toString().trim();
                final String new_password = new_pass.getText().toString().trim();
                if (!TextUtils.isEmpty(emailAdress) && !TextUtils.isEmpty(old_password) && !TextUtils.isEmpty(new_password)) {
                    AuthCredential credential = EmailAuthProvider.getCredential(emailAdress, old_password);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(password_update.this, "password updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(password_update.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(password_update.this, "failed to update", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(password_update.this, "failed to authenticate", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(password_update.this, "fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(password_update.this, settings.class));
            }
        });
    }
}
