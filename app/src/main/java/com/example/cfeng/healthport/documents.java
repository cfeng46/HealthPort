package com.example.cfeng.healthport;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class documents extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private List<String> uploadList;
    private FirebaseAuth mAuth;
    private List<String> name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        uploadList = new ArrayList<>();
        name = new ArrayList<>();
        listView = findViewById(R.id.list_view);
        final Button upload = findViewById(R.id.upload);
        ImageButton back = findViewById(R.id.go_back);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String url = uploadList.get(i);
//                final Dialog dialog = new Dialog(documents.this);
//                dialog.setContentView(R.layout.pdf_viewer);
//                Button dismiss = findViewById(R.id.close_window);
////                WebView wv = findViewById(R.id.view);
//////                wv.loadUrl(url);
//                dismiss.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String value = postSnapshot.getValue(String.class);
                    name.add(key);
                    uploadList.add(value);
                }
                List<String> data = name.subList(1,name.size());


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, data);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(documents.this, uploads.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(documents.this, home.class));
            }
        });
    }

//    private void dialog() {
//        final Dialog log = new Dialog(documents.this);
//        log.setContentView(R.layout.pdf_viewer);
//        Button close = findViewById(R.id.close_window);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                log.dismiss();
//            }
//        });
//        log.show();
//    }

}
