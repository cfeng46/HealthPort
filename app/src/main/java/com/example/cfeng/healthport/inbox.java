package com.example.cfeng.healthport;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class inbox extends AppCompatActivity {

    private ImageView backArrow;
    private TextView backText;
    private ListView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference inboxDB;
    private ArrayList<String> faxedDocs;
    private ArrayList<String> docNames;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        backArrow = findViewById(R.id.backArrow);
        backText = findViewById(R.id.backText);
        listView = findViewById(R.id.list_view);

        docNames = new ArrayList<>();
        faxedDocs = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(inbox.this, home.class));
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(inbox.this, home.class));
            }
        });

        //store faxed documents under user -> inbox
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        inboxDB = databaseReference.child("inbox");
        inboxDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String value = postSnapshot.getValue(String.class);
                    docNames.add(key);
                    faxedDocs.add(value);
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, docNames);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                final Dialog dialog = new Dialog(inbox.this);
                dialog.setContentView(R.layout.faxed_document_popup);
                ImageView backArrow = dialog.findViewById(R.id.no);
                TextView backText = dialog.findViewById(R.id.back);
                ImageView addButton = dialog.findViewById(R.id.yes);
                TextView addText = dialog.findViewById(R.id.add);
                TextView name = dialog.findViewById(R.id.file_name);
                name.setText(docNames.get(i));

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transferDoc(docNames.get(index), Uri.parse(faxedDocs.get(index)));
                        dialog.dismiss();
                    }
                });
                addText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                backText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }



        });
    }

    private void transferDoc(final String name, Uri uri) {
        final StorageReference ref = storage.child("Uploads").child(name+".pdf");
        UploadTask uploadTask = ref.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(inbox.this, "File not successfully transferred", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(inbox.this, "File successfully transferred", Toast.LENGTH_SHORT).show();
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri  downloadUri = task.getResult();
                    String url = downloadUri.toString();
                    Map report = new HashMap();
                    report.put(name, url);
                    databaseReference.child("profile").updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                inboxDB.child(name).removeValue();
                                Toast.makeText(inbox.this, "File successfully transferred", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(inbox.this, documents.class));
                            } else {
                                Toast.makeText(inbox.this, "File not successfully transferred", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
