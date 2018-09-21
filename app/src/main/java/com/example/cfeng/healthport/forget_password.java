package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forget_password extends AppCompatActivity {
    private EditText inputEmail;
    private Button back, reset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        inputEmail = (EditText) findViewById(R.id.email_address);

        reset = (Button) findViewById(R.id.reset_button);

        back = (Button) findViewById(R.id.back_button);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forget_password.this, login.class));
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_address = inputEmail.getText().toString();
                if (TextUtils.isEmpty(email_address)) {
                    Toast.makeText(forget_password.this, "Enter your registered email", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email_address).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(forget_password.this, "We have sent you instruction to reset your password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(forget_password.this, "fail to send email", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
