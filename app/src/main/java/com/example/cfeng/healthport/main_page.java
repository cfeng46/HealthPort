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
        Button documents = (Button) findViewById(R.id.documents);
        Button uploads = (Button) findViewById(R.id.uploads);
        Button share = (Button) findViewById(R.id.share);
        Button contacts = (Button) findViewById(R.id.contacts);
        Button inbox = (Button) findViewById(R.id.inbox);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(main_page.this, MainActivity.class));
                finish();
            }
        });

        uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(main_page.this, uploads.class));
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
    }
}
