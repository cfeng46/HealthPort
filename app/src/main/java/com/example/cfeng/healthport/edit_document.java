package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class edit_document extends AppCompatActivity {
    private EditText doc_name_box;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //private static ArrayList<String> name;
    //private ArrayList<ArrayList<String>> contactsList = new ArrayList<>();
    private DatabaseReference current_contact_db;
    private String position;
    private String sp;
    private String sp_name;
    private String sp_fax;
    private String currentDocID;
    private ArrayList<String> data;
    private String savedData;
    private DatabaseReference currentDoc;
    private String user_id;
    private  String currentDocName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_document);
        //data = getIncomingIntent();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        user_id = mAuth.getCurrentUser().getUid();

        doc_name_box = findViewById(R.id.docName_type);
        currentDocID = getIncomingIntent();
        Log.d("DocID", currentDocID);
        currentDoc = mDatabase.child(user_id).child("profile").child(currentDocID).child("DocName");
        currentDoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentDocName = dataSnapshot.getValue().toString();
                doc_name_box.setText(currentDocName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ohno", "canceled");
            }
        });

        TextView cancelText = findViewById(R.id.cancelText);
        ImageView cancelIcon = findViewById(R.id.cancelX);
        TextView saveText = findViewById(R.id.saveText);
        ImageView saveIcon = findViewById(R.id.save_icon);


        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(edit_document.this, documents.class);
                startActivity(intent);
                finish();
            }
        });
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(edit_document.this, documents.class);
                startActivity(intent);
                finish();
            }
        });


    }
    private void update() {
        final String updatedDocName = doc_name_box.getText().toString().trim();
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_user_docs = mDatabase.child(user_id).child("profile");

        if (updatedDocName.isEmpty()) {
            Toast.makeText(edit_document.this,"Please type a file name", Toast.LENGTH_SHORT).show();
        } else {
            mDatabase.child(user_id).child("profile").child(currentDocID).child("DocName").setValue(updatedDocName);
            //current_user_db.child("contact_"+Integer.valueOf(position)).removeValue();
//            current_user_db.child("contact_"+Integer.valueOf(position)).child("Name").setValue(updatedContactName);
//            current_user_db.child("contact_"+Integer.valueOf(position)).child("Fax").setValue(updatedFaxNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(change_contact.this, "Contact Updated", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(change_contact.this, contact.class);
//                        intent.putExtra("contact_name", position+","+updatedContactName+":"+updatedFaxNumber);
//                        startActivity(intent);
//                    }
//                }
//
//            });
            //Log.d("bobo", savedData);

        }
    }

    private String getIncomingIntent() {
        if (getIntent().hasExtra("doc_id")) {
            String doc_id = getIntent().getStringExtra("doc_id");
            Log.d("docID", doc_id);

            return doc_id;

        }
        else {
            Log.d("zeep", "zeep");
            return null;}
    }
}
