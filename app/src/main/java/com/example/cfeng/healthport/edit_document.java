package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
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
    private String currentDocName;
    private ArrayList<String> data;
    private String savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_document);
        //data = getIncomingIntent();

        doc_name_box = findViewById(R.id.docName_type);
        currentDocName = getIncomingIntent();
        Log.d("docName", currentDocName);
        doc_name_box.setText(currentDocName);

        TextView cancelText = findViewById(R.id.cancelText);
        ImageView cancelIcon = findViewById(R.id.cancelX);
        TextView saveText = findViewById(R.id.saveText);
        ImageView saveIcon = findViewById(R.id.save_icon);


        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update();
            }
        });

        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update();
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
//    private void update() {
//        final String updatedDocName = doc_name_box.getText().toString().trim();
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        String user_id = mAuth.getCurrentUser().getUid();
//        final DatabaseReference current_user_docs = mDatabase.child(user_id).child("profile");
//
//        if (updatedDocName.isEmpty()) {
//            Toast.makeText(edit_document.this,"Please type a file name", Toast.LENGTH_SHORT).show();
//        } else {
//            //current_user_db.child("contact_"+Integer.valueOf(position)).removeValue();
//            DatabaseReference currentDoc = current_user_docs.child(currentDocName);
//            //String savedData;
//            currentDoc.addListenerForSingleValueEvent(Anew ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String value = (String) dataSnapshot.getValue();
//                    savedData = value;
//                    Log.d("okay", "okay");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.d("not okay", "not okay");
//
//                }
//            });
//            Log.d("bobo", savedData);
//
//        }
//    }

    private String getIncomingIntent() {
        if (getIntent().hasExtra("doc_name")) {
            String docName = getIntent().getStringExtra("doc_name");
            Log.d("doc", docName);

            return docName;
            //String[] items_1 = contactName.split(",");
//            for(String temp:items_1) {
//                if(position == null) {
//                    position = temp;
//                } else{
//                    sp = temp;
//                }
//            }
//            String[] items_2 = sp.split(":");
//            for(String temp:items_2) {
//                if(sp_name == null) {
//                    sp_name = temp;
//                } else{
//                    sp_fax = temp;
//                }
//            }
//            ArrayList<String> a = new ArrayList<>();
//            a.add(position);
//            a.add(sp_name);
//            a.add(sp_fax);
//            doc_name_box = findViewById(R.id.docName_type);
//            doc_name_box.setText(sp_name);
//            return(a);
        }
        else {
            Log.d("zeep", "zeep");
            return null;}
    }
}
