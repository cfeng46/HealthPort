package com.example.cfeng.healthport;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class profile extends AppCompatActivity {

    private ImageButton back;
    private ImageButton add_file;
    private List<String> fileList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = (ImageButton) findViewById(R.id.Verify);
        add_file = (ImageButton) findViewById(R.id.add_file);

        final File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, home.class));
            }
        });
        add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(profile.this);
                final View myView = getLayoutInflater().inflate(R.layout.information_enter, null);
                final EditText name = (EditText) myView.findViewById(R.id.name);
                Button confirm = (Button) myView.findViewById(R.id.confirm);
                Button cancel = (Button) myView.findViewById(R.id.cancel);

                dialog.setView(myView);
                final AlertDialog mydialog = dialog.create();
                mydialog.show();

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!name.getText().toString().isEmpty()) {
                            try {
                                File root = new File(Environment.getExternalStorageDirectory(), "HealthPort");
                                if (!root.exists()) {
                                    root.mkdirs();
                                    File subFolder = new File (Environment.getExternalStorageDirectory() + String.format("/HealthPort/%s", name.getText().toString()));
                                    subFolder.mkdirs();
                                    mydialog.cancel();
                                } else {
                                    File subFolder = new File (Environment.getExternalStorageDirectory() + String.format("/HealthPort/%s", name));
                                    subFolder.mkdirs();
                                    mydialog.cancel();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(profile.this, "not a valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.cancel();
                    }
                });
            }
        });
    }

    public static ArrayList<String> ListFolder(String path) {
        ArrayList<String> ADir = new ArrayList<String>();
        for (File files: new File(path).listFiles()) {
            if (files.isDirectory()) {
                ADir.add("" + files);
            }
        }
        return ADir;
    }

    public void sigL(String s) {
        Toast.makeText(profile.this, s, Toast.LENGTH_LONG).show();
    }
}
