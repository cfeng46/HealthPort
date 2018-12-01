package com.example.cfeng.healthport;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cfeng.healthport.Model.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class send_documents extends AppCompatActivity {

    ImageView backArrow;
    TextView backText;
    ImageView sendButton;
    TextView sendText;
    ListView contact_list;
    ListView doc_list;
    EditText fax_number;
    private ArrayAdapter<String> adapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    ArrayList<String> contactNames;
    ArrayList<String> contactNumbers;
    ArrayList<String> documents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_documents);

        Bundle b = getIntent().getExtras();
        documents = b.getStringArrayList("documents");

        doc_list = findViewById(R.id.doc_list);
        ArrayAdapter<String> doc_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, documents);
        doc_list.setAdapter(doc_adapter);


        backArrow = findViewById(R.id.backArrow);
        backText = findViewById(R.id.backText);
        sendButton = findViewById(R.id.sendButton);
        sendText = findViewById(R.id.sendText);
        contact_list = findViewById(R.id.contact_list);
        fax_number = findViewById(R.id.fax_number);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(send_documents.this, select_documents.class));
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(send_documents.this, select_documents.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        contactNames = new ArrayList<>();
        contactNumbers = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Contacts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectContactNames(dataSnapshot.getChildren());


                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, contactNames);
                contact_list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        contact_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
            contactNames.add(new Contact(x, y, o).getName());

        }
    }
}
