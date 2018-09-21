package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;


public class contacts extends AppCompatActivity {

    private static final String TAG = "contacts";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView name;
    //private RecyclerView rcvListMessage;
    //private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Map<String, Object>> mNames= new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Button add = (Button) findViewById(R.id.add);
        Button back = (Button) findViewById(R.id.back);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contacts.this, add_contacts.class));
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contacts.this, main_page.class));
                finish();
            }
        });



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = mDatabase.child(user_id);


        Log.d(TAG, "onCreate: started");


    }


}
