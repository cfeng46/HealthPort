package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class add_contacts extends AppCompatActivity {
    private EditText name;
    private EditText fax_number;
    private ArrayList<String> contactsList = new ArrayList<String>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);


        TextView backText = findViewById(R.id.backText);
        ImageView backButton = findViewById(R.id.backArrow);
        TextView saveText = findViewById(R.id.saveText);
        ImageView saveButton = findViewById(R.id.saveButton);



        name = (EditText) findViewById(R.id.name);
        fax_number = (EditText) findViewById(R.id.fax_number);




        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_func();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_new();
                startActivity(new Intent(add_contacts.this, contacts_home.class));
            }
        });

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_contacts.this, contacts_home.class));
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_contacts.this, contacts_home.class));
                finish();
            }
        });


        }

    private void add_new() {
        final String newName = name.getText().toString().trim();
        final String faxNumber = fax_number.getText().toString().trim();
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase.child(uid).child("contacts").child(newName).setValue(faxNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(add_contacts.this, "New Contacts Added", Toast.LENGTH_SHORT).show();
                }
                new_func();
            }
        });


    }
    private void new_func() {
        final String newName = name.getText().toString().trim();
        final String faxNumber = fax_number.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = mDatabase.child(user_id).child("contacts");
        current_user_db.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectContactNames(dataSnapshot.getChildren());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        for (String member : contactsList){
            Log.i("Member name: ", member);
        }

        if (newName.isEmpty() || faxNumber.isEmpty()) {
            Toast.makeText(add_contacts.this,"You miss important information", Toast.LENGTH_SHORT).show();
        } else if (faxNumber.length() > 10) {
            Toast.makeText(add_contacts.this,"Your faxNumber is invalid", Toast.LENGTH_SHORT).show();

        } else if (contactsList.contains(newName)) {
            Toast.makeText(add_contacts.this,"This contact exist", Toast.LENGTH_SHORT).show();
        } else {
            mDatabase.child(user_id).child("contacts").child(newName).setValue(faxNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(add_contacts.this, "New Contacts Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(add_contacts.this, contacts_home.class));
                    }
                }

            });
        }


    }
    private void collectContactNames(Iterable<DataSnapshot> contacts) {

        for (DataSnapshot singleContact: contacts){
            contactsList.add((String) singleContact.getKey());
        }

    }

}