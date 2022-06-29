package com.hfad.fixerapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button conversionButton = (Button) findViewById(R.id.conversion_bt);

    }

    public void onClickConversionButton(View view) {
        Intent intent = new Intent(this, ConversionActivity.class);
        intent.putExtra("DEFAULT_BASE","MXN");
        startActivity(intent);

    }

}




