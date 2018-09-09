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

        Button profile = (Button) findViewById(R.id.profile);
        Button documents = (Button) findViewById(R.id.documents);
        Button uploads = (Button) findViewById(R.id.activity_uploads);
        Button contacts = (Button) findViewById(R.id.contacts);

        uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(main_page.this, uploads.class));
                finish();
            }
        });
    }
}
