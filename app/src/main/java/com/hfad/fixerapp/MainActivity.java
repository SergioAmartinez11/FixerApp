package com.hfad.fixerapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONException;
import org.json.JSONObject;
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
    Toolbar toolbar;

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
        textViews.add((TextView) findViewById(R.id.info_text7));
        textViews.add((TextView) findViewById(R.id.info_text8));
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.usd_def){FROM_CURRENCY = "USD";}
                else if(id == R.id.mxn_def){FROM_CURRENCY = "MXN";}
                else if(id == R.id.cad_def){FROM_CURRENCY = "CAD";}
                else if(id == R.id.gbp_def){FROM_CURRENCY = "GBP";}
                else if(id == R.id.eur_def){FROM_CURRENCY = "EUR";}
                else if(id == R.id.jpy_def){FROM_CURRENCY = "JPY";}
                else if(id == R.id.cnd_def){FROM_CURRENCY = "CND";}
                else if(id == R.id.hkd_def){FROM_CURRENCY = "HKD";}
                cardRequest();
                return false;
            }
        });
        cardRequest();

    }

    public void onClickConversionButton(View view) {
        Intent intent = new Intent(this, ConversionActivity.class);
        intent.putExtra("DEFAULT_BASE",FROM_CURRENCY);
        startActivity(intent);

    }

    private void cardRequest(){
        //LOGICA DE CONEXION A LA API
        Request request = new Request.Builder()
                .url("https://api.apilayer.com/fixer/latest?symbols=MXN%2CGBP%2CUSD%2CEUR%2CJPY%2CCAD%2CCNY%2CHKD&base="+FROM_CURRENCY)
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
                        conversions.add( new Conversion(conversionJSON.getString("base"),"MXN","1",ratesJSON.getString("MXN")));
                        conversions.add( new Conversion(conversionJSON.getString("base"),"GBP","1",ratesJSON.getString("GBP")));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i<8;i++){
                                    String newString = conversions.get(i).getResult()+"    "+conversions.get(i).getFrom()
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





