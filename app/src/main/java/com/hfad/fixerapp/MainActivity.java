package com.hfad.fixerapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    private String FROM_CURRENCY = "MXN";
    ArrayList<TextView> textViews = new ArrayList<>();
    private TextView secondCard;
    private TextView gpsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button conversionButton = (Button) findViewById(R.id.conversion_bt);
        textViews.add((TextView) findViewById(R.id.info_text1));
        textViews.add((TextView) findViewById(R.id.info_text2));
        textViews.add((TextView) findViewById(R.id.info_text3));
        textViews.add((TextView) findViewById(R.id.info_text4));
        textViews.add((TextView) findViewById(R.id.info_text5));
        textViews.add((TextView) findViewById(R.id.info_text6));
        gpsTextView = (TextView) findViewById(R.id.text_gps);
        cardRequest();

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                //EXPLICACION DE PERMISO DENEGADO ASINCRONO
            }
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }


    }

    public void onClickConversionButton(View view) {
        Intent intent = new Intent(this, ConversionActivity.class);
        intent.putExtra("DEFAULT_BASE","MXN");
        startActivity(intent);

    }

    private void cardRequest(){
        //LOGICA DE CONEXION A LA API
        Request request = new Request.Builder()
                .url("https://api.apilayer.com/fixer/latest?symbols=USD%2CEUR%2CJPY%2CCAD%2CCNY%2CHKD&base="+FROM_CURRENCY)
                .addHeader("apikey","GoWHRWa5IvUGnjDsz4xvPco6kh8Hsze0").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject conversionJSON = new JSONObject(response.body().string());
                        JSONObject ratesJSON = conversionJSON.getJSONObject("rates");
                        ArrayList<Conversion> conversions = new ArrayList<>();
                        conversions.add( new Conversion(conversionJSON.getString("base"),"USD","1",ratesJSON.getString("USD")));
                        conversions.add( new Conversion(conversionJSON.getString("base"),"CAD","1",ratesJSON.getString("CAD")));
                        conversions.add( new Conversion(conversionJSON.getString("base"),"EUR","1",ratesJSON.getString("EUR")));
                        conversions.add( new Conversion(conversionJSON.getString("base"),"JPY","1",ratesJSON.getString("JPY")));
                        conversions.add( new Conversion(conversionJSON.getString("base"),"CNY","1",ratesJSON.getString("CNY")));
                        conversions.add( new Conversion(conversionJSON.getString("base"),"HKD","1",ratesJSON.getString("HKD")));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i<6;i++){
                                    String newString = conversions.get(i).getResult()+"\t"+conversions.get(i).getFrom()
                                            +" -> "+conversions.get(i).getTo();
                                    textViews.get(i).setText(newString);
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        //FIN DE REQUEST DE API
    }
    }





