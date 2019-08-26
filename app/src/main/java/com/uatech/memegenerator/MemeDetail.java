package com.uatech.memegenerator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class MemeDetail extends AppCompatActivity {
    ImageView bmImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_detail);

        bmImage = (ImageView)findViewById(R.id.generatedImage);
        bmImage.setImageBitmap(MainActivity.generatedImage);
    }

    public void Back (View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void addImageToGallery(View view) {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }else{
            MediaStore.Images.Media.insertImage(getContentResolver(), MainActivity.generatedImage, "Meme"+"-"+UUID.randomUUID().toString(),"Generated Meme from MemeGenerator"  );
            Toast toast= Toast.makeText(getApplicationContext(),"Downloaded",Toast.LENGTH_LONG);
            toast.setMargin(50,50);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2 ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MediaStore.Images.Media.insertImage(getContentResolver(), MainActivity.generatedImage, "Meme"+"-"+UUID.randomUUID().toString(),"Generated Meme from MemeGenerator"  );
                Toast toast= Toast.makeText(getApplicationContext(),"Downloaded",Toast.LENGTH_LONG);
                toast.setMargin(20,20);
                toast.show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void shareMeme(View view){
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            MainActivity.generatedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), MainActivity.generatedImage, "Title", null);
            shareImageUri(Uri.parse(path));
    }

    private void shareImageUri(Uri uri){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        startActivity(intent);
    }
}
