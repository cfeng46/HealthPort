package com.example.cfeng.healthport;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private EditText search;
    private ArrayAdapter<String> adapter;

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
        search = findViewById(R.id.searchBar);

        TextView back = findViewById(R.id.go_back);
        ImageView back_arrow = findViewById(R.id.backArrow);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String url = uploadList.get(i);
                final String file_name = name.get(i);
                final Dialog dialog = new Dialog(documents.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater m_inflater = LayoutInflater.from(documents.this);
                View m_view = m_inflater.inflate(R.layout.pdf_viewer, null);
                Button dismiss = m_view.findViewById(R.id.close);
                Button download = m_view.findViewById(R.id.download);
                Button editDoc = m_view.findViewById(R.id.editDoc);
                WebView wv = m_view.findViewById(R.id.view);
                wv.setWebViewClient(new WebViewClient());
                try {
                    String encode_url = URLEncoder.encode(url, "UTF-8");
                    wv.loadUrl("http://docs.google.com/gview?embedded=true&url="+ encode_url);
                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                wv.getSettings().setSupportZoom(true);
                wv.getSettings().setMinimumFontSize(30);
                wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                wv.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
                wv.getSettings().setDefaultTextEncodingName("UTF-8");
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setDatabaseEnabled(true);
                wv.getSettings().setDomStorageEnabled(true);
                wv.getSettings().setAllowFileAccess(true);
                wv.getSettings().setUseWideViewPort(true);
                wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                wv.getSettings().setLoadWithOverviewMode(true);
                wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadFile(file_name);
                        dialog.dismiss();
                    }
                });
                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                editDoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(documents.this, edit_document.class);
                        intent.putExtra("doc_name", file_name);
                        dialog.dismiss();
                        startActivity(intent);
                        //finish();
                    }
                });

                dialog.setContentView(m_view);
                dialog.show();
            }



        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("profile");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Log.e("bb",  " " + dataSnapshot.getChildrenCount());
                    String nameString = postSnapshot.child("DocName").getValue(String.class);
                    String urlString = postSnapshot.child("URL").getValue(String.class);
                    name.add(nameString);
                    uploadList.add(urlString);
                }


                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, name);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        currentUserDocs.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // get total available quest
//                Log.e("bb",  " " + dataSnapshot.getChildrenCount());
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

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
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                documents.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void downloadFile(String file_name) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://healthport-7d48b.appspot.com");
        StorageReference childReference = storageReference.child("Uploads");
        StorageReference file = childReference.child(file_name+".pdf");
        File rootPath = new File(Environment.getExternalStorageDirectory(), "PDF Folder");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath, file_name+".pdf");
        file.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(documents.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(documents.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
