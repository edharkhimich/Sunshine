package com.example.appleeeee.myapplication.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appleeeee.myapplication.MainActivity;
import com.example.appleeeee.myapplication.R;
import com.example.appleeeee.myapplication.adapter.ForecastAdapter;
import com.example.appleeeee.myapplication.api.Api;
import com.example.appleeeee.myapplication.model.Forecast;
import com.example.appleeeee.myapplication.model.Model;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    @BindView(R.id.location_text_view)
    TextView locationTv;
    @BindView(R.id.condition_text_view)
    TextView conditionTv;
    @BindView(R.id.temperature_text_view)
    TextView temperatureTv;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    private String conditionText;
    private String city;
    private String temperatureF;
    private int temperatureC;
    private String lastBuildDate;
    private String cityBundle;
    private String query;
    private List<Forecast> list;
    private ForecastAdapter adapter;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).showProgress();
        View v = inflater.inflate(R.layout.main_fragment, container, false);

        JodaTimeAndroid.init(getActivity());
        ButterKnife.bind(this, v);

        Bundle bundle = this.getArguments();
        cityBundle = bundle.getString(CityFragment.KEY);
        query = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + cityBundle + "\")";

        makingRequest();
        ((MainActivity) getActivity()).dismissDialog();
        return v;
    }


    private void formatTime() {
        DateTime dt = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat
                .forPattern("EEE, dd MMM yyyy HH:mm a z");
//        if (lastBuildDate.contains("EEST")) {
//            dt = formatter.parseDateTime(lastBuildDate.replace("EEST", "EST"));
//        } else if (lastBuildDate.contains("BST")) {
//            dt = formatter.parseDateTime(lastBuildDate.replace("BST", "GMT"));
//        } else if (lastBuildDate.contains("CEST")) {
//            dt = formatter.parseDateTime(lastBuildDate.replace("CEST", "PDT"));
//        }
//        dt = formatter.parseDateTime(lastBuildDate);
//        }
        DateTimeFormatter formatter2 = DateTimeFormat
                .forPattern("HH:mm");

        lastBuildDate = formatter2.print(dt);
    }

    private void setAdapter() {
        adapter = new ForecastAdapter(getActivity());

        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void makingRequest() {
        Api.getInstance().getApiInterface().getWeather(query, "json")
                .enqueue(new Callback<Model>() {
                             @Override
                             public void onResponse(Call<Model> call, Response<Model> response) {
                                 if (response.code() == 200) {
                                     if (response.body().getQuery().getResults() != null) {
                                         city = response.body().getQuery().getResults().getChannel().getLocation().getCity();
                                         temperatureF = response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp();
                                         conditionText = response.body().getQuery().getResults().getChannel().getItem().getCondition().getText();
                                         lastBuildDate = response.body().getQuery().getResults().getChannel().getLastBuildDate();
                                         list = response.body().getQuery().getResults().getChannel().getItem().getForecast();
//                                         formatTime();
                                         Log.d("mLog", "LastBuildDate = " + lastBuildDate);
                                         temperatureC = (int) ((Integer.valueOf(temperatureF) - 32) / 1.8000);
                                         if (city != null) {
                                             locationTv.setText(city);
                                         } else {
                                             locationTv.setText("Unknown location");
                                         }
                                         if (temperatureF != null) {
                                             temperatureTv.setText((temperatureC) + " \u00B0C");
                                         } else {
                                             temperatureTv.setText("Unknown temperature");
                                         }
                                         if (conditionText != null) {
                                             if (conditionText.equalsIgnoreCase("Scattered Showers")) {
                                                 weatherIcon.setImageResource(R.drawable.scattered_showers);
                                             } else if (conditionText.equalsIgnoreCase("Showers")
                                                     || conditionText.equalsIgnoreCase("Rain")
                                                     || conditionText.equalsIgnoreCase("Mixed Rain And Hail")) {
                                                 weatherIcon.setImageResource(R.drawable.rain);
                                             } else if (conditionText.equalsIgnoreCase("Tornado")
                                                     || conditionText.equalsIgnoreCase("Tropical Storm")
                                                     || conditionText.equalsIgnoreCase("Hurricane")
                                                     || conditionText.equalsIgnoreCase("Severe Thunderstorms")
                                                     || conditionText.equalsIgnoreCase("Thunderstorms")) {
                                                 weatherIcon.setImageResource(R.drawable.tornado);
                                             } else if (conditionText.equalsIgnoreCase("Isolated Thunderstorms")
                                                     || conditionText.equalsIgnoreCase("Scattered Thunderstorms")
                                                     || conditionText.equalsIgnoreCase("Scattered Showers")) {
                                                 weatherIcon.setImageResource(R.drawable.isolated_thunderstorms);
                                             } else if (conditionText.equalsIgnoreCase("Partly Cloudy")) {
                                                 weatherIcon.setImageResource(R.drawable.partly_cloudy);
                                             } else if (conditionText.equalsIgnoreCase("Mostly Cloudy")
                                                     || conditionText.equalsIgnoreCase("Cloudy")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy);
                                             } else if (conditionText.equalsIgnoreCase("Mostly Cloudy (Day)")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy_day);
                                             } else if (conditionText.equalsIgnoreCase("Mostly Cloudy (Night)")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy_night);
                                             } else if (conditionText.equalsIgnoreCase("Partly Cloudy (Night)")) {
                                                 weatherIcon.setImageResource(R.drawable.partly_cloudy_night);
                                             } else if (conditionText.equalsIgnoreCase("Clear (Night)")
                                                     || conditionText.equalsIgnoreCase("Fair (Night)")) {
                                                 weatherIcon.setImageResource(R.drawable.clear_night);
                                             } else if (conditionText.equalsIgnoreCase("Sunny")
                                                     || conditionText.equalsIgnoreCase("Fair (Day)")
                                                     || conditionText.equalsIgnoreCase("Hot")) {
                                                 weatherIcon.setImageResource(R.drawable.sunny);
                                             } else if (conditionText.equalsIgnoreCase("Breezy")
                                                     || conditionText.equalsIgnoreCase("Partly Cloudy (Day)")) {
                                                 weatherIcon.setImageResource(R.drawable.breeze);
                                             } else if (conditionText.equalsIgnoreCase("Mixed Rain And Snow")
                                                     || conditionText.equalsIgnoreCase("Mixed Rain And Sleet")
                                                     || conditionText.equalsIgnoreCase("Mixed Snow And Sleet")) {
                                                 weatherIcon.setImageResource(R.drawable.mixed_rain_and_snow);
                                             } else if (conditionText.equalsIgnoreCase("Freezing Drizzle")) {
                                                 weatherIcon.setImageResource(R.drawable.freezy);
                                             } else if (conditionText.equalsIgnoreCase("Drizzle")
                                                     || conditionText.equalsIgnoreCase("Freezing Rain")) {
                                                 weatherIcon.setImageResource(R.drawable.drizzle);
                                             } else if (conditionText.equalsIgnoreCase("Foggy")) {
                                                 weatherIcon.setImageResource(R.drawable.mostly_cloudy);
                                             } else if (conditionText.equalsIgnoreCase("Hail")) {
                                                 weatherIcon.setImageResource(R.drawable.hail);
                                             } else if (conditionText.equalsIgnoreCase("Dust")
                                                     || conditionText.equalsIgnoreCase("Foggy")
                                                     || conditionText.equalsIgnoreCase("Haze")
                                                     || conditionText.equalsIgnoreCase("Smoky")) {
                                                 weatherIcon.setImageResource(R.drawable.dust);
                                             } else if (conditionText.equalsIgnoreCase("Scattered Snow Showers")) {
                                                 weatherIcon.setImageResource(R.drawable.scattered_snow_showers);
                                             } else if (conditionText.equalsIgnoreCase("Blustery")
                                                     || conditionText.equalsIgnoreCase("Windy")
                                                     || conditionText.equalsIgnoreCase("Cold")) {
                                                 weatherIcon.setImageResource(R.drawable.blustery);
                                             } else if (conditionText.equalsIgnoreCase("Snow")
                                                     || conditionText.equalsIgnoreCase("Snow Showers")
                                                     || conditionText.equalsIgnoreCase("Heavy Snow")
                                                     || conditionText.equalsIgnoreCase("Snow Flurries")
                                                     || conditionText.equalsIgnoreCase("Light Snow Showers")
                                                     || conditionText.equalsIgnoreCase("Blowing Snow")
                                                     || conditionText.equalsIgnoreCase("Sleet")) {
                                                 weatherIcon.setImageResource(R.drawable.snow);
                                             }
                                             else {
                                                 weatherIcon.setImageResource(R.drawable.na);
                                             }
                                             conditionTv.setText(conditionText);
                                         } else {
                                             conditionTv.setText("Unknown condition");
                                         }
                                         setAdapter();
                                     } else {
                                         Toast.makeText(getActivity(), "Please enter correct city", Toast.LENGTH_LONG).show();
                                         ((MainActivity) getActivity()).changeFragment(new CityFragment(), false);
                                     }
                                 } else {
                                     Toast.makeText(getActivity(), "Wrong response. " +
                                             "Please check all your details" +
                                             " and internet connection", Toast.LENGTH_LONG).show();
                                 }
                             }

                             @Override
                             public void onFailure(Call<Model> call, Throwable t) {
                                 Log.d("TAG", "Error = " + String.valueOf(t.getMessage()));
                             }
                         }
                );
    }

}
