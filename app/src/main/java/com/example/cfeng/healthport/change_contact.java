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
public class change_contact extends AppCompatActivity {

    private EditText name_box;
    private EditText fax_number_box;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static ArrayList<String> name;
    private ArrayList<ArrayList<String>> contactsList = new ArrayList<>();
    private DatabaseReference current_contact_db;
    private String position;
    private String sp;
    private String sp_name;
    private String sp_fax;
    private ArrayList<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_contact);
        data = getIncomingIntent();

        name_box = findViewById(R.id.name_type);
        name_box.setText(data.get(1));
        fax_number_box = findViewById(R.id.fax_type);
        fax_number_box.setText(data.get(2));

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
                Intent intent = new Intent(change_contact.this, contact.class);
                intent.putExtra("contact_name", position+","+sp_name+":"+sp_fax);
                startActivity(intent);
                finish();
            }
        });
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(change_contact.this, contact.class);
                intent.putExtra("contact_name", position+","+sp_name+":"+sp_fax);
                startActivity(intent);
                finish();
            }
        });
    }

    private void update() {
        final String updatedContactName = name_box.getText().toString().trim();
        final String updatedFaxNumber = fax_number_box.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_user_db = mDatabase.child(user_id).child("Contacts");
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

        if (updatedContactName.isEmpty() || updatedFaxNumber.isEmpty()) {
            Toast.makeText(change_contact.this,"Please type a name", Toast.LENGTH_SHORT).show();
        } else if (updatedFaxNumber.length() > 10) {
            Toast.makeText(change_contact.this,"Fax number invalid", Toast.LENGTH_SHORT).show();

        } else if (updatedContactName.equals(sp_name)) {
            current_contact_db.child("contact_"+Integer.valueOf(position)).child("Fax").setValue(updatedFaxNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(change_contact.this, "Contact Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(change_contact.this, contact.class);
                        intent.putExtra("contact_name", position+","+sp_name+":"+updatedFaxNumber);
                        startActivity(intent);
                    }
                }
            });

        } else {
            current_user_db.child("contact_"+Integer.valueOf(position)).removeValue();
            current_user_db.child("contact_"+Integer.valueOf(position)).child("Name").setValue(updatedContactName);
            current_user_db.child("contact_"+Integer.valueOf(position)).child("Fax").setValue(updatedFaxNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(change_contact.this, "Contact Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(change_contact.this, contact.class);
                        intent.putExtra("contact_name", position+","+updatedContactName+":"+updatedFaxNumber);
                        startActivity(intent);
                    }
                }

            });
        }


    }

    private void collectContactNames(Iterable<DataSnapshot> contacts) {

        for (DataSnapshot singleContact:contacts){
            int i = 0;
            ArrayList<String> nest_list = new ArrayList<>();
            for (DataSnapshot b: singleContact.getChildren()) {
                nest_list.set(i, (String.valueOf(b.getValue())));
                i++;
            }
            contactsList.add(nest_list);
        }

    }
    private ArrayList<String> getIncomingIntent() {
        if (getIntent().hasExtra("dbContact")) {
            String contactName = getIntent().getStringExtra("dbContact");
            Log.e("djksjkjrkjksjkfjks",contactName);
            String[] items_1 = contactName.split(",");
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
            //TextView nameText = findViewById(R.id.name_value);
           // nameText.setText(sp_name);
           // String contact_fax = sp_fax;
            //Log.e("denug", contact_fax);
           // TextView fax_text = findViewById(R.id.fax_number_value);
           // fax_text.setText(contact_fax);
            ArrayList<String> a = new ArrayList<>();
            a.add(position);
            a.add(sp_name);
            a.add(sp_fax);
            name_box = findViewById(R.id.name_type);
            name_box.setText(sp_name);
            fax_number_box = findViewById(R.id.fax_type);
            fax_number_box.setText(sp_fax);
            return(a);
        }
        return null;
    }
}
