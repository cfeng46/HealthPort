package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settings extends AppCompatActivity{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView emailText = findViewById(R.id.email_value);
        TextView passText = findViewById(R.id.password_value);
        ImageView update_email = (ImageView) findViewById(R.id.change_email_icon);
        TextView update_email_text = (TextView) findViewById(R.id.change_email);
        ImageView update_pass = (ImageView) findViewById(R.id.change_password_icon);
        TextView update_pass_text = (TextView) findViewById(R.id.change_password);
        ImageView back = (ImageView) findViewById(R.id.backArrow);

        emailText.setText(user.getEmail());
        passText.setText("********");

        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, change_settings.class));
            }
        });

        update_email_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, change_settings.class));
            }
        });

        update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, password_update.class));
            }
        });

        update_pass_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, password_update.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, home.class));
            }
        });
    }
}