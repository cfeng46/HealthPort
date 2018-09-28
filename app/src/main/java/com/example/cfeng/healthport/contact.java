package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class contact extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static String contactName;
    private static String contactNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        String name = getIncomingIntent();
        Log.d("mytag", name);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_contact_db = mDatabase.child(user_id).child("contacts").child(name);
        current_contact_db.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String contact_fax = (String) dataSnapshot.getValue();
                        TextView name = findViewById(R.id.fax_number_value);
                        name.setText(contact_fax);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


        ImageView update_contact = findViewById(R.id.change_contact_icon);
        TextView update_contact_text = findViewById(R.id.change_contact);
        TextView backText = findViewById(R.id.backText);
        ImageView backButton = findViewById(R.id.backArrow);
        TextView deleteText = findViewById(R.id.delete_contact_text);

        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(current_contact_db);
            }
        });

        update_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(contact.this, change_contact.class));
            }
        });

        update_contact_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(contact.this, change_contact.class));
            }
        });


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

    private String getIncomingIntent() {
        if (getIntent().hasExtra("contact_name")) {
            String nameDisplay = getIntent().getStringExtra("contact_name");

            TextView name = findViewById(R.id.name_value);
            name.setText(nameDisplay);
            return(nameDisplay);
        }
        return null;
    }
    private void deleteContact(DatabaseReference contactReference) {
        contactReference.removeValue();

        Toast.makeText(this, "Contact is deleted.", Toast.LENGTH_LONG).show();
    }

    public static void setContactName(String name) {
        contactName = name;
    }
    public static void setContactNumber(String number) {
        contactNum = number;
    }
}
