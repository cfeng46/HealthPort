package com.example.cfeng.healthport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import java.util.UUID;

public class photo extends AppCompatActivity {

    private EditText name;
//    private EditText profile;
    private Button choose_photo;
    private static final int PICK_IMAGE = 1;
    private ImageButton back;
    private DatabaseReference database;
    private StorageReference storage;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        choose_photo = findViewById(R.id.choose);
        back = findViewById(R.id.Verify);
        name = findViewById(R.id.fileName);
//        profile = findViewById(R.id.profile);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(photo.this, uploads.class));
            }
        });

        choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                */
                Intent myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(myIntent, PICK_IMAGE);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String myPath;
            //String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null,null);
            //cursor.moveToFirst();
            //int columnIndex = cursor.getColumnIndex(filePath[0]);
            //String myPath = cursor.getColumnName(columnIndex);
            if  (cursor == null) {
                myPath = selectedImageUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                myPath = cursor.getString(idx);
                cursor.close();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(myPath);

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(),1).create();
            PdfDocument.Page page = pdfDocument.startPage(pi);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap,0,0,null);

            pdfDocument.finishPage(page);

            /*
            ContextWrapper cw = new ContextWrapper((getApplicationContext()));
            File root = cw.getDir("profile", Context.MODE_PRIVATE);
            if (!root.exists()) {
                root.mkdir();
            }
            */
            File root = new File(Environment.getExternalStorageDirectory(), "PDF Folder");
            if (!root.exists()) {
                root.mkdir();
            }
            File file = new File(root, "picture" + UUID.randomUUID().toString() + ".pdf");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                pdfDocument.writeTo(fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfDocument.close();
            Uri contentUri = Uri.fromFile(file);
            if (!name.getText().toString().isEmpty()) {
                upload(contentUri, name.getText().toString());
            } else {
                Toast.makeText(photo.this, "Missing information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upload(Uri contentUri, final String file_name) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file....");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String fileName = System.currentTimeMillis() + "";
        final String uid = mAuth.getCurrentUser().getUid();

        storage.child("Uploads").child(fileName).putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getDownloadUrl().toString();
                Map report = new HashMap();
                report.put(file_name, url);
//                database.child(uid).child(profile.getText().toString()).updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(photo.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                database.child(uid).updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(photo.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(photo.this, documents.class));
                        } else {
                            Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();;
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }
}
