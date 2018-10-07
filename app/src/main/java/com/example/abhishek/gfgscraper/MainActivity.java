package com.example.abhishek.gfgscraper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText programName ;
    Button submit ;
    TextView textView ;
    boolean successfullSearch = true ;
    public String searchURL = Constants.GOOGLE_SEARCH_URL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        programName = findViewById( R.id.program_name ) ;
        submit = findViewById( R.id.submit ) ;
        textView = findViewById( R.id.text_view ) ;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchingFunction() ;
                } catch (IOException e) {
                    startActivity( new Intent( getApplicationContext() , MainActivity.class ));
                }
            }
        });
    }

    public void searchingFunction() throws IOException {
        String programToFind = programName.getText().toString() ;
        thisFunctionToExecute( programToFind ) ;

        if ( successfullSearch ) {
            Intent intent = new Intent( getApplicationContext() , SuccessActivity.class ) ;
            intent.putExtra("Found" , false ) ;
            intent.putExtra("SearchedString" , programToFind ) ;
            startActivity( intent ) ;
        } else {
            Intent intent = new Intent( getApplicationContext() , MainActivity.class ) ;
            intent.putExtra( "MainTitle" , "false" ) ;
            startActivity( intent ) ;
        }
    }

    public void thisFunctionToExecute(final String searchTerm) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("URL GENERATED : ", searchURL.toString());
                    searchURL += "?q="+searchTerm ;
                    Document doc = Jsoup.connect(searchURL).get();
                    System.out.print("the document is : " + doc );
                    if (doc == null) {
                        successfullSearch = false;
                    }
                    Elements results = doc.select("h3.r > a ");
                    if (results.size() == 0) {
                        successfullSearch = false;
                    } else {
                        if (results.first().attr("href").contains("geeksforgeeks")) {
                            successfullSearch = true ;
                            DoTheScrapingHere.Scrape ( results , searchTerm ) ;
                        } else {
                            System.out.print("The else part is executing !! ");
                            successfullSearch = false ;
                        }
                    }
                } catch (Exception e) {
                    successfullSearch = false ;
                }
            }
        });
    }
}