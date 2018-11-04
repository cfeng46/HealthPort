package com.example.cfeng.healthport;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.HashMap;
import java.util.List;

public class documents extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private List<String> uploadList;
    private FirebaseAuth mAuth;
    private List<String> name;
    private TextView sendText;
    private ImageView sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        uploadList = new ArrayList<>();
        name = new ArrayList<>();
        listView = findViewById(R.id.list_view);
        final TextView upload = findViewById(R.id.upload);
        final ImageView addButton = findViewById(R.id.addButton);
        sendText = findViewById(R.id.share);
        sendButton = findViewById(R.id.sendButton);

        TextView back = findViewById(R.id.go_back);
        ImageView back_arrow = findViewById(R.id.backArrow);
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
                Log.d("URL", "This is the url:" + url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("profile");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String value = postSnapshot.getValue(String.class);
                    name.add(key);
                    uploadList.add(value);
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, name);
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

        addButton.setOnClickListener(new View.OnClickListener() {
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
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(documents.this, home.class));
            }
        });

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(documents.this, select_documents.class));
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(documents.this, select_documents.class));
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
