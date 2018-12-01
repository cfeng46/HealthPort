package com.example.cfeng.healthport;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class exist_pdf extends AppCompatActivity {
    final static int PICK_PDF_CODE = 1;
    private EditText profile_name;
    //private  EditText file_name;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        //Button choose = findViewById(R.id.pdf_choose);
//        profile_name = findViewById(R.id.pdf_profile);
        //file_name = findViewById(R.id.pdf_file);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        getPDF();

        /*choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!file_name.getText().toString().isEmpty()) {
                    getPDF();
                } else {
                    Toast.makeText(exist_pdf.this, "Missing information", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }
    private void getPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(exist_pdf.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null/* && !file_name.getText().toString().isEmpty()*/) {
                uploadFile(data.getData()/*, file_name.getText().toString()*/);
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(final Uri data/*, final String file_name*/) {
        final Dialog dialog = new Dialog(exist_pdf.this);
        dialog.setContentView(R.layout.confirmation_page);
        ImageView green_check = dialog.findViewById(R.id.yes);
        ImageView cancel_cross = dialog.findViewById(R.id.no);
//                    final EditText profile_name = dialog.findViewById(R.id.profile_name);
        final EditText file_name = dialog.findViewById(R.id.file_name);
        green_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!file_name.getText().toString().isEmpty()) {
                    dialog.dismiss();

                    finishUpload(data, file_name.getText().toString());
                } else {
                    Log.d("FILE_NAME1", file_name.getText().toString());
                    Toast.makeText(exist_pdf.this, "Missing information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private void finishUpload(final Uri data, final String name) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file....");
        progressDialog.setProgress(0);
        progressDialog.show();
        //final String fileName = System.currentTimeMillis() + "";
        final String uid = mAuth.getCurrentUser().getUid();
        final StorageReference ref = storageReference.child("Uploads").child(name+".pdf");
        UploadTask uploadTask = ref.putFile(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(exist_pdf.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(exist_pdf.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
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
                    databaseReference.child(uid).child("profile").updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(exist_pdf.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(exist_pdf.this, documents.class));
                            } else {
                                Toast.makeText(exist_pdf.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
//        storageReference.child("Uploads").child(fileName).putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String url = uri.toString();
//                        Map report = new HashMap();
//                        report.put(name, url);
//                        databaseReference.child(uid).child("profile").updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(exist_pdf.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(exist_pdf.this, documents.class));
//                                } else {
//                                    Toast.makeText(exist_pdf.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//                });
////                Map report = new HashMap();
////                report.put(name, url);
////                databaseReference.child(uid).child("profile").updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        if (task.isSuccessful()) {
////                            Toast.makeText(exist_pdf.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
////                            startActivity(new Intent(exist_pdf.this, documents.class));
////                        } else {
////                            Toast.makeText(exist_pdf.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
////                databaseReference.child(uid).child(profile_name).updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        if (task.isSuccessful()) {
////                            Toast.makeText(exist_pdf.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(exist_pdf.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(exist_pdf.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();;
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                progressDialog.setProgress(currentProgress);
//            }
//        });
    }
}
