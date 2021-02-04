package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    JSONObject jsonPart;
    TextView cityName ;
    TextView details;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        details = findViewById(R.id.details);
        cityName = findViewById(R.id.cityName);
    }
    public class DownloadJson extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url ;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int data = inputStreamReader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();

                }
                return result;

            } catch (Exception e) {

                e.printStackTrace();

                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather info", weatherInfo);
                String message = "";

                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i=0 ; i < jsonArray.length(); i++){
                    jsonPart = jsonArray.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if (!main.equals("") && !description.equals("")){
                        message += main + ": " + description + "\r\n" ;
                    }
                }

                if (!message.equals("")){
                    details.setText(message);
                }else {
                    Toast.makeText(getApplicationContext() , "Cannot Find Weather :(" , Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext() , "Cannot Find Weather :(" , Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void getWeather(View view) throws JSONException {

        DownloadJson task = new DownloadJson();
        if(!cityName.getText().equals("")) {
            task.execute(" https://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + "&appid=b829e5b968f3b3e14575c2bdbac4a00d");

        }else {
            Toast.makeText(this , "Cannot Find Weather :(" , Toast.LENGTH_SHORT).show();

        }
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(details.getWindowToken(), 0);

    }
}