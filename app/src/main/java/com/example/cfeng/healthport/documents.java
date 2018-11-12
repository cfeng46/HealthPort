package com.example.cfeng.healthport;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
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
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class documents extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private ArrayList<String> urls;
    private FirebaseAuth mAuth;
    private List<String> name;
    private TextView sendText;
    private ImageView sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        urls = new ArrayList<>();
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

                final String url = urls.get(i);
                final String file_name = name.get(i);
                final Dialog dialog = new Dialog(documents.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater m_inflater = LayoutInflater.from(documents.this);
                View m_view = m_inflater.inflate(R.layout.pdf_viewer, null);
                Button dismiss = m_view.findViewById(R.id.close);
                Button download = m_view.findViewById(R.id.download);
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
                dialog.setContentView(m_view);
                dialog.show();
//                Log.d("URL", "This is the url:" + url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
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
                    urls.add(value);
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
                Intent intent = new Intent(documents.this, select_documents.class);
                Bundle b = new Bundle();
                b.putStringArrayList("urls", urls);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(documents.this, select_documents.class);
                Bundle b = new Bundle();
                b.putStringArrayList("urls", urls);
                intent.putExtras(b);
                startActivity(intent);
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
