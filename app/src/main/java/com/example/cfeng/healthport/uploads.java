package com.example.cfeng.healthport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class uploads extends AppCompatActivity {
    private ImageView picture_button, pdf_button, photo_button, backArrow;
    private TextView picture_text, pdf_text, photo_text, backText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        picture_text = findViewById(R.id.capture);
        picture_button = findViewById(R.id.camera);
        pdf_text = findViewById(R.id.pdf);
        pdf_button = findViewById(R.id.pdfIcon);
        photo_text = findViewById(R.id.photo);
        photo_button = findViewById(R.id.image);
        backArrow = findViewById(R.id.backArrow);
        backText = findViewById(R.id.backText);

        picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploads.this, capture.class));
                finish();
            }
        });
        picture_text.setOnClickListener(new View.OnClickListener() {
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
        photo_text.setOnClickListener(new View.OnClickListener() {
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
        pdf_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(uploads.this, exist_pdf.class));
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploads.this, documents.class));
                finish();
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploads.this, documents.class));
                finish();
            }
        });
    }
}
