package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class uploads extends AppCompatActivity {
    private Button picture_button;
    private Button photo_button;
    private ImageButton back;
    private Button pdf_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        picture_button = (Button) findViewById(R.id.capture);
        pdf_button = findViewById(R.id.pdf);
        photo_button = (Button) findViewById(R.id.photo);
        back = (ImageButton) findViewById(R.id.imageButton);

        picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploads.this, capture.class));
                finish();
            }
        });

        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploads.this, photo.class));
                finish();
            }
        });

        pdf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(uploads.this, exist_pdf.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploads.this, home.class));
                finish();
            }
        });
    }
}
