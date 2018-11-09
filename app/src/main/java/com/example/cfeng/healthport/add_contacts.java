package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import java.util.HashMap;

import static java.lang.String.valueOf;

public class add_contacts extends AppCompatActivity {
    private EditText name;
    private EditText fax_number;
    private ArrayList<String> contactsList = new ArrayList<String>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private HashMap<String,String> contactslist = new HashMap<>();

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
                new_func();
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

    private void new_func() {
        final String newName = name.getText().toString().trim();
        final String faxNumber = fax_number.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_user_db = mDatabase.child(user_id).child("Contacts");
        current_user_db.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String k = null;
                        for (DataSnapshot singleContact:dataSnapshot.getChildren()){
                            String x = null;
                            String y = null;
                            for (DataSnapshot b: singleContact.getChildren()) {
                                if (b.getKey().equals("Name")) {
                                    x = String.valueOf(b.getValue());
                                }
                                if (b.getKey().equals("Fax")) {
                                    y = String.valueOf(b.getValue());
                                }
                            }
                            contactslist.put(String.valueOf(x), String.valueOf(y));
                            k = singleContact.getKey();
                        }
                        String[] item = k.split("_");
                        String o = null;
                        for (String s: item) {
                            o = s;
                        }

                        if (newName.isEmpty() || faxNumber.isEmpty()) {
                            Toast.makeText(add_contacts.this,"You miss important information", Toast.LENGTH_SHORT).show();
                        }
                        if (faxNumber.length() > 10) {
                            Toast.makeText(add_contacts.this,"Your fax number is invalid", Toast.LENGTH_SHORT).show();
                        }
                        if (contactslist.containsKey(newName)) {
                            //Log.e("true or false", String.valueOf(contactsList.contains(newName)));
                            Toast.makeText(add_contacts.this,"This contact already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            int a = Integer.valueOf(o)+1;
                            //Log.e("size",String.valueOf(a));
                            DatabaseReference addvalue = current_user_db.child("contact_" + valueOf(a));
                            addvalue.child("Name").setValue(newName);
                            addvalue.child("Fax").setValue(faxNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(add_contacts.this, "New Contact Added", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(add_contacts.this, contacts_home.class));
                                    }
                                }
                            });
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }
}