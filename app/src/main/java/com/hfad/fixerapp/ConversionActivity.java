package com.hfad.fixerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.nearby.messages.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ConversionActivity extends AppCompatActivity {
    Bundle data;
    private String amount;
    private final OkHttpClient client = new OkHttpClient();
    private String TO_CURRENCY="USD";
    private String FROM_CURRENCY;
    Spinner ToSpinner,FromSpinner;
    TextView textResult;
    EditText editTextAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        ToSpinner = findViewById(R.id.spinnerTo);
        FromSpinner = findViewById(R.id.spinnerFrom);
        final Button button = findViewById(R.id.convertButtonActivity);
        textResult = findViewById(R.id.result_currencyText);
        editTextAmount = findViewById(R.id.base_currencyInput);
        data = getIntent().getExtras();
        FROM_CURRENCY = data.getString("DEFAULT_BASE");

        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(1,"MXN"));
        currencies.add(new Currency(2,"USD"));
        currencies.add(new Currency(3,"EUR"));
        currencies.add(new Currency(4,"JPY"));
        currencies.add(new Currency(5,"CAD"));
        currencies.add(new Currency(6,"CNY"));
        currencies.add(new Currency(7,"HKD"));

        ArrayAdapter<Currency> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,currencies);
        ToSpinner.setAdapter(adapter);
        FromSpinner.setAdapter(adapter);
        ToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Currency actualCurrency = (Currency) ToSpinner.getSelectedItem();
                TO_CURRENCY = actualCurrency.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        FromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Currency actualCurrency = (Currency) FromSpinner.getSelectedItem();
                FROM_CURRENCY = actualCurrency.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void onClickConversionButton(View view){
        EditText text = (EditText) findViewById(R.id.base_currencyInput);
        amount = text.getText().toString();
        if(amount != null){
            getRequest();
        }
    }

    private void getRequest(){
        //LOGICA DE CONEXION A LA API
        Request request = new Request.Builder()
                .url("https://api.apilayer.com/fixer/convert?to="+TO_CURRENCY
                        +"&from="+FROM_CURRENCY+"&amount="+amount)
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
                        Conversion conversion = new Conversion();
                        conversion.setResult(conversionJSON.getString("result"));
                        JSONObject query = conversionJSON.getJSONObject("query");
                        conversion.setTo(query.getString("to"));
                        conversion.setFrom(query.getString("from"));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textResult.setText(conversion.getResult());

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