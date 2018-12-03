package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class document_links extends AppCompatActivity {

    private Map<String, String> documents;
    private ArrayList<Map<String, String>> docList;
    private ImageView backArrow;
    private TextView backText;
    private ListView linkList;
    private ImageView checkMark;
    private TextView doneText;
    private SimpleAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_links);

        Bundle b = getIntent().getExtras();
        documents = (HashMap<String, String>) b.getSerializable("documents");
        docList = new ArrayList<Map<String,String>>();


        backArrow = findViewById(R.id.backArrow);
        backText = findViewById(R.id.backText);
        linkList = findViewById(R.id.link_list);
        checkMark = findViewById(R.id.checkMark);
        doneText = findViewById(R.id.doneText);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(document_links.this, select_documents.class));
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(document_links.this, select_documents.class));
            }
        });

        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(document_links.this, documents.class));
            }
        });
        doneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(document_links.this, documents.class));
            }
        });

        for (String name : documents.keySet()) {
            Map<String, String> pair = new HashMap<String, String>(2);
            pair.put("name", name);
            pair.put("url", documents.get(name));
            docList.add(pair);
        }

        adapter = new SimpleAdapter(this, docList,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "url"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        linkList.setAdapter(adapter);

    }
}
