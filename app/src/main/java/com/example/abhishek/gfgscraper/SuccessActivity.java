package com.example.abhishek.gfgscraper;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SuccessActivity extends AppCompatActivity {

    public final String GOOGLE_IMAGE_SEARCH = "https://www.images.google.com" ;
    ImageView imageView ;
    TextView textView ;
    Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        imageView = findViewById( R.id.image_view ) ;
        textView = findViewById( R.id.text_view ) ;
        button = findViewById( R.id.submit ) ;

        try {
//            setImage() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        textView.setText( "PREVIOUS SEARCH : " + getIntent().getStringExtra("SearchedString").toString() ) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext() , MainActivity.class ) ) ;
            }
        });
    }

    public void setImage() throws IOException {
        String searchTerm = getIntent().getStringExtra("SearchedString").toString();
        int num = 1 ;
        String searchURL = GOOGLE_IMAGE_SEARCH + "?q=" + searchTerm + "&num=" + num;
        Document doc = Jsoup.connect(searchURL).userAgent(Constants.USER_AGENT_STRING).get();
        Elements results = doc.select("h3.r > a ");
        Element first = results.first() ;
        String url = first.attr("href") ;
        String exactLink = url.substring( 7 , url.indexOf("&" ) ) ;
        InputStream isImageFound = findTheImage( exactLink ) ;
        if ( isImageFound != null ) {
            InputStream is = isImageFound ;
            imageView.setImageBitmap(BitmapFactory.decodeStream(is));
        } else {
            imageView.setBackgroundColor(10);
        }
    }

    public InputStream findTheImage(String fileUrl ){
        try {
            URL myFileUrl = new URL(fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            return is ;

        } catch (MalformedURLException e) {
            return null ;
        } catch (Exception e) {
            return null ;
        }
    }
}
