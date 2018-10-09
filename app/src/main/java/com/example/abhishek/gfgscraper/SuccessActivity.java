package com.example.abhishek.gfgscraper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    TextView textView ;
    Button button ;
    TextView programView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        textView = findViewById( R.id.text_view ) ;
        button = findViewById( R.id.submit ) ;
        programView = findViewById( R.id.program_area ) ;
        programView.clearComposingText();
        textView.setText( "Searched For : " + getIntent().getStringExtra("SearchedString").toString() );
        programView.setText( getIntent().getStringExtra("program").toString() );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext() , MainActivity.class ) );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        programView.clearComposingText();
    }
}
