package com.example.cfeng.healthport;

import android.content.Intent;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.phaxio.Phaxio;
import com.phaxio.resources.Fax;

import java.util.ArrayList;
import java.util.HashMap;

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
    ArrayList<String> urls;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_documents);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            documents = b.getStringArrayList("documents");
            urls = b.getStringArrayList("urls");
        }

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFax();
                startActivity(new Intent(send_documents.this, home.class));
            }
        });

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFax();
                startActivity(new Intent(send_documents.this, home.class));
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
        for (DataSnapshot singleContact : contacts) {
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

    private void sendFax() {
        String key = "d5rh0tk7vpk1jyavzx11f4atkpb9zhpw9fpvi9rs";
        String secret = "f82imasl1n6wnnhcu6fmnr08hi4fdb3s0rsrtr63";
        final Phaxio phaxio = new Phaxio(key, secret);
//        final File tempFile = new File("lorem.pdf");
        String url = urls.get(0);
        HashMap<String, Object> faxParams = new HashMap<>();
        faxParams.put("to", "4048945113");
//        faxParams.put("file", tempFile);
        faxParams.put("content_url", url);
        Fax fax = phaxio.fax.create(faxParams);
    }
}
