package com.example.weatherproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Weather> weatherArrayList;

    public WeatherAdapter(Context context, ArrayList<Weather> weatherArrayList) {
        this.context = context;
        this.weatherArrayList = weatherArrayList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {

        Weather weather = weatherArrayList.get(position);
        holder.temperatureV.setText(weather.getTemperature()+"°c");
        Picasso.get().load("http:".concat(weather.getIcon())).into(holder.conditionV);
        holder.windV.setText(weather.getWind()+"Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date t = input.parse(weather.getTime());
            holder.timeV.setText(output.format(t));
        } catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return weatherArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView windV, temperatureV, timeV;
        private ImageView conditionV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windV = itemView.findViewById(R.id.idWind);
            temperatureV = itemView.findViewById(R.id.idTemperaturee);
            timeV = itemView.findViewById(R.id.idTime);
            conditionV = itemView.findViewById(R.id.idConditionn);


        }
    }
}
