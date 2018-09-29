package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cfeng.healthport.Model.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class contacts_home extends AppCompatActivity {

    private static final String TAG = "contacts_home";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView name;
    private RecyclerView rcvListMessage;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    //private ArrayList<Map<String, String>> mNames= new ArrayList<Map<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_home);

        TextView addText = findViewById(R.id.addText);
        ImageView addButton = findViewById(R.id.addButton);
        TextView backText = findViewById(R.id.backText);
        ImageView backButton = findViewById(R.id.backArrow);

        //Get all contact names
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

        //Set up receiver list view
        rcvListMessage = findViewById(R.id.contactList);
        rcvListMessage.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rcvListMessage.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(contactsList);
        rcvListMessage.setAdapter(mAdapter);

        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contacts_home.this, add_contacts.class));
                finish();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contacts_home.this, add_contacts.class));
                finish();
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contacts_home.this, home.class));
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contacts_home.this, home.class));
                finish();
            }
        });




        Log.d(TAG, "onCreate: started");


    }

    private void collectContactNames(Iterable<DataSnapshot> contacts) {

        for (DataSnapshot singleContact: contacts){

            //Get name
            contactsList.add(new Contact((String) singleContact.getKey(), (String) singleContact.getValue()));
        }

        Log.d("PRINTNAMES", contactsList.toString());

    }

}
