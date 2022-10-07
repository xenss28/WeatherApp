package com.example.weatherproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRelative;
    private ProgressBar loadingG;
    private TextView idCity, idTVTemperature, idTVCondition;
    private RecyclerView idWeather;
    private TextInputEditText cityEdit;
    private ImageView backH, iconH, searchH;
    private ArrayList<Weather> weatherArrayList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;


    String url = "http://api.weatherapi.com/v1/forecast.json?key=27e50c52b3e44d0d9e783529220610&q=Indonesia&days=1&aqi=yes&alerts=yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeRelative = findViewById(R.id.idRLHome);
        loadingG = findViewById(R.id.BLoading);
        idCity = findViewById(R.id.idTVCityName);
        idTVTemperature = findViewById(R.id.idTemperature);
        idTVCondition = findViewById(R.id.idCondition);
        idWeather = findViewById(R.id.idWeather);
        cityEdit = findViewById(R.id.idEditCity);
        backH = findViewById(R.id.idBack);
        iconH = findViewById(R.id.idIcon);
        searchH = findViewById(R.id.idSearch);
        backH = findViewById(R.id.idBack);
        weatherArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherArrayList);
        idWeather.setAdapter(weatherAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(),location.getLatitude());
        getWeatherInfo(cityName);

        searchH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEdit.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter city name", Toast.LENGTH_SHORT).show();
                }else{
                    idCity.setText(cityName);
                    getWeatherInfo(city);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissions granted...", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(longitude,latitude, 10);
            for (Address adr : addresses){
                if (adr!=null){
                    String city = adr.getLocality();
                    if (city!=null && !city.equals("")){
                        cityName = city;
                    }else{
                        Log.d("TAG", "CTIY NOT FOUND");
                        Toast.makeText(this, "User city not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo (String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=27e50c52b3e44d0d9e783529220610&q=" + cityName +"&days=1&aqi=yes&alerts=yes";
        idCity.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingG.setVisibility(View.GONE);
                homeRelative.setVisibility(View.VISIBLE);
                weatherArrayList.clear();

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    idTVTemperature.setText(temperature+"°c");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http://".concat(conditionIcon)).into(iconH);
                    idTVCondition.setText(condition);
                    if (isDay==1){
                        Picasso.get().load("https://images.unsplash.com/photo-1500382017468-9049fed747ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8bW9ybmluZ3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60").into(backH);
                    }else {
                        Picasso.get().load("https://images.unsplash.com/photo-1505322022379-7c3353ee6291?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8bmlnaHR8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60").into(backH);
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastO.getJSONArray("hour");

                    for (int i=0; i<hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("time").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherArrayList.add(new Weather(time, temper, img, wind));
                    }
                    weatherAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}