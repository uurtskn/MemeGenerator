package com.uatech.memegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
}
