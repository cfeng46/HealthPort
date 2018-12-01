package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class select_documents extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private DatabaseReference databaseReference;
    private List<String> docList;
    private FirebaseAuth mAuth;
    private List<String> docNames;
    private ImageView cancelX;
    private TextView cancelText;
    private ImageView saveButton;
    private TextView saveText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_documents);

        docList = new ArrayList<>();
        docNames = new ArrayList<>();

        listView = findViewById(R.id.doc_list);
        cancelX = findViewById(R.id.cancelX);
        cancelText = findViewById(R.id.cancelText);
        saveButton = findViewById(R.id.saveButton);
        saveText = findViewById(R.id.saveText);

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(select_documents.this, documents.class));
            }
        });
        cancelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(select_documents.this, documents.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("profile");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Log.e("bb",  " " + dataSnapshot.getChildrenCount());
                    String nameString = postSnapshot.child("DocName").getValue(String.class);
                    String urlString = postSnapshot.child("URL").getValue(String.class);
                    docNames.add(nameString);
                    docList.add(urlString);
                }


                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, docNames);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for(int i = 0; i < checkedItems.size(); i++) {
                    int position = checkedItems.keyAt(i);
                    if(checkedItems.valueAt(i)) {
                        selectedItems.add(adapter.getItem(position));
                    }
                }

                Intent intent = new Intent(select_documents.this, send_documents.class);
                Bundle b = new Bundle();
                b.putStringArrayList("documents", selectedItems);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for(int i = 0; i < checkedItems.size(); i++) {
                    int position = checkedItems.keyAt(i);
                    if(checkedItems.valueAt(i)) {
                        selectedItems.add(adapter.getItem(position));
                    }
                }

                Intent intent = new Intent(select_documents.this, send_documents.class);
                Bundle b = new Bundle();
                b.putStringArrayList("documents", selectedItems);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
