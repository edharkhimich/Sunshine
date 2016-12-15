package com.example.appleeeee.myapplication.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appleeeee.myapplication.R;
import com.example.appleeeee.myapplication.activity.MainActivity;
import com.example.appleeeee.myapplication.fragments.MainFragment;
import com.example.appleeeee.myapplication.fragments.WeatherForDayFragment;
import com.example.appleeeee.myapplication.model.Forecast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ItemHolder> {

    public static final String HIGH_TEMP_KEY = "093";
    public static final String LOW_TEMP_KEY = "092";

    List<Forecast> forecastList;
    Context context;
    Forecast forecast;
    private String temperatureHighF;
    private String temperatureLowF;
    private int temperatureHighC;
    private int temperatureLowC;
    private String condition;
    String thigh;

    public ForecastAdapter(Context context) {
        this.context = context;
        forecastList = new ArrayList<>();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        forecast = forecastList.get(position);
        temperatureHighF = forecast.getHigh();
        temperatureLowF = forecast.getLow();
        temperatureHighC = (int) ((Integer.valueOf(temperatureHighF) - 32) / 1.8000);
        temperatureLowC = (int) ((Integer.valueOf(temperatureLowF) - 32) / 1.8000);
        condition = forecast.getText();
        putImageToHolder(holder);
        holder.condition.setText(forecast.getText());
        holder.day.setText(forecast.getDate());
        if (MainFragment.isCelsiuos) {
            holder.tempHigh.setText(String.valueOf(temperatureHighC) + " \u00B0C");
            holder.tempLow.setText(String.valueOf(temperatureLowC) + " \u00B0C");
        } else {
            holder.tempHigh.setText(temperatureHighF + " \u00B0F");
            holder.tempLow.setText(temperatureLowF + " \u00B0F");
        }
        holder.setOnClick(holder);


    }


    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView itemImage;
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.condition)
        TextView condition;
        @BindView(R.id.temp_high)
        TextView tempHigh;
        @BindView(R.id.temp_low)
        TextView tempLow;
        @BindView(R.id.item_full)
        RelativeLayout itemFull;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setOnClick(final ItemHolder holder) {

            itemFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    WeatherForDayFragment dayWeatherFragment = new WeatherForDayFragment();
                    bundle.putString(HIGH_TEMP_KEY, String.valueOf(holder.tempHigh.getText()));
                    bundle.putString(LOW_TEMP_KEY, String.valueOf(holder.tempLow.getText()));
                    dayWeatherFragment.setArguments(bundle);
                    ((MainActivity)context).changeFragment(dayWeatherFragment, true);
                }
            });
        }
    }

    public void setList(List<Forecast> list) {
        this.forecastList = list;
        notifyItemRangeChanged(0, forecastList.size());
    }

    private void putImageToHolder(ItemHolder holder) {
        if (!condition.isEmpty()) {
            if (condition.equals("Scattered Showers")) {
                holder.itemImage.setImageResource(R.drawable.ic_light_rain);
            } else if (condition.equalsIgnoreCase("Showers") || condition.equalsIgnoreCase("Rain")) {
                holder.itemImage.setImageResource(R.drawable.ic_rain);
            } else if (condition.equalsIgnoreCase("Tornado") || condition.equalsIgnoreCase("Tropical Storm")
                    || condition.equalsIgnoreCase("Hurricane") || condition.equalsIgnoreCase("Thunderstorms")
                    || condition.equalsIgnoreCase("Severe Thunderstorms") || condition.equalsIgnoreCase("Thundershowers")) {
                holder.itemImage.setImageResource(R.drawable.ic_storm);
            } else if (condition.equalsIgnoreCase("Mixed Rain And Snow") || condition.equalsIgnoreCase("Mixed Rain And Sleet")
                    || condition.equalsIgnoreCase("Mixed Snow And Sleet") || condition.equalsIgnoreCase("Mixed Rain And Hail")) {
                holder.itemImage.setImageResource(R.drawable.ic_rain_snow);
            } else if (condition.equalsIgnoreCase("Partly Cloudy")) {
                holder.itemImage.setImageResource(R.drawable.ic_light_clouds);
            } else if (condition.equalsIgnoreCase("Mostly Cloudy") || condition.equalsIgnoreCase("Cloudy")) {
                holder.itemImage.setImageResource(R.drawable.ic_cloudy);
            } else if (condition.equalsIgnoreCase("Foggy") || condition.equalsIgnoreCase("Haze")
                    || condition.equalsIgnoreCase("Smoky")) {
                holder.itemImage.setImageResource(R.drawable.ic_fog);
            } else if (condition.equalsIgnoreCase("Snow") || condition.equalsIgnoreCase("Snow Flurries")
                    || condition.equalsIgnoreCase("Light Snow Showers") || condition.equalsIgnoreCase("Blowing Snow")
                    || condition.equalsIgnoreCase("Sleet") || condition.equalsIgnoreCase("Heavy Snow")
                    || condition.equalsIgnoreCase("Scattered Snow Showers") || condition.equalsIgnoreCase("Snow Showers")) {
                holder.itemImage.setImageResource(R.drawable.ic_snowing);
            } else if (condition.equalsIgnoreCase("Freezing Drizzle") || condition.equalsIgnoreCase("Drizzle")
                    || condition.equalsIgnoreCase("Freezing Rain")) {
                holder.itemImage.setImageResource(R.drawable.ic_freezing_drizzle);
            } else if (condition.equalsIgnoreCase("Hail")) {
                holder.itemImage.setImageResource(R.drawable.ic_hail);
            } else if (condition.equalsIgnoreCase("Dust")) {
                holder.itemImage.setImageResource(R.drawable.ic_dust);
            } else if (condition.equalsIgnoreCase("Blustery") || condition.equalsIgnoreCase("Windy")) {
                holder.itemImage.setImageResource(R.drawable.ic_wind);
            } else if (condition.equalsIgnoreCase("Clear (Night)")) {
                holder.itemImage.setImageResource(R.drawable.ic_clear);
            } else if (condition.equalsIgnoreCase("Sunny") || condition.equalsIgnoreCase("Fair (Day)")
                    || condition.equalsIgnoreCase("Hot")) {
                holder.itemImage.setImageResource(R.drawable.ic_sunny);
            } else if (condition.equalsIgnoreCase("Fair (night)")) {
                holder.itemImage.setImageResource(R.drawable.ic_fair_night);
            } else if (condition.equalsIgnoreCase("Isolated Thunderstorms") || condition.equalsIgnoreCase("Scattered Thunderstorms")
                    || condition.equalsIgnoreCase("Scattered Showers")) {
                holder.itemImage.setImageResource(R.drawable.ic_isolated_thunderstorms);
            } else {
                holder.itemImage.setImageResource(R.drawable.ic_light_clouds);
            }
        }
    }
}


