package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cfeng.healthport.Model.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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
        EditText searchBar = findViewById(R.id.searchBar);
        //searchBar.requestFocus();

        //Get all contact names
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = mDatabase.child(user_id).child("Contacts");
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
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String charString = charSequence.toString();
                if (!charString.isEmpty()) {
                    ArrayList<Contact>  newList = new ArrayList<>();
                    for (Contact row : contactsList) {
                        if (row.getName().toLowerCase().contains(charString)) {
                            newList.add(row);
                        }
                    }
                    mAdapter = new MyAdapter(newList);
                    rcvListMessage.setAdapter(mAdapter);
                } else {
                    mAdapter = new MyAdapter(contactsList);
                    rcvListMessage.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Log.d(TAG, "onCreate: started");
    }
    private void collectContactNames(Iterable<DataSnapshot> contacts) {
        String k = null;
        for (DataSnapshot singleContact:contacts) {
            //Log.e("Stitie",String.valueOf(singleContact.getChildren()));
            String x = null;
            String y = null;
            k = singleContact.getKey();
            String[] item = k.split("_");
            String o = null;
            for (String s : item) {
                o = s;
            }
            for (DataSnapshot b : singleContact.getChildren()) {
                if (b.getKey().equals("Name")) {
                    x = String.valueOf(b.getValue());
                }
                if (b.getKey().equals("Fax")) {
                    y = String.valueOf(b.getValue());
                }
            }
            contactsList.add(new Contact(x, y, o));

        }
    }

}
