package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class main_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button signout = (Button) findViewById(R.id.signout);
        final Button profile = (Button) findViewById(R.id.profile);
        Button docs = (Button) findViewById(R.id.documents);
        Button setting = (Button) findViewById(R.id.settings);
        Button contacts = (Button) findViewById(R.id.contacts);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page.this, settings.class));
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(main_page.this, MainActivity.class));
                finish();
            }
        });


        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(main_page.this, documents.class));
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page.this, profile.class));
                finish();
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page.this, contacts.class));
                finish();
            }
        });
    }
}
