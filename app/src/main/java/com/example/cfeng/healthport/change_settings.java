package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class change_settings extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_settings);

        final EditText Old_email = (EditText) findViewById(R.id.old_email);
        final EditText new_email = (EditText) findViewById(R.id.new_email);
        final EditText pass = (EditText) findViewById(R.id.password);
        ImageView good = (ImageView) findViewById(R.id.good_icon);
        TextView goodText = (TextView) findViewById(R.id.good);
        ImageView cancel = (ImageView) findViewById(R.id.cancelX);
        TextView cancelText = (TextView) findViewById(R.id.cancelText);

        mAuth = FirebaseAuth.getInstance();

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                String origin_email = Old_email.getText().toString().trim();
                final String updated_emial = new_email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (!TextUtils.isEmpty(origin_email) && !TextUtils.isEmpty(updated_emial) && !TextUtils.isEmpty(password)) {
                    AuthCredential credential = EmailAuthProvider.getCredential(origin_email, password);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(updated_emial).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(change_settings.this, "email updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(change_settings.this, login.class));
                                        } else {
                                            Toast.makeText(change_settings.this, "failed to update", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(change_settings.this, "failed to authenticate", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(change_settings.this, "fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                String origin_email = Old_email.getText().toString().trim();
                final String updated_emial = new_email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (!TextUtils.isEmpty(origin_email) && !TextUtils.isEmpty(updated_emial) && !TextUtils.isEmpty(password)) {
                    AuthCredential credential = EmailAuthProvider.getCredential(origin_email, password);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(updated_emial).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(change_settings.this, "email updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(change_settings.this, login.class));
                                        } else {
                                            Toast.makeText(change_settings.this, "failed to update", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(change_settings.this, "failed to authenticate", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(change_settings.this, "fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(change_settings.this, settings.class));
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(change_settings.this, settings.class));
            }
        });

    }

}
