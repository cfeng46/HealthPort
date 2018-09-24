package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class add_contacts extends AppCompatActivity {
    private EditText name;
    private EditText fax_number;
    private FirebaseAuth mAuth;
    private  DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        Button save = (Button) findViewById(R.id.save);
        Button back = (Button) findViewById(R.id.back);


        name = (EditText) findViewById(R.id.name);
        fax_number = (EditText) findViewById(R.id.fax_number);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newName = name.getText().toString().trim();
                final String faxNumber = fax_number.getText().toString().trim();
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = mDatabase.child(user_id);
                current_user_db.child("contacts_home").child(newName).setValue(faxNumber);
                startActivity(new Intent(add_contacts.this, contacts_home.class));
                Toast.makeText(add_contacts.this, "New Contacts Added", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_contacts.this, contacts_home.class));
                finish();
            }
        });
    }

}