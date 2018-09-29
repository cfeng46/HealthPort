package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class contact extends AppCompatActivity {

    private static String contactName;
    private static String contactNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView backText = findViewById(R.id.backText);
        ImageView backButton = findViewById(R.id.backArrow);


        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contact.this, contacts_home.class));
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contact.this, contacts_home.class));
                finish();
            }
        });


    }

    public static void setContactName(String name) {
        contactName = name;
    }
    public static void setContactNumber(String number) {
        contactNum = number;
    }
}
