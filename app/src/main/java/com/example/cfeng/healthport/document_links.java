package com.example.cfeng.healthport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

public class document_links extends AppCompatActivity {

    private HashMap<String, String> documents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_links);

        Bundle b = getIntent().getExtras();
        documents = (HashMap<String, String>) b.getSerializable("documents");

    }
}
