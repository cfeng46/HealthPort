package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView signout = findViewById(R.id.signout);
        TextView signoutText = findViewById(R.id.signout_text);
        final Button inbox = (Button) findViewById(R.id.inbox);
        Button docs = (Button) findViewById(R.id.documents);
        Button setting = (Button) findViewById(R.id.settings);
        Button contacts = (Button) findViewById(R.id.contacts);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, settings.class));
            }
        });

        signoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, login.class));
                finish();
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, login.class));
                finish();
            }
        });


        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, documents.class));
                finish();
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, inbox.class));
                finish();
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, contacts_home.class));
            }
        });
    }
}
