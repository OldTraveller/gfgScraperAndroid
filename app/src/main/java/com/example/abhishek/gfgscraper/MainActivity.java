package com.example.abhishek.gfgscraper;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static boolean status = false ;
    public static Button submit ;
    public static TextView textView ;
    public static EditText editText ;
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    public static StringBuilder builder = new StringBuilder("");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = findViewById( R.id.submit ) ;
        textView = (TextView)findViewById(R.id.text_view) ;
        editText = findViewById( R.id.program_name_here ) ;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( editText.getText().toString().equals("") ) {
                    Toast.makeText(getApplicationContext() , "Mind Entering Something ? " , Toast.LENGTH_SHORT ).show();
                    Intent intent = new Intent( getApplicationContext() , MainActivity.class ) ;
                    startActivity( intent ) ;
                }
                AsyncTaskRunner runner = new AsyncTaskRunner( editText.getText().toString())  ;
                System.out.println("The programFind is : " + editText.getText().toString() ) ;
                runner.execute( editText.getText().toString() ) ;
            }
        });
    }

    public void checkActivity() {
        if ( status ) {
            Intent intent = new Intent( getApplicationContext() , SuccessActivity.class ) ;
            intent.putExtra("program" , builder.toString() ) ;
            intent.putExtra("SearchedString" , editText.getText().toString() ) ;
            startActivity( intent ) ;
        } else {
            Toast.makeText( getApplicationContext() , "Couldnt find the thing !! " , Toast.LENGTH_SHORT ).show();
            Intent intent = new Intent( getApplicationContext() , MainActivity.class ) ;
            startActivity( intent ) ;
        }
    }

    class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String programToFind ;
        private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search" ;
        private StringBuilder builder = null ;
        private StringBuilder programBuilder = null ;
        public AsyncTaskRunner( String programToFind ) {
            this.programToFind = programToFind ;
            builder = new StringBuilder("") ;
            programBuilder = new StringBuilder("") ;
            Log.d("REACHED : " , "constructor" ) ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("REACHED : " , "onpreExecute" ) ;
        }

        @Override
        protected String doInBackground(String... params ) {
            Log.d("REACHED : " , "doInBackground" ) ;
            String searchTerm = programToFind ;
            Log.d("REACHED : " , searchTerm + "is this !! ") ;
            builder.append(GOOGLE_SEARCH_URL).append("?q=").append(searchTerm).append("&num=1") ;

            Log.d("REACHED : " , "Builder String is : " + builder.toString() ) ;
            Document doc = null;
            try {
                Log.d("REACHED : " , "trying to connect!! " ) ;
                doc = Jsoup.connect( builder.toString() ).get() ;
                Log.d("REACHED : " , doc.toString() ) ;
            } catch (IOException e) {
                Log.d("REACHED : " , "catch Statement " ) ;
                Log.d("REACHED : " , e.getStackTrace().toString() ) ;
                return null;
            }

            Elements results = doc.select("h3.r > a");
            if ( results.size() == 0 ) {
                return null ;
            } else {
                Toast.makeText( getApplicationContext() , "asfasdfS" , Toast.LENGTH_SHORT ).show(); ;
                if ( results.first().attr("href").contains("geeksforgeeks")) {
                    Element first = results.first() ;
                    String url = first.attr("href") ;
                    String exactLink = url.substring( 7 , url.indexOf("&" ) ) ;
                    Document reachedWebsite = null;
                    try {
                        reachedWebsite = Jsoup.connect(exactLink).get();
                        Log.d("REACHED : " , "reachedWebsite" ) ;
                    } catch (IOException e) {
                        return null;
                    }

                    System.out.println("The Title is : " + reachedWebsite.title()  );
                    Elements Program = reachedWebsite.getElementsByClass("code") ;
                    Log.d("REACHED : " , "Elememnts" ) ;
                    if ( Program != null ) {
                        for ( Element ele : Program ) {
                            System.out.println( ele.text() );
                            programBuilder.append( ele.text() + "\n" ) ;
                        }
                        System.out.println("----------------------------------------------");

                    } else {
                        return null ;
                    }
                }
            }
            Log.d("REACHED : " , "making true !!! " ) ;
            MainActivity.status = true ;
            return null ;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("REACHED : " , "onPostExecute" ) ;
            MainActivity.builder.append( programBuilder.toString() ) ;
            checkActivity();
        }
    }

}

