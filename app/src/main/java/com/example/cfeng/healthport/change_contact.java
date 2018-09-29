package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class change_contact extends AppCompatActivity {

    private EditText name_box;
    private EditText fax_number_box;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String name;
    private ArrayList<String> contactsList = new ArrayList<String>();
    private DatabaseReference current_contact_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_contact);

        name = getIncomingIntent();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        current_contact_db = mDatabase.child(user_id).child("contacts").child(name);
        current_contact_db.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String contact_fax = (String) dataSnapshot.getValue();
                        fax_number_box = findViewById(R.id.fax_type);
                        fax_number_box.setText(contact_fax);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        TextView cancelText = findViewById(R.id.cancelText);
        ImageView cancelIcon = findViewById(R.id.cancelX);
        TextView saveText = findViewById(R.id.saveText);
        ImageView saveIcon = findViewById(R.id.save_icon);


        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(current_contact_db);
            }
        });

        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(current_contact_db);
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(change_contact.this, contacts_home.class));
                finish();
            }
        });
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(change_contact.this, contacts_home.class));
                finish();
            }
        });
    }

    private void update(final DatabaseReference currentContact) {
        final String updatedContactName = name_box.getText().toString().trim();
        final String updatedFaxNumber = fax_number_box.getText().toString().trim();
        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_user_db = mDatabase.child(user_id).child("contacts");

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

        if (updatedContactName.isEmpty() || updatedFaxNumber.isEmpty()) {
            Toast.makeText(change_contact.this,"Please type a name", Toast.LENGTH_SHORT).show();
        } else if (updatedFaxNumber.length() > 10) {
            Toast.makeText(change_contact.this,"Fax number invalid", Toast.LENGTH_SHORT).show();

        } else if (updatedContactName.equals(name)) {
            current_contact_db.setValue(updatedFaxNumber);
            Toast.makeText(change_contact.this, "Contact Updated", Toast.LENGTH_SHORT).show();
            return;
            //Toast.makeText(change_contact.this,"This contact already exists", Toast.LENGTH_SHORT).show();
        } else {
            mDatabase.child(user_id).child("contacts").child(updatedContactName).setValue(updatedFaxNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(change_contact.this, "Contact Updated", Toast.LENGTH_SHORT).show();
                        currentContact.removeValue();
                        //startActivity(new Intent(change_contact.this, contacts_home.class));
                    }
                }

            });


        }


    }

    private void collectContactNames(Iterable<DataSnapshot> contacts) {

        for (DataSnapshot singleContact: contacts){
            contactsList.add(singleContact.getKey());
        }

    }
    private String getIncomingIntent() {
        if (getIntent().hasExtra("dbContact")) {
            String contactName = getIntent().getStringExtra("dbContact");

            name_box = findViewById(R.id.name_type);
            name_box.setText(contactName);
            return(contactName);
        }
        return null;
    }
}
