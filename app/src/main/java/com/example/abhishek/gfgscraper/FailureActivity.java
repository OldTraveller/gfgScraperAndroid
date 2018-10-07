package com.example.abhishek.gfgscraper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FailureActivity extends AppCompatActivity {

    ImageView imageView ;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);
        imageView = findViewById( R.id.image_view ) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( getApplicationContext() , MainActivity.class ) ;
                intent.putExtra("Found" , "false" ) ;
                intent.putExtra( "SearchedString" , getIntent().getStringExtra("SearchedString").toString()) ;
                startActivity( intent ) ;
            }
        });
    }
}
