package com.uatech.memegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView bmImage;
    TextView topText,bottomText;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetImages getImages = new GetImages((Context) getApplicationContext());
        String url ="https://ronreiter-meme-generator.p.rapidapi.com/images";
        getImages.execute(url);
    }

    public void generateMeme(View view) {

        spinner = (Spinner)findViewById(R.id.spinner);
        String selectedMeme = spinner.getSelectedItem().toString();

        DownloadMeme downloadMeme = new DownloadMeme(bmImage);

        topText = (EditText) findViewById(R.id.topText);
        bottomText = (EditText) findViewById(R.id.bottomText);
        try{
            String url ="https://ronreiter-meme-generator.p.rapidapi.com/meme?font=Impact&font_size=50&meme=";
            String concattedUrl=url.concat(selectedMeme.trim().toString().concat("&top=").concat(topText.getText().toString().concat("&bottom=").concat(bottomText.getText().toString())));
            downloadMeme.execute(concattedUrl);

        }catch (Exception e)
        {
        }
    }

    private class DownloadMeme extends AsyncTask<String ,Void,Bitmap>
    {
        ImageView imageNew;
        public DownloadMeme(ImageView bmImage) {
            imageNew = bmImage;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String result=null;
            URL url;
            HttpURLConnection httpURLConnection;
            BufferedReader reader = null;
            Bitmap bmp = null;

            try {

                url = new URL(strings[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty ("X-RapidAPI-Host", "ronreiter-meme-generator.p.rapidapi.com");
                httpURLConnection.setRequestProperty ("X-RapidAPI-Key", "40c7a5b05bmsh5db573c39aaee2ep1ee494jsn39dead5f8aba");

                InputStream inputStream = httpURLConnection.getInputStream();
                bmp = BitmapFactory.decodeStream(inputStream);
                return bmp;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                bmImage = (ImageView)findViewById(R.id.imageView5);
                bmImage.setImageBitmap(result);
            }
        }
    }

    private class GetImages extends AsyncTask<String ,Void,List<String>> {

        private Context mContext;

        public GetImages (Context context){
            mContext = context;
        }
        @Override
        protected List<String> doInBackground(String... strings) {
            String result = null;
            URL url;
            HttpURLConnection httpURLConnection;
            BufferedReader reader = null;
            StringBuffer sb = new StringBuffer();
            List<String> list;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("X-RapidAPI-Host", "ronreiter-meme-generator.p.rapidapi.com");
                httpURLConnection.setRequestProperty("X-RapidAPI-Key", "40c7a5b05bmsh5db573c39aaee2ep1ee494jsn39dead5f8aba");

                BufferedReader rd;
                rd = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));


                String line;
                while ((line = rd.readLine()) != null){
                    sb.append(line);
                    if (!rd.ready()) {
                        break;
                    }
                }
                rd.close();
                line = line.replace("\"", "");
                line = line.replace("[", "");
                line = line.replace("]", "");
                list = new ArrayList<String>(Arrays.asList(line.split(",")));


            } catch (Exception e) {
                return null;
            }


            return list;
        }

        protected void onPostExecute(List<String> result) {
            if(result != null) {
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                        android.R.layout.simple_spinner_item, result);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }
    }

    }


