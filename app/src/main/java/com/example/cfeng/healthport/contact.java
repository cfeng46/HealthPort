package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
public class contact extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static String name;

    private static String contactName;
    private static String contactNum;
    private ArrayList<ArrayList<String>> contact_list = new ArrayList<>();
    private String position;
    private String sp;
    private String sp_name;
    private String sp_fax;
    private int num = 0;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        data = getIncomingIntent();
        TextView nameText = findViewById(R.id.name_value);
        nameText.setText(data.get(1));
        TextView fax_text = findViewById(R.id.fax_number_value);
        fax_text.setText(data.get(2));
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_contact_db = mDatabase.child(user_id).child("Contacts");

        ImageView update_contact = findViewById(R.id.change_contact_icon);
        TextView update_contact_text = findViewById(R.id.change_contact_text);
        TextView backText = findViewById(R.id.backText);
        ImageView backButton = findViewById(R.id.backArrow);
        TextView deleteText = findViewById(R.id.delete_contact_text);
        ImageView deleteIcon = findViewById(R.id.delete_contact_icon);

        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(current_contact_db);
//                finish();
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(current_contact_db);
                //finish();
            }
        });

        update_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            editContact();
            finish();

            }
        });

        update_contact_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            editContact();
            finish();
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

    private ArrayList<String> getIncomingIntent() {
        if (getIntent().hasExtra("contact_name")) {
            String nameDisplay = getIntent().getStringExtra("contact_name");
            String[] items_1 = nameDisplay.split(",");
            for(String temp:items_1) {
                if(position == null) {
                    position = temp;
                } else{
                    sp = temp;
                }
            }
            String[] items_2 = sp.split(":");
            for(String temp:items_2) {
                if(sp_name == null) {
                    sp_name = temp;
                } else{
                    sp_fax = temp;
                }
            }
            TextView nameText = findViewById(R.id.name_value);
            nameText.setText(sp_name);
            String contact_fax = sp_fax;
            //Log.e("denug", contact_fax);
            TextView fax_text = findViewById(R.id.fax_number_value);
            fax_text.setText(contact_fax);
            ArrayList<String> a = new ArrayList<>();
            a.add(position);
            a.add(sp_name);
            a.add(sp_fax);
            Log.e("kdkdkdkdkdkkd",String.valueOf(a));
            return(a);

        }
        return null;
    }

    private void editContact() {
        Log.d("msg", "clicked update");
        Intent intent = new Intent(contact.this, change_contact.class);
        Log.d("msg", "intent made");
        intent.putExtra("dbContact", String.valueOf(position)+","+sp_name+":"+sp_fax);
        startActivity(intent);


    }
    private void deleteContact(DatabaseReference contactReference) {
        contactReference.child("contact_"+String.valueOf(position)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(contact.this, "Contact is deleted.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(contact.this, contacts_home.class));
                }
            }
        });

    }

    public static void setContactName(String contName) {
        contactName = contName;
    }
    public static void setContactNumber(String number) {
        contactNum = number;
    }

}
