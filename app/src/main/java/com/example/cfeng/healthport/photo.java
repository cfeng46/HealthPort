package com.example.cfeng.healthport;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    //private EditText name;
//    private EditText profile;
    private Button choose_photo;
    private static final int PICK_IMAGE = 1;
    private ImageButton back;
    private DatabaseReference database;
    private StorageReference storage;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private PdfDocument pdfDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        //choose_photo = findViewById(R.id.choose);
        //back = findViewById(R.id.Verify);
        //name = findViewById(R.id.fileName);
//        profile = findViewById(R.id.profile);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        pdfDoc = new PdfDocument();


//        Intent myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(myIntent, PICK_IMAGE);

        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "android.intent.action.SEND_MULTIPLE"), 1);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d("Pages", "d "+ data.getClipData().getItemCount() + " " + data.getClipData().getDescription());// Get count of image here.
        ClipData clip = data.getClipData();
        if (clip == null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //int pageNum = pdfDoc.getPages().size() + 1;
                PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 0).create();
                PdfDocument.Page page = pdfDoc.startPage(pi);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#FFFFFF"));
                canvas.drawPaint(paint);

                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                paint.setColor(Color.BLUE);
                canvas.drawBitmap(bitmap,0,0,null);

                pdfDoc.finishPage(page);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final int numPages = clip.getItemCount();
            for(int x = 0; x  < numPages; x++) {
                ClipData.Item image = clip.getItemAt(x);
                Uri imageUri = image.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    //int pageNum = pdfDoc.getPages().size() + 1;
                    PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), x).create();
                    PdfDocument.Page page = pdfDoc.startPage(pi);
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.parseColor("#FFFFFF"));
                    canvas.drawPaint(paint);

                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    paint.setColor(Color.BLUE);
                    canvas.drawBitmap(bitmap,0,0,null);

                    pdfDoc.finishPage(page);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        //int x = 0;

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
                pdfDoc.writeTo(fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfDoc.close();
            Uri contentUri = Uri.fromFile(file);

            upload(contentUri);
        }


    private void upload(final Uri contentUri) {
        final Dialog dialog = new Dialog(photo.this);
        dialog.setContentView(R.layout.confirmation_page);
        ImageView green_check = dialog.findViewById(R.id.yes);
        ImageView cancel_cross = dialog.findViewById(R.id.no);
        final EditText file_name = dialog.findViewById(R.id.file_name);

        DatabaseReference currentUserDocs = null;


        green_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!file_name.getText().toString().isEmpty()) {
                    dialog.dismiss();

                    finishUpload(contentUri, file_name.getText().toString());
                } else {
                    Log.d("FILE_NAME1", file_name.getText().toString());
                    Toast.makeText(photo.this, "Missing information", Toast.LENGTH_SHORT).show();
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

    private void finishUpload(final Uri contentUri, final String file_name) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file....");
        progressDialog.setProgress(0);
        progressDialog.show();
        //final String fileName = System.currentTimeMillis() + "";
        final String uid = mAuth.getCurrentUser().getUid();
        final DatabaseReference documents = database.child(uid).child("profile");

        final StorageReference ref = storage.child("Uploads").child(file_name+".pdf");
        UploadTask uploadTask = ref.putFile(contentUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(photo.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
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
                    final String url = downloadUri.toString();
                    Map report = new HashMap();
                    report.put(file_name, url);
                    DatabaseReference addDoc = documents.push();
                    addDoc.child("DocName").setValue(file_name);
                    addDoc.child("URL").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(photo.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(photo.this, documents.class));
                            } else {
                                Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

//                    documents.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            DatabaseReference addDoc = documents.child("doc_"+dataSnapshot.getChildrenCount());
//                            addDoc.child("DocName").setValue(file_name);
//                            addDoc.child("URL").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(photo.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(photo.this, documents.class));
//                                        finish();
//                                    } else {
//                                        Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
                }
            }
        });
//
//        storage.child("Uploads").child(fileName).putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                String url = taskSnapshot.getStorage().getDownloadUrl().toString();
//                Map report = new HashMap();
//                report.put(file_name, url);
////                database.child(uid).child(profile.getText().toString()).updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        if (task.isSuccessful()) {
////                            Toast.makeText(photo.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
//                database.child(uid).child("profile").updateChildren(report).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(photo.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(photo.this, documents.class));
//                        } else {
//                            Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(photo.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();;
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
