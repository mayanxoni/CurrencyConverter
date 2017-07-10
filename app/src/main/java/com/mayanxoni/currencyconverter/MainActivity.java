package com.mayanxoni.currencyconverter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextView eur, usd, inr;
    private EditText editText;
    private String result[] = new String[10];
    private int index;
    private double inputvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usd = (TextView)findViewById(R.id.usdRate);
        eur = (TextView)findViewById(R.id.eurRate);
        inr = (TextView)findViewById(R.id.inrRate);
        editText = (EditText) findViewById(R.id.editText);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Button buttonConvert = (Button) findViewById(R.id.buttonConvert);

        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parent, View view, int position, long id){
                index = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?>parent){

            }
        });
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usd.setText("wait...");
                eur.setText("wait...");
                inr.setText("wait...");

                if(editText.getText().toString().trim().length() > 0 && !editText.getText().toString().trim().equals(".")) {
                    String textView = editText.getText().toString();
                    inputvalue = Double.parseDouble(textView);
                }
                new Calculate().execute();
            }
        });
    }



//    public void convert(View view) {
//        EditText editText = (EditText) findViewById(R.id.editTextRupees);
//        String input = editText.getText().toString();
//        if(!input.matches("")) {
//            Double dollar = Double.parseDouble(input);
//            double price = dollar * 64.59;
//            Toast.makeText(this, input + " Dollars = ₹" + price, Toast.LENGTH_SHORT).show();
//        }
//        else if(input.matches(""))
//            Toast.makeText(this,"Please enter an amount!", Toast.LENGTH_SHORT).show();
//    }

    private class Calculate extends AsyncTask<Object, Object, String[]> {

        @Override
        protected String[] doInBackground(Object... strings) {
            if(index==0) {
                String uRL;
                try {
                    uRL = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22USDEUR,USDINR%22)&format=json&env=store://datatables.org/alltableswithkeys");
                    JSONObject USDtoObj = new JSONObject(uRL);
                    JSONArray rateArray = USDtoObj.getJSONObject("query").getJSONObject("results").getJSONArray("rate");
                    result[0] = rateArray.getJSONObject(0).getString("Rate");
                    result[1] = rateArray.getJSONObject(1).getString("Rate");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
            else if(index==1){
                String uRL;
                try {
                    uRL = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22EURUSD,EURINR%22)&format=json&env=store://datatables.org/alltableswithkeys");
                    JSONObject USDtoObj = new JSONObject(uRL);
                    JSONArray rateArray = USDtoObj.getJSONObject("query").getJSONObject("results").getJSONArray("rate");
                    result[0] = rateArray.getJSONObject(0).getString("Rate");
                    result[1] = rateArray.getJSONObject(1).getString("Rate");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
            else if(index==2){
                String uRL;
                try {
                    uRL = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22INRUSD,INREUR%22)&format=json&env=store://datatables.org/alltableswithkeys");
                    JSONObject USDtoObj = new JSONObject(uRL);
                    JSONArray rateArray = USDtoObj.getJSONObject("query").getJSONObject("results").getJSONArray("rate");
                    result[0] = rateArray.getJSONObject(0).getString("Rate");
                    result[1] = rateArray.getJSONObject(1).getString("Rate");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] s) {
            if(index==0){
                double usdtoeurval, usdtoinrval, usdtoeurinp, usdtoinrinp, usdtousdinp;

                usdtousdinp = inputvalue * 1;
                usd.setText(""+usdtousdinp);

                usdtoeurval = Double.parseDouble(result[0]);
                usdtoeurinp = inputvalue * usdtoeurval;
                eur.setText(""+usdtoeurinp);

                usdtoinrval = Double.parseDouble(result[1]);
                usdtoinrinp = inputvalue * usdtoeurval;
                eur.setText(""+usdtoinrinp);
            }
            else if(index==1){
                double eurtousdval, eurtoinrval, eurtousdinp, eurtoinrinp, eurtoeurinp;

                eurtoeurinp = inputvalue * 1;
                usd.setText(""+eurtoeurinp);

                eurtousdval = Double.parseDouble(result[0]);
                eurtousdinp = inputvalue * eurtousdval;
                eur.setText(""+eurtousdinp);

                eurtoinrval = Double.parseDouble(result[1]);
                eurtoinrinp = inputvalue * eurtoinrval;
                eur.setText(""+eurtoinrinp);
            }
            else if(index==2){
                double inrtousdval, inrtoeurval, inrtousdinp, inrtoinrinp, inrtoeurinp;

                inrtoinrinp = inputvalue * 1;
                usd.setText(""+inrtoinrinp);

                inrtousdval = Double.parseDouble(result[0]);
                inrtousdinp = inputvalue * inrtousdval;
                eur.setText(""+inrtousdinp);

                inrtoeurval = Double.parseDouble(result[1]);
                inrtoeurinp = inputvalue * inrtoeurval;
                eur.setText(""+inrtoeurinp);
            }
        }

        String getJson(String url) throws ClientProtocolException, IOException{
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String con;
            while((con = reader.readLine())!=null){
                builder.append(con);
            }
            return builder.toString();
        }
    }
}
