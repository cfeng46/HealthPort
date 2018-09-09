package com.example.cfeng.healthport;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class photo extends AppCompatActivity {
    private ImageView selected_image;
    private Button choose_photo;
    private static final int PICK_IMAGE = 1;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        selected_image = (ImageView) findViewById(R.id.image);
        choose_photo = (Button) findViewById(R.id.choose);
        back = (ImageButton) findViewById(R.id.Verify);

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
            selected_image.setImageBitmap(bitmap);

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
        }
    }
}
